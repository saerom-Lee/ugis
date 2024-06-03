package ugis.cmmn.imgproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.coverage.processing.Operations;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.gce.geotiff.GeoTiffWriter;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ImgDataCropper {

	public GridCoverage2D referencingCrop(String inputPath, ReferencedEnvelope orgEnv, ReferencedEnvelope cropEnv,
			String outputPath, CoordinateReferenceSystem crs, CoordinateReferenceSystem targetCrs) throws IOException {

		System.out.println("[DEBUG]ImgDataCropper.referencingCrop : " + outputPath);

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

		if (cropGc != null) {
			return writeGeoTiff(cropGc, outputPath);
		} else {
			return null;
		}
	}

	/**
	 * Geotiff 파일 생성
	 * 
	 * @param gc
	 *            Geotiff 정보
	 * @param path
	 *            Geotiff 파일 경로
	 * @throws IllegalArgumentException
	 * @throws IndexOutOfBoundsException
	 * @throws IOException
	 */
	private GridCoverage2D writeGeoTiff(GridCoverage2D gc, String path)
			throws IllegalArgumentException, IndexOutOfBoundsException, IOException {

		System.out.println("[DEBUG]ImgDataCropper.writeGeoTiff : " + path);

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
