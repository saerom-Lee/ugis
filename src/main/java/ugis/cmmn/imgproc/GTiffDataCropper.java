package ugis.cmmn.imgproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.styling.StyleFactory;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.filter.FilterFactory;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import lombok.Data;

@Data
public class GTiffDataCropper {

	public static StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
	public static FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory();

	public static GridCoverage2D referencingCrop(String inputPath, ReferencedEnvelope orgEnv,
			ReferencedEnvelope cropEnv, String outputPath, CoordinateReferenceSystem targetCrs) throws IOException {

		System.out.println("[DEBUG]GTiffDataCropper.referencingCrop inputPath : " + inputPath);
		System.out.println("[DEBUG]GTiffDataCropper.referencingCrop outputPath : " + outputPath);

		File file = new File(inputPath);
		if (!file.exists()) {
			return null;
		}

		BufferedImage image = ImageIO.read(file);
		GridCoverageFactory gcf = new GridCoverageFactory();
		GridCoverage2D coverage = gcf.create("coverage", image, orgEnv);

		if (targetCrs != null) {
			try {
				CoordinateReferenceSystem orgCrs = coverage.getCoordinateReferenceSystem();
				Integer orgEpsg = CRS.lookupEpsgCode(orgCrs, false);
				Integer targetEpsg = CRS.lookupEpsgCode(targetCrs, false);
				if (!orgEpsg.equals(targetEpsg)) {
					coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, targetCrs);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		// set roi
		double minX = cropEnv.getMinX();
		double maxX = cropEnv.getMaxX();
		double minY = cropEnv.getMinY();
		double maxY = cropEnv.getMaxY();

		Coordinate[] coors = new Coordinate[5];
		coors[0] = new Coordinate(minX, minY);
		coors[1] = new Coordinate(maxX, minY);
		coors[2] = new Coordinate(maxX, maxY);
		coors[3] = new Coordinate(minX, maxY);
		coors[4] = new Coordinate(minX, minY);
		Geometry roi = new GeometryFactory().createPolygon(coors);

		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope((Envelope) cropEnv);
		intersectionEnvelope.setCoordinateReferenceSystem(coverage.getCoordinateReferenceSystem());
		// intersect the envelopes
		intersectionEnvelope.intersect(coverage.getEnvelope2D());

		// crop tif
		CoverageProcessor processor = CoverageProcessor.getInstance();
		final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
		param.parameter("Source").setValue(coverage);
		param.parameter("Envelope").setValue(cropEnv);
		param.parameter("ROI").setValue(roi);
		
		
		//새롬 NoData -> 검은색으로 수정
		Range nodata = RangeFactory.create(128d, 128d);
		param.parameter("NoData").setValue(nodata);
		
		GridCoverage2D cropGc = (GridCoverage2D) processor.doOperation(param);

		if (cropGc != null) {
			return writeGeoTiff(cropGc, outputPath);
		} else {
			return null;
		}

	}

	public GridCoverage2D referencingCrop2(String inputPath, ReferencedEnvelope orgEnv, ReferencedEnvelope cropEnv,
			String outputPath, CoordinateReferenceSystem targetCrs) throws IOException {

		System.out.println("[DEBUG]GTiffDataCropper.referencingCrop : " + outputPath);

		File file = new File(inputPath);
		if (!file.exists()) {
			return null;
		}

		BufferedImage image = ImageIO.read(file);
		GridCoverageFactory gcf = new GridCoverageFactory();
		GridCoverage2D coverage = gcf.create("coverage", image, orgEnv);

		if (targetCrs != null) {
			try {
				coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, targetCrs);
			} catch (Exception e) {
				return null;
			}
		}

		// set roi
		double minX = cropEnv.getMinX();
		double maxX = cropEnv.getMaxX();
		double minY = cropEnv.getMinY();
		double maxY = cropEnv.getMaxY();

		Coordinate[] coors = new Coordinate[5];
		coors[0] = new Coordinate(minX, minY);
		coors[1] = new Coordinate(maxX, minY);
		coors[2] = new Coordinate(maxX, maxY);
		coors[3] = new Coordinate(minX, maxY);
		coors[4] = new Coordinate(minX, minY);
		Geometry roi = new GeometryFactory().createPolygon(coors);

		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope((Envelope) cropEnv);
		intersectionEnvelope.setCoordinateReferenceSystem(coverage.getCoordinateReferenceSystem());
		// intersect the envelopes
		intersectionEnvelope.intersect(coverage.getEnvelope2D());

		// crop tif
		CoverageProcessor processor = CoverageProcessor.getInstance();
		final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
		param.parameter("Source").setValue(coverage);
		param.parameter("Envelope").setValue(cropEnv);
		param.parameter("ROI").setValue(roi);
		
		GridCoverage2D cropGc = (GridCoverage2D) processor.doOperation(param);

		return cropGc;

	}

	/**
	 * Geotiff Crop
	 * 
	 * @param inputPath  Geotiff 파일 경로
	 * @param envelope   Crop 영역
	 * @param outputPath Crop Geotiff 파일 경로
	 * @param crs        좌표계
	 * @throws IOException
	 */
	public GridCoverage2D crop(String inputPath, ReferencedEnvelope envelope, String outputPath,
			CoordinateReferenceSystem crs, CoordinateReferenceSystem targetCrs) throws IOException {

		System.out.println("[DEBUG]GTiffDataCropper.crop : " + outputPath);

		File file = new File(inputPath);
		if (!file.exists()) {
			return null;
		}

		GridCoverage2D coverage = null;
		try {
			GeoTiffReader reader = new GeoTiffReader(file,
					new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE));
			coverage = reader.read(null);
		} catch (Exception e1) {
			try {
				coverage = new GTiffDataReader(inputPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16).openTiffFile(inputPath,
						crs);
			} catch (Exception e) {
				return null;
			}
		}

		if (targetCrs != null) {
			try {
				coverage = (GridCoverage2D) Operations.DEFAULT.resample(coverage, targetCrs);
			} catch (Exception e) {
				return null;
			}
		}

		// set roi
		double minX = envelope.getMinX();
		double maxX = envelope.getMaxX();
		double minY = envelope.getMinY();
		double maxY = envelope.getMaxY();

		Coordinate[] coors = new Coordinate[5];
		coors[0] = new Coordinate(minX, minY);
		coors[1] = new Coordinate(maxX, minY);
		coors[2] = new Coordinate(maxX, maxY);
		coors[3] = new Coordinate(minX, maxY);
		coors[4] = new Coordinate(minX, minY);
		Geometry roi = new GeometryFactory().createPolygon(coors);

		final GeneralEnvelope intersectionEnvelope = new GeneralEnvelope((Envelope) envelope);
		intersectionEnvelope.setCoordinateReferenceSystem(coverage.getCoordinateReferenceSystem());
		// intersect the envelopes
		intersectionEnvelope.intersect(coverage.getEnvelope2D());

		// crop tif
		CoverageProcessor processor = CoverageProcessor.getInstance();
		final ParameterValueGroup param = processor.getOperation("CoverageCrop").getParameters();
		param.parameter("Source").setValue(coverage);
		param.parameter("Envelope").setValue(envelope);
		param.parameter("ROI").setValue(roi);
		GridCoverage2D cropGc = (GridCoverage2D) processor.doOperation(param);

		if (cropGc != null) {
			return writeGeoTiff(cropGc, outputPath);
		} else {
			return null;
		}

	}

	/**
	 * Geotiff 파일 생성
	 * 
	 * @param gc   Geotiff 정보
	 * @param path Geotiff 파일 경로
	 * @throws IllegalArgumentException
	 * @throws IndexOutOfBoundsException
	 * @throws IOException
	 */
	public static GridCoverage2D writeGeoTiff(GridCoverage2D gc, String path)
			throws IllegalArgumentException, IndexOutOfBoundsException, IOException {

		System.out.println("[DEBUG]GTiffDataCropper.writeGeoTiff : " + path);

		try {
			File file = new File(path);
			// 정방향 축 설정
			ParameterValueGroup params = new GeoTiffFormat().getWriteParameters();
			params.parameter(GeoTiffFormat.RETAIN_AXES_ORDER.getName().toString()).setValue(true);

			GeoTiffWriter gtWriter = new GeoTiffWriter(file);
			gtWriter.write(gc, (GeneralParameterValue[]) params.values().toArray(new GeneralParameterValue[1]));
			gtWriter.dispose();
		} catch (Exception e) {
			return null;
		}
		return gc;
	}
}
