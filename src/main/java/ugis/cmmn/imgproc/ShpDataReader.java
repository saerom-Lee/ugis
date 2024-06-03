package ugis.cmmn.imgproc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDumper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.label.LabelCacheImpl;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Fill;
import org.geotools.styling.LineSymbolizer;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Rule;
import org.geotools.styling.Stroke;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ShpDataReader {

	public File orgShp;

	public boolean isDigital;
	public boolean isAnals;

	public static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
	public static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

	public int width;
	public int height;
	public ReferencedEnvelope env;

	public ShpDataReader(File shpFile) {
		this.orgShp = shpFile;
	}

	private Geometry getIntersection(Geometry envelope, Geometry geometry, MathTransform transform) {
		try {
			if (transform != null) {
				Geometry targetGeometry = JTS.transform(geometry, transform);
				Geometry interGeom = envelope.intersection(targetGeometry);
				if (interGeom != null && !interGeom.isEmpty()) {
					return interGeom;
				}
			} else {
				Geometry interGeom = envelope.intersection(geometry);
				if (interGeom != null) {
					return interGeom;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public String cropAndToImage(double ulx, double uly, double lrx, double lry, String targetEPSG, String path)
			throws Exception {

		Map<String, Object> map = new HashMap<>();
		map.put("url", orgShp.toURI().toURL());

		DataStore dataStore = DataStoreFinder.getDataStore(map);
		String typeName = dataStore.getTypeNames()[0];

		FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
		SimpleFeatureType sfType = source.getSchema();
		Integer shpEPSG = CRS.lookupEpsgCode(sfType.getCoordinateReferenceSystem(), false);

		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
		CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
		CoordinateReferenceSystem targetCrs = factory.createCoordinateReferenceSystem(targetEPSG); // 5179
		CoordinateReferenceSystem shpCrs = factory.createCoordinateReferenceSystem(shpEPSG.toString()); // 32652

		MathTransform transform = null;
		if (!shpEPSG.toString().contentEquals(targetEPSG)) {
			transform = CRS.findMathTransform(shpCrs, targetCrs, true);
		}

		ReferencedEnvelope env = new ReferencedEnvelope(ulx, lrx, lry, uly, shpCrs);
		Geometry envelpoeGeom = new GeometryFactory().toGeometry(env);

		this.width = (int) env.getWidth();
		this.height = (int) env.getHeight();
		this.env = env;

		Filter filter = Filter.INCLUDE;
		SimpleFeatureCollection collection = (SimpleFeatureCollection) source.getFeatures(filter);

		sfType = DataUtilities.createSubType(sfType, null, targetCrs);
		DefaultFeatureCollection dfc = new DefaultFeatureCollection();
		SimpleFeatureIterator iter = collection.features();
//		try {
		while (iter.hasNext()) {
			SimpleFeature feature = iter.next();
			Geometry geom = getIntersection(envelpoeGeom, (Geometry) feature.getDefaultGeometry(), transform);
			if (geom != null) {
				List<PropertyDescriptor> pds = (List<PropertyDescriptor>) sfType.getDescriptors();
				List<Object> valueList = new ArrayList<Object>();
				for (int p = 0; p < pds.size(); p++) {
					String name = pds.get(p).getName().toString();
					Object value = feature.getAttribute(name);
					valueList.add(value);
				}
				SimpleFeature newSf = SimpleFeatureBuilder.build(sfType,
						valueList.toArray(new Object[valueList.size()]), feature.getID());
				newSf.setDefaultGeometry(geom);
				dfc.add(newSf);
			}
		}
//		} finally {
//			dataStore.dispose();
//			iter.close();
//		}

		// shp to image
		return shpToImage(path, dfc);
	}

	public String cropAndWrite2(double ulx, double uly, double lrx, double lry, String targetEPSG, String path) {

		if (!orgShp.exists()) {
			return null;
		}

		try {
			Map<String, Object> map = new HashMap<>();
			map.put("url", orgShp.toURI().toURL());

			DataStore dataStore = DataStoreFinder.getDataStore(map);
			String typeName = dataStore.getTypeNames()[0];

			FeatureSource<SimpleFeatureType, SimpleFeature> source = dataStore.getFeatureSource(typeName);
			SimpleFeatureType sfType = source.getSchema();
			Integer shpEPSG = CRS.lookupEpsgCode(sfType.getCoordinateReferenceSystem(), false);

			Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
			CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
			CoordinateReferenceSystem targetCrs = factory.createCoordinateReferenceSystem(targetEPSG); // 5179
			CoordinateReferenceSystem shpCrs = factory.createCoordinateReferenceSystem(shpEPSG.toString()); // 32652

			MathTransform transform = null;
			if (!shpEPSG.toString().contentEquals(targetEPSG)) {
				transform = CRS.findMathTransform(shpCrs, targetCrs, true);
			}

			ReferencedEnvelope env = new ReferencedEnvelope(ulx, lrx, lry, uly, targetCrs);
			Geometry envelpoeGeom = new GeometryFactory().toGeometry(env);

			this.width = (int) env.getWidth();
			this.height = (int) env.getHeight();
			this.env = env;

			Filter filter = Filter.INCLUDE;
			SimpleFeatureCollection collection = (SimpleFeatureCollection) source.getFeatures(filter);

			ReferencedEnvelope bounds = collection.getBounds();
			ReferencedEnvelope orgEnv = new ReferencedEnvelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(),
					bounds.getMaxY(), shpCrs);
			Envelope transEnv = JTS.transform(orgEnv, transform);

			if (!env.intersects(transEnv)) {
				return null;
			} else if (env.contains(transEnv)) {
				env = new ReferencedEnvelope(transEnv.getMinX(), transEnv.getMaxX(), transEnv.getMinY(),
						transEnv.getMaxY(), targetCrs);
			}

			sfType = DataUtilities.createSubType(sfType, null, targetCrs);
			DefaultFeatureCollection dfc = new DefaultFeatureCollection();
			SimpleFeatureIterator iter = collection.features();
			while (iter.hasNext()) {
				SimpleFeature feature = iter.next();
				Geometry geom = getIntersection(envelpoeGeom, (Geometry) feature.getDefaultGeometry(), transform);
				if (geom != null) {
					List<PropertyDescriptor> pds = (List<PropertyDescriptor>) sfType.getDescriptors();
					List<Object> valueList = new ArrayList<Object>();
					for (int p = 0; p < pds.size(); p++) {
						String name = pds.get(p).getName().toString();
						Object value = feature.getAttribute(name);
						valueList.add(value);
					}
					SimpleFeature newSf = SimpleFeatureBuilder.build(sfType,
							valueList.toArray(new Object[valueList.size()]), feature.getID());
					newSf.setDefaultGeometry(geom);
					dfc.add(newSf);
				}
			}
			iter.close();
			dataStore.dispose();

			// crop shp 생성
			File dir = new File(path).getParentFile();
			String fileNm = new File(path).getName();
			String name = fileNm.replace(".shp", "");
			ShapefileDumper dumper = new ShapefileDumper(dir);
			dumper.dump(name, DataUtilities.collection(dfc));

			// shp to image
			shpToImage(path, dfc);

			// _tmp shp 삭제
			File[] tmpFiles = orgShp.getParentFile().listFiles();
			for (File tmp : tmpFiles) {
				if (tmp.getName().contains("_tmp")) {
					tmp.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

			File[] tmpFiles = new File(path).getParentFile().listFiles();
			for (File tmp : tmpFiles) {
				tmp.delete();
			}
		}
		return path;
	}

	public String readToByte(String shpPath) throws IOException {

		// read file
		File shpFile = new File(shpPath);
		if (!shpFile.exists()) {
			return null;
		}

		File fileDir = shpFile.getParentFile();
		String shpName = shpFile.getName();
		String ext = FilenameUtils.getExtension(shpName);
		String name = shpName.replace("." + ext, "");

		File[] allFileList = fileDir.listFiles();

		List<File> fileList = new ArrayList<>();
		for (int f = 0; f < allFileList.length; f++) {
			File file = allFileList[f];
			String fnm = file.getName();
			String fext = FilenameUtils.getExtension(fnm);
			String fname = fnm.replace("." + fext, "");

			if (fname.toUpperCase().equals(name.toUpperCase())) {
				if (fext.equals("shp")) {
					fileList.add(file);
				}
				if (fext.equals("shx")) {
					fileList.add(file);
				}
				if (fext.equals("dbf")) {
					fileList.add(file);
				}
				if (fext.equals("prj")) {
					fileList.add(file);
				}
			}
		}

		// zip file
		String zipPath = fileDir.getAbsolutePath() + "/" + name + ".zip";
		File zipAbsFile = new File(zipPath).getAbsoluteFile();
		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipAbsFile));
		byte[] buf = new byte[4096];

		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);

			// copy
			String fname = file.getName();
			String fext = FilenameUtils.getExtension(fname);
			String nm = fname.replace("." + fext, "") + "_copy";
			File copyFile = new File(fileDir + File.separator + nm + "." + fext);
			Files.copy(file.toPath(), copyFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

			FileInputStream fis = new FileInputStream(copyFile);
			ZipEntry ze = new ZipEntry(fname);
			zos.putNextEntry(ze);
			int len;
			while ((len = fis.read(buf)) > 0) {
				zos.write(buf, 0, len);
			}
			zos.closeEntry();
			fis.close();
		}
		zos.close();
		// zip to byte[]
		byte[] byteContent = Files.readAllBytes(zipAbsFile.toPath());
		String encodedStr = Base64.getEncoder().encodeToString(byteContent);

		// delete copy file
		File[] dirFiles = fileDir.listFiles();
		for (File dirFile : dirFiles) {
			if (dirFile.getName().contains("_copy") || dirFile.getName().endsWith(".zip")) {
				dirFile.delete();
			}
		}
		return encodedStr;
	}

	public String shpToImage(String path, DefaultFeatureCollection dfc) throws IOException {

		MapContent mapContent = new MapContent();
		mapContent.setTitle("Quickstart");

		Style style = null;
		if (isDigital) { // 수치지도 스타일
			String geomTy = dfc.getSchema().getGeometryDescriptor().getType().getName().toString();
			if (geomTy.contains("Polygon")) {
				style = createDigitalPolygonStyle();
			} else if (geomTy.contains("LineString")) {
				style = createDigitalLineStringStyle();
			}
		}
		if (isAnals) { // 분석결과 스타일
			style = createAnalsPolygonStyle();
		}

		Layer layer = new FeatureLayer(dfc, style);
		mapContent.addLayer(layer);

		String imagePath = path.replace(".shp", ".png");
		File outputFile = new File(imagePath);
		try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
				ImageOutputStream outputImageFile = ImageIO.createImageOutputStream(fileOutputStream);) {

//			ReferencedEnvelope bounds = dfc.getBounds();
			int w = width;
			int h = height;

//			System.out.println(">>>>>>>>>>>>>>>>>>> shp bounds : " + bounds.toString());
			System.out.println(">>>>>>>>>>>>>>>>>>> shp h : " + h);
			System.out.println(">>>>>>>>>>>>>>>>>>> shp w : " + w);

			BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g2d = bufferedImage.createGraphics();

			mapContent.getViewport().setMatchingAspectRatio(true);
			mapContent.getViewport().setScreenArea(new Rectangle(Math.round(w), Math.round(h)));
			mapContent.getViewport().setBounds(env);

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Rectangle outputArea = new Rectangle(w, h);
			GTRenderer renderer = new StreamingRenderer();
			LabelCacheImpl labelCache = new LabelCacheImpl();
			Map<Object, Object> hints = renderer.getRendererHints();

			if (hints == null) {
				hints = new HashMap<>();
			}

			hints.put(StreamingRenderer.LABEL_CACHE_KEY, labelCache);
			renderer.setRendererHints(hints);
			renderer.setMapContent(mapContent);
			renderer.paint(g2d, outputArea, env);
			ImageIO.write(bufferedImage, "png", outputImageFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return imagePath;
	}

	private Style createDigitalPolygonStyle() {

		// create a partially opaque outline stroke
		Stroke stroke = styleFactory.createStroke(filterFactory.literal(new Color(0, 0, 0)), filterFactory.literal(0.5),
				filterFactory.literal(0.8));

		// create a partial opaque fill
		Fill fill = styleFactory.createFill(filterFactory.literal(new Color(170, 170, 170)), filterFactory.literal(1));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to draw the
		 * default geomettry of features
		 */
		PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	private Style createDigitalLineStringStyle() {

		// create a partially opaque outline stroke
		Stroke stroke = styleFactory.createStroke(filterFactory.literal(new Color(0, 0, 255)), filterFactory.literal(1),
				filterFactory.literal(1));
		/*
		 * Setting the geometryPropertyName arg to null signals that we want to draw the
		 * default geomettry of features
		 */
		LineSymbolizer sym = styleFactory.createLineSymbolizer(stroke, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

	private Style createAnalsPolygonStyle() {

		// create a partially opaque outline stroke
		Stroke stroke = styleFactory.createStroke(filterFactory.literal(new Color(83, 170, 214)),
				filterFactory.literal(0.5), filterFactory.literal(1));

		// create a partial opaque fill
		Fill fill = styleFactory.createFill(filterFactory.literal(new Color(255, 255, 255)), filterFactory.literal(1));

		/*
		 * Setting the geometryPropertyName arg to null signals that we want to draw the
		 * default geomettry of features
		 */
		PolygonSymbolizer sym = styleFactory.createPolygonSymbolizer(stroke, fill, null);

		Rule rule = styleFactory.createRule();
		rule.symbolizers().add(sym);
		FeatureTypeStyle fts = styleFactory.createFeatureTypeStyle(rule);
		Style style = styleFactory.createStyle();
		style.featureTypeStyles().add(fts);

		return style;
	}

}
