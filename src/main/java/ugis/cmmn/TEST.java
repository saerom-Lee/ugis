package ugis.cmmn;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import ugis.cmmn.imgproc.GTiffDataCropper;

public class TEST {

	public static void main(String[] args) throws Exception {

		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
		CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
		CoordinateReferenceSystem targetCrs = factory.createCoordinateReferenceSystem("EPSG:5179"); // 5179

		double ltopCrdntX = 1133432.77534192;
		double ltopCrdntY = 1955317.65348952;
		double rbtmCrdntX = 1143464.98003806;
		double rbtmCrdntY = 1949891.23602095;

		CoordinateReferenceSystem sourceCrs = factory.createCoordinateReferenceSystem("EPSG:5179");
		ReferencedEnvelope envelope5179 = new ReferencedEnvelope(ltopCrdntX, rbtmCrdntX, rbtmCrdntY, ltopCrdntY,
				sourceCrs);

		double ltopCrdntX1 = 1134231.3741627200506628;
		double ltopCrdntY1 = 1955530.5514764699619263;
		double rbtmCrdntX1 = 1143303.0392834814265370;
		double rbtmCrdntY1 = 1949760.4487039765808731;

		ReferencedEnvelope orgEnv = new ReferencedEnvelope(ltopCrdntX1, rbtmCrdntX1, rbtmCrdntY1, ltopCrdntY1,
				sourceCrs);

		GTiffDataCropper cropper = new GTiffDataCropper();

		String fullFileCoursNm = "C:\\Users\\admin\\Desktop\\새 폴더\\Existing\\Image\\Satellite\\2022\\Sentinel\\Sentinel_yw_20220315_RGB.tif";
		String cropedPath = "C:\\Users\\admin\\Desktop\\새 폴더\\Existing\\Image\\Satellite\\2022\\Sentinel\\Sentinel_yw_20220315_RGB_c.tif";

		GridCoverage2D cropedGc = cropper.referencingCrop(fullFileCoursNm, orgEnv, envelope5179, cropedPath, targetCrs);
		System.out.println();
	}
}