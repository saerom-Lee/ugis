package ugis.cmmn;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.collections.map.HashedMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Envelope;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataCropper;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.ShpDataReader;

public class TEST2 {

	public static void main(String[] args) throws Exception {

//		Map<String, GridCoverage2D> tifMap = new HashedMap();
//		GridCoverageFactory gcf = new GridCoverageFactory();
//
//		Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
//		CRSAuthorityFactory factory = ReferencingFactoryFinder.getCRSAuthorityFactory("EPSG", hints);
//		CoordinateReferenceSystem targetCrs = factory.createCoordinateReferenceSystem("EPSG:5179"); // 5179
//
//		ReferencedEnvelope orgEnv1 = new ReferencedEnvelope(1039363.5829000000376254, 1041698.0768000000389293,
//				2013902.6514000000897795, 2016811.7531000000890344, targetCrs);
//		BufferedImage image1 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716049s.tif"));
//		GridCoverage2D coverage1 = gcf.create("coverage", image1, orgEnv1);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716049s.tif", coverage1);
//
//		ReferencedEnvelope orgEnv2 = new ReferencedEnvelope(1041553.6509999999543652, 1043889.1391000000294298,
//				2013913.8351000000257045, 2016822.9353000000119209, targetCrs);
//		BufferedImage image2 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716050s.tif"));
//		GridCoverage2D coverage2 = gcf.create("coverage", image2, orgEnv2);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716050s.tif", coverage2);
//
//		ReferencedEnvelope orgEnv3 = new ReferencedEnvelope(1039376.6286000000545755, 1041712.1132999999681488,
//				2011128.8360999999567866, 2014037.9361000000499189, targetCrs);
//		BufferedImage image3 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716059s.tif"));
//		GridCoverage2D coverage3 = gcf.create("coverage", image3, orgEnv3);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716059s.tif", coverage3);
//
//		ReferencedEnvelope orgEnv4 = new ReferencedEnvelope(1041567.6960999999428168, 1043904.1748999999836087,
//				2011140.0208999998867512, 2014049.1195000000298023, targetCrs);
//		BufferedImage image4 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716060s.tif"));
//		GridCoverage2D coverage4 = gcf.create("coverage", image4, orgEnv4);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716060s.tif", coverage4);
//
//		ReferencedEnvelope orgEnv5 = new ReferencedEnvelope(1039389.6825999999418855, 1041726.1581000000005588,
//				2008355.0208999998867512, 2011264.1192000000737607, targetCrs);
//		BufferedImage image5 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716069s.tif"));
//		GridCoverage2D coverage5 = gcf.create("coverage", image5, orgEnv5);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716069s.tif", coverage5);
//
//		ReferencedEnvelope orgEnv6 = new ReferencedEnvelope(1041581.7495000000344589, 1043919.2190999999875203,
//				2008366.2069000001065433, 2011275.3038000001106411, targetCrs);
//		BufferedImage image6 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716070s.tif"));
//		GridCoverage2D coverage6 = gcf.create("coverage", image6, orgEnv6);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716070s.tif", coverage6);
//
//		ReferencedEnvelope orgEnv7 = new ReferencedEnvelope(1039403.7444, 1041740.2112, 2005581.2059, 2008490.2972,
//				targetCrs);
//		BufferedImage image7 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716079s.tif"));
//		GridCoverage2D coverage7 = gcf.create("coverage", image7, orgEnv7);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716079s.tif", coverage7);
//
//		ReferencedEnvelope orgEnv8 = new ReferencedEnvelope(1041595.8112, 1043934.2716, 2005592.3931, 2008501.4883,
//				targetCrs);
//		BufferedImage image8 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716080s.tif"));
//		GridCoverage2D coverage8 = gcf.create("coverage", image8, orgEnv8);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\cent\\38716\\38716080s.tif", coverage8);
//
//		ReferencedEnvelope orgEnv9 = new ReferencedEnvelope(1043743.714, 1046080.2016, 2013925.0186, 2016835.1169,
//				targetCrs);
//		BufferedImage image9 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813041s.tif"));
//		GridCoverage2D coverage9 = gcf.create("coverage", image9, orgEnv9);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813041s.tif", coverage9);
//
//		ReferencedEnvelope orgEnv10 = new ReferencedEnvelope(1045934.7768, 1048272.2638, 2013937.1961, 2016848.2926,
//				targetCrs);
//		BufferedImage image10 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813042s.tif"));
//		GridCoverage2D coverage10 = gcf.create("coverage", image10, orgEnv10);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813042s.tif", coverage10);
//
//		ReferencedEnvelope orgEnv11 = new ReferencedEnvelope(1048124.8402, 1050463.3213, 2013950.3785, 2016861.4735,
//				targetCrs);
//		BufferedImage image11 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813043s.tif"));
//		GridCoverage2D coverage11 = gcf.create("coverage", image11, orgEnv11);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813043s.tif", coverage11);
//
//		ReferencedEnvelope orgEnv12 = new ReferencedEnvelope(1043758.7585, 1046096.2368, 2011151.2056, 2014061.3023,
//				targetCrs);
//		BufferedImage image12 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813051s.tif"));
//		GridCoverage2D coverage12 = gcf.create("coverage", image12, orgEnv12);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813051s.tif", coverage12);
//
//		ReferencedEnvelope orgEnv13 = new ReferencedEnvelope(1045949.8211, 1048288.2988, 2011163.3897, 2014074.4845,
//				targetCrs);
//		BufferedImage image13 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813052s.tif"));
//		GridCoverage2D coverage13 = gcf.create("coverage", image13, orgEnv13);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813052s.tif", coverage13);
//
//		ReferencedEnvelope orgEnv14 = new ReferencedEnvelope(1048141.878, 1050480.3557, 2011175.5737, 2014087.6612,
//				targetCrs);
//		BufferedImage image14 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813053s.tif"));
//		GridCoverage2D coverage14 = gcf.create("coverage", image14, orgEnv14);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813053s.tif", coverage14);
//
//		ReferencedEnvelope orgEnv15 = new ReferencedEnvelope(1043773.8113, 1046112.2803, 2008377.3928, 2011287.4878,
//				targetCrs);
//		BufferedImage image15 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813061s.tif"));
//		GridCoverage2D coverage15 = gcf.create("coverage", image15, orgEnv15);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813061s.tif", coverage15);
//
//		ReferencedEnvelope orgEnv16 = new ReferencedEnvelope(1043773.8113, 1046112.2803, 2008377.3928, 2011287.4878,
//				targetCrs);
//		BufferedImage image16 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813061s.tif"));
//		GridCoverage2D coverage16 = gcf.create("coverage", image16, orgEnv16);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813061s.tif", coverage16);
//
//		ReferencedEnvelope orgEnv17 = new ReferencedEnvelope(1045965.8732, 1048304.3368, 2008389.5835, 2011299.6717,
//				targetCrs);
//		BufferedImage image17 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813062s.tif"));
//		GridCoverage2D coverage17 = gcf.create("coverage", image17, orgEnv17);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813062s.tif", coverage17);
//
//		ReferencedEnvelope orgEnv18 = new ReferencedEnvelope(1048157.93, 1050497.3984, 2008401.7687, 2011313.8545,
//				targetCrs);
//		BufferedImage image18 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813063s.tif"));
//		GridCoverage2D coverage18 = gcf.create("coverage", image18, orgEnv18);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813063s.tif", coverage18);
//
//		ReferencedEnvelope orgEnv19 = new ReferencedEnvelope(1043788.8724, 1046127.3326, 2005603.5856, 2008513.6735,
//				targetCrs);
//		BufferedImage image19 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813071s.tif"));
//		GridCoverage2D coverage19 = gcf.create("coverage", image19, orgEnv19);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813071s.tif", coverage19);
//
//		ReferencedEnvelope orgEnv20 = new ReferencedEnvelope(1045981.9337, 1048321.388, 2005615.7721, 2008525.8586,
//				targetCrs);
//		BufferedImage image20 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813072s.tif"));
//		GridCoverage2D coverage20 = gcf.create("coverage", image20, orgEnv20);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813072s.tif", coverage20);
//
//		ReferencedEnvelope orgEnv21 = new ReferencedEnvelope(1048173.9903, 1050514.4495, 2005627.9638, 2008540.0479,
//				targetCrs);
//		BufferedImage image21 = ImageIO
//				.read(new File("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813073s.tif"));
//		GridCoverage2D coverage21 = gcf.create("coverage", image21, orgEnv21);
//		tifMap.put("D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\east\\38813\\38813073s.tif", coverage21);
//
//		GImageProcessor g = new GImageProcessor();
//		GImageProcessor.ProcessCode procCode = g.autoMosaic(tifMap,
//				"D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\mosa_result.tif", 51);
//
//		ReferencedEnvelope envelope5179 = new ReferencedEnvelope(1039613.05198228, 1048229.13973316, 2008411.37022565,
//				2016172.10786083, targetCrs);
//
//		// 긴급공간정보 생성
//		GTiffDataCropper cropper = new GTiffDataCropper();
//		String cropedPath = "D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\mosa_result_croped.tif";
//		ReferencedEnvelope orgEnv = new ReferencedEnvelope(g.getMinX(), g.getMaxX(), g.getMinY(), g.getMaxY(),
//				g.getCrs());
//		GridCoverage2D cropedGc = cropper.referencingCrop(
//				"D:\\TestData\\01_GeoFraData\\ort\\51cm\\2020\\0017\\mosa_result.tif", orgEnv, envelope5179, cropedPath,
//				targetCrs);
//8
//		// create thumbnail
//		GImageProcessor gImageProcessor = new GImageProcessor();
//		String thumbnailPath = cropedPath.replace(".tif", ".png");
//		gImageProcessor.createThumbnailImage(cropedPath, GTiffDataReader.BIT16ToBIT8.MAX_BIT16, thumbnailPath,
//				GImageProcessor.ImageFormat.IMG_PNG, 1000, GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR);
//
//		// 좌표없는 tif 생성
//		File cropFile = new File(cropedPath);
//		BufferedImage image = ImageIO.read(cropFile);
//		File outputfile = new File(cropedPath.replace(".tif", "_s.tif"));


		
//		ShpDataReader rdaer = new ShpDataReader(shpFile);
		
		
		System.out.println();
	}
}