//package ugis.cmmn;
//
//import java.util.ArrayList;
//
//import org.geotools.coverage.grid.GridEnvelope2D;
//import org.geotools.coverage.grid.GridGeometry2D;
//import org.geotools.geometry.DirectPosition2D;
//import org.geotools.geometry.Envelope2D;
//import org.locationtech.jts.geom.Coordinate;
//import org.opengis.geometry.DirectPosition;
//import org.opengis.referencing.crs.CoordinateReferenceSystem;
//
//import ugis.cmmn.imgproc.GImageEnhancement;
//import ugis.cmmn.imgproc.GImageProcessor;
//import ugis.cmmn.imgproc.GTiffDataReader;
//import ugis.cmmn.imgproc.GTiffDataWriter;
//import ugis.cmmn.imgproc.data.AbsoluteRadiatingCorrectionKompsatInputImpl;
//import ugis.cmmn.imgproc.data.AbsoluteRadiatingCorrectionLandsatInputImpl;
//import ugis.cmmn.imgproc.data.GFileData;
//
//public class TestImgProc {
//
//	public static void main(final String[] args) {
//		// TestByte();
//		// Test_SampleBitPerPixel();
//		// Test_Trans();
//		// Test1();
//
//		// ------------------------------------------------------------//
//
//		// [썸네일 이미지 생성]
//		Test_Thumbnail();
//
//		// [영상 중첩비율]
//		// Test_OverlapRate();
//
//		// ------------------------------------------------------------//
//
//		// [히스토그램 조정]
//		// Test_Histogram(); //히스토그램 정보
//		// Test_Histogram_LinearStretching(); //히스토그램 (Linear Stretching)
//		// Test_Histogram_AutoEqualization(); //히스토그램 (Auto Equalization)
//		// Test_Histogram_AutoLinearStretching(); //히스토그램 (Auto Linear Stretching)
//
//		// [컬러합성]
//		// Test_ColorComposite();
//
//		// [4밴드합성]
//		// Test_Merge4Band();
//
//		// [상대대기보정]
//		// Test_RelativeAtmosphericCorrection();
//
//		// [상대방사보정] : Histogram Matching
//		// Test_RelativeRadiatingCorrection();
//
//		// [가상영상생성] : 시뮬레이터
//		// Test_VirtualImageSimulation();
//
//		// ------------------------------------------------------------//
//
//		// [절대방사보정]
//		// Test_AbsoluteRadiatingCorrection();
//
//		// ------------------------------------------------------------//
//
//		// [모자이크]
//		// Test_AutoMosaic(); //자동 모자이크
//		// Test_SimpleMergedMosaic(); //단순 병합 모자이크
//
//		// ------------------------------------------------------------//
//
//		// RGBA -> RGB 변환
//		// Test_ConvertRGBAToRGB();
//		// Test_ConvertRGBToBGR();
//	}
//
//	private static void Test_Thumbnail() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String filePath = "";
//		String outFilePath = "";
//		// GImageProcessor.ImageFormat outImgFormat =
//		// GImageProcessor.ImageFormat.IMG_PNG;
//		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
//		int thumbnailWidth = 1000;
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR;
//
//		System.out.println("Test_Thumbnail : ");
//		/*
//		 * filePath = "E:\\36807062s.tif"; inMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; resampleMethod =
//		 * GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR; procCode =
//		 * imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath,
//		 * outImgFormat, thumbnailWidth, resampleMethod);
//		 * System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * filePath = "E:\\음영기복도(UTM-K).tif"; inMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; resampleMethod =
//		 * GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR; procCode =
//		 * imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath,
//		 * outImgFormat, thumbnailWidth, resampleMethod);
//		 * System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * filePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1.tif"; inMaxBit16
//		 * = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; resampleMethod =
//		 * GTiffDataReader.ResamplingMethod.BILINEAR; procCode =
//		 * imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath,
//		 * outImgFormat, thumbnailWidth, resampleMethod);
//		 * System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * filePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif"; inMaxBit16
//		 * = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; resampleMethod =
//		 * GTiffDataReader.ResamplingMethod.BILINEAR; procCode =
//		 * imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath,
//		 * outImgFormat, thumbnailWidth, resampleMethod);
//		 * System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * filePath = "E:\\L1C_T52SCE_A032747_20210929T022725.tif"; inMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; resampleMethod =
//		 * GTiffDataReader.ResamplingMethod.CUBIC_CONVOLUTION; procCode =
//		 * imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath,
//		 * outImgFormat, thumbnailWidth, resampleMethod);
//		 * System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * filePath = "E:\\202005403C00370010.tif"; inMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; resampleMethod =
//		 * GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR; procCode =
//		 * imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath,
//		 * outImgFormat, thumbnailWidth, resampleMethod);
//		 * System.out.println("procCode : " + procCode.toString());
//		 */
//
//		filePath = "D:\\TestData\\tmp\\FORESTFIRE\\2111090301\\2111090301\\Existing\\Image\\Satellite\\2022\\Sentinel-2\\CompResult_test.tif";
//		outFilePath = "D:\\TestData\\tmp\\FORESTFIRE\\2111090301\\2111090301\\Existing\\Image\\Satellite\\2022\\Sentinel-2\\CompResult_test.png";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR;
//		procCode = imgProcessor.createThumbnailImage(filePath, inMaxBit16, outFilePath, outImgFormat, thumbnailWidth,
//				resampleMethod);
//		System.out.println("procCode : " + procCode.toString());
//
//		System.out.println("-- Finish!");
//	}
//
//	private static void Test_OverlapRate() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String srcFilePath = "E:\\36807062s.tif";
//		String trgFilePath = "E:\\36807063s.tif";
//
//		System.out.println("Test_OverlapRate : ");
//
//		double overlapRate = imgProcessor.procCalcOverlapRate(srcFilePath, trgFilePath);
//
//		System.out.println("overlapRate : " + overlapRate);
//	}
//
//	private static void Test_Histogram() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		// String filePath = "E:\\음영기복도(UTM-K).tif"; //Gray Image (8bit)
//		// String filePath = "E:\\36807062s.tif"; //Color Image (8bit)
//		// String filePath = "E:\\36807062s_DO.tif"; //Color Image (8bit)
//		String filePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif"; // Gray Image (16bit)
//		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GImageEnhancement imgEnhance = new GImageEnhancement();
//		GTiffDataReader gdReader = null;
//		int[] histgR = new int[256];
//		int[] histgG = new int[256];
//		int[] histgB = new int[256];
//		int[] stretchingMin = new int[3];
//		int[] stretchingMax = new int[3];
//		int i = 0;
//
//		System.out.println("Test_Histogram : ");
//
//		try {
//			gdReader = new GTiffDataReader(filePath, maxBit16);
//			gdReader.getHistogram(histgR, histgG, histgB);
//
//			System.out.println("Histogram : ");
//			System.out.println("\tPixel \tR \tG \tB");
//			for (i = 0; i < 256; i++) {
//				System.out.println("\t" + i + " \t" + histgR[i] + " \t" + histgG[i] + " \t" + histgB[i]);
//			}
//
//			// 자동 Min, Max 검색
//			imgEnhance.calcAutoHistogramMinMax(histgR, histgG, histgB, stretchingMin, stretchingMax);
//			System.out.println(
//					"\t (Geneal) Min : \t" + stretchingMin[0] + " \t" + stretchingMin[1] + " \t" + stretchingMin[2]);
//			System.out.println(
//					"\t (Geneal) Max : \t" + stretchingMax[0] + " \t" + stretchingMax[1] + " \t" + stretchingMax[2]);
//
//			// 1 Sigma 기준 자동 Min, Max 검색
//			imgEnhance.calcAutoHistogramMinMaxByLinearSigma(GImageEnhancement.LinearSigma.LINEAR_1SIGMA, histgR, histgG,
//					histgB, stretchingMin, stretchingMax);
//			System.out.println(
//					"\t (1 Sigma) Min : \t" + stretchingMin[0] + " \t" + stretchingMin[1] + " \t" + stretchingMin[2]);
//			System.out.println(
//					"\t (1 Sigma) Max : \t" + stretchingMax[0] + " \t" + stretchingMax[1] + " \t" + stretchingMax[2]);
//		} catch (Exception ex) {
//			System.out.println("Test_Histogram : " + ex.toString());
//		}
//
//		if (gdReader != null)
//			gdReader.destory();
//		gdReader = null;
//	}
//
//	private static void Test_Histogram_LinearStretching() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inFilePath = "";
//		String outFilePath = "";
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		int[] stretchingMin = new int[3];
//		int[] stretchingMax = new int[3];
//
//		System.out.println("Test_Histogram_LinearStretching : ");
//
//		// New Histogram Range
//		stretchingMin[0] = 50;
//		stretchingMax[0] = 200;
//		stretchingMin[1] = 10;
//		stretchingMax[1] = 225;
//		stretchingMin[2] = 25;
//		stretchingMax[2] = 240;
//
//		// Color Image
//		inFilePath = "E:\\36807062s.tif";
//		outFilePath = "E:\\36807062s_HLS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramLinearStretching(inFilePath, inMaxBit16, outFilePath, stretchingMin,
//				stretchingMax);
//		System.out.println("procCode : " + procCode.toString());
//		/*
//		 * //Gray Image inFilePath = "E:\\36807062s_R.tif"; outFilePath =
//		 * "E:\\36807062s_R_HLS_result.tif"; inMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
//		 * imgProcessor.procHistogramLinearStretching(inFilePath, inMaxBit16,
//		 * outFilePath, stretchingMin, stretchingMax); System.out.println("procCode : "
//		 * + procCode.toString());
//		 * 
//		 * //-------------------------------------------------------------------// //Non
//		 * Histogram Range
//		 * //-------------------------------------------------------------------//
//		 * stretchingMin[0] = 0; stretchingMax[0] = 255; stretchingMin[1] = 0;
//		 * stretchingMax[1] = 255; stretchingMin[2] = 0; stretchingMax[2] = 255; //Gray
//		 * Image(16bit) inFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif"; outFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1_HLS0_result.tif"; inMaxBit16
//		 * = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
//		 * imgProcessor.procHistogramLinearStretching(inFilePath, inMaxBit16,
//		 * outFilePath, stretchingMin, stretchingMax); System.out.println("procCode : "
//		 * + procCode.toString());
//		 * //-------------------------------------------------------------------//
//		 */
//	}
//
//	private static void Test_Histogram_AutoEqualization() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inFilePath = "";
//		String outFilePath = "";
//		boolean isApplyBlueBand = true;
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		System.out.println("Test_Histogram_AutoEqualization : ");
//
//		// Color Image (Blue 밴드 일괄적용)
//		isApplyBlueBand = true;
//		inFilePath = "E:\\36807062s.tif";
//		outFilePath = "E:\\36807062s_HAE_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Color Image (RGB 밴드 개별적용)
//		isApplyBlueBand = false;
//		inFilePath = "E:\\36807062s.tif";
//		outFilePath = "E:\\36807062s_HAE2_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image
//		isApplyBlueBand = true;
//		inFilePath = "E:\\36807062s_R.tif";
//		outFilePath = "E:\\36807062s_R_HAE_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//
//		// ------------------------------------------------------------------//
//
//		// Gray Image(8bit)
//		isApplyBlueBand = true;
//		inFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1.tif";
//		outFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1_HAE_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Color Image (Blue 밴드 일괄적용)
//		isApplyBlueBand = true;
//		inFilePath = "E:\\L1C_T52SCE_A032747_20210929T022725.tif";
//		outFilePath = "E:\\L1C_T52SCE_A032747_20210929T022725_HAE_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Color Image (RGB 밴드 개별적용)
//		isApplyBlueBand = false;
//		inFilePath = "E:\\L1C_T52SCE_A032747_20210929T022725.tif";
//		outFilePath = "E:\\L1C_T52SCE_A032747_20210929T022725_HAE2_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image(16bit)
//		isApplyBlueBand = true;
//		inFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif";
//		outFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1_HAE_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoEqualization(inFilePath, inMaxBit16, outFilePath, isApplyBlueBand);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_Histogram_AutoLinearStretching() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inFilePath = "";
//		String outFilePath = "";
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		System.out.println("Test_Histogram_AutoLinearStretching : ");
//
//		// Color Image (Blue 밴드 일괄적용)
//		inFilePath = "E:\\36807062s.tif";
//		outFilePath = "E:\\36807062s_HALS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoLinearStretching(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image
//		inFilePath = "E:\\36807062s_R.tif";
//		outFilePath = "E:\\36807062s_R_HALS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoLinearStretching(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// ------------------------------------------------------------------//
//
//		// Gray Image(8bit)
//		inFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1.tif";
//		outFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1_HALS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoLinearStretching(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Color Image (Blue 밴드 일괄적용)
//		inFilePath = "E:\\L1C_T52SCE_A032747_20210929T022725.tif";
//		outFilePath = "E:\\L1C_T52SCE_A032747_20210929T022725_HALS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoLinearStretching(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image(16bit)
//		inFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif";
//		outFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1_HALS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procHistogramAutoLinearStretching(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_ColorComposite() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inRedFilePath = "E:\\36807062s_R.tif";
//		String inGreenFilePath = "E:\\36807062s_G.tif";
//		String inBlueFilePath = "E:\\36807062s_B.tif";
//		String outFilePath = "E:\\36807062s_CC_result.tif";
//		GTiffDataReader.BIT16ToBIT8 inRedMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.BIT16ToBIT8 inGreenMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.BIT16ToBIT8 inBlueMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//
//		System.out.println("Test_ColorComposite : ");
//		/*
//		 * inRedFilePath = "E:\\36807062s_R.tif"; inGreenFilePath =
//		 * "E:\\36807062s_G.tif"; inBlueFilePath = "E:\\36807062s_B.tif"; outFilePath =
//		 * "E:\\36807062s_CC_result.tif"; inRedMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; inGreenMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; inBlueMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
//		 * imgProcessor.procColorComposite(inRedFilePath, inRedMaxBit16,
//		 * inGreenFilePath, inGreenMaxBit16, inBlueFilePath, inBlueMaxBit16,
//		 * outFilePath); System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * inRedFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_R.tif";
//		 * inGreenFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_G.tif";
//		 * inBlueFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_B.tif";
//		 * outFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_CC_result.tif";
//		 * inRedMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; inGreenMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; inBlueMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
//		 * imgProcessor.procColorComposite(inRedFilePath, inRedMaxBit16,
//		 * inGreenFilePath, inGreenMaxBit16, inBlueFilePath, inBlueMaxBit16,
//		 * outFilePath); System.out.println("procCode : " + procCode.toString());
//		 */
//		// inRedFilePath = "E:\\K3A_20220302_L1R_R.tif";
//		// inGreenFilePath = "E:\\K3A_20220302_L1R_G.tif";
//		// inBlueFilePath = "E:\\K3A_20220302_L1R_B.tif";
//		// outFilePath = "E:\\K3A_20220302_L1R_CC_result.tif";
//
//		inRedFilePath = "E:\\T52SEG_20220223T020701_B02.tif";
//		inGreenFilePath = "E:\\T52SEG_20220223T020701_B03.tif";
//		inBlueFilePath = "E:\\T52SEG_20220223T020701_B04.tif";
//		outFilePath = "E:\\T52SEG_20220223T020701_CC_result.tif";
//
//		inRedMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		inGreenMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		inBlueMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procColorComposite(inRedFilePath, inRedMaxBit16, inGreenFilePath, inGreenMaxBit16,
//				inBlueFilePath, inBlueMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_Merge4Band() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//		String inRedFilePath = "";
//		String inGreenFilePath = "";
//		String inBlueFilePath = "";
//		String inNirFilePath = "";
//		String outFilePath = "";
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//
//		System.out.println("Test_Merge4Band : ");
//		/*
//		 * //8bit inRedFilePath = "E:\\36807062s_R.tif"; inGreenFilePath =
//		 * "E:\\36807062s_G.tif"; inBlueFilePath = "E:\\36807062s_B.tif"; inNirFilePath
//		 * = "E:\\36807062s_DO.tif"; outFilePath = "E:\\36807062s_M4B_result.tif";
//		 * procCode = imgProcessor.procMerge4Band(inRedFilePath, inGreenFilePath,
//		 * inBlueFilePath, inNirFilePath, outFilePath); System.out.println("procCode : "
//		 * + procCode.toString());
//		 * 
//		 * //8bit inRedFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1.tif";
//		 * inGreenFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B3.tif";
//		 * inBlueFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B5.tif";
//		 * inNirFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B7.tif";
//		 * outFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_M4B_result.tif";
//		 * procCode = imgProcessor.procMerge4Band(inRedFilePath, inGreenFilePath,
//		 * inBlueFilePath, inNirFilePath, outFilePath); System.out.println("procCode : "
//		 * + procCode.toString());
//		 * 
//		 * //16bit inRedFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif"; inGreenFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B4.tif"; inBlueFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B7.tif"; inNirFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B10.tif"; outFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_M4B_result.tif"; procCode =
//		 * imgProcessor.procMerge4Band(inRedFilePath, inGreenFilePath, inBlueFilePath,
//		 * inNirFilePath, outFilePath); System.out.println("procCode : " +
//		 * procCode.toString());
//		 * 
//		 * //16bit inRedFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_R.tif";
//		 * inGreenFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_G.tif";
//		 * inBlueFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_B.tif";
//		 * inNirFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_N.tif";
//		 * outFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_M4B_result.tif";
//		 * procCode = imgProcessor.procMerge4Band(inRedFilePath, inGreenFilePath,
//		 * inBlueFilePath, inNirFilePath, outFilePath); System.out.println("procCode : "
//		 * + procCode.toString());
//		 */
//		// 16bit
//		inRedFilePath = "E:\\K3A_20220302_L1R_R.tif";
//		inGreenFilePath = "E:\\K3A_20220302_L1R_G.tif";
//		inBlueFilePath = "E:\\K3A_20220302_L1R_B.tif";
//		inNirFilePath = "E:\\K3A_20220302_L1R_N.tif";
//		outFilePath = "E:\\K3A_20220302_L1R_M4B_result.tif";
//		procCode = imgProcessor.procMerge4Band(inRedFilePath, inGreenFilePath, inBlueFilePath, inNirFilePath,
//				outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_RelativeAtmosphericCorrection() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inFilePath = "";
//		String outFilePath = "";
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		System.out.println("Test_RelativeAtmosphericCorrection : ");
//
//		// Gray Image(8bit)
//		inFilePath = "E:\\36807062s_DO.tif";
//		outFilePath = "E:\\36807062s_DO_RAC_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procRelativeAtmosphericCorrection(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image(16bit)
//		inFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif";
//		outFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1_RAC_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		procCode = imgProcessor.procRelativeAtmosphericCorrection(inFilePath, inMaxBit16, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_RelativeRadiatingCorrection() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String srcFilePath = "";
//		String trgFilePath = "";
//		String outFilePath = "";
//		boolean isApplyOverlapArea = true;
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 srcMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.BIT16ToBIT8 trgMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		double overlapRate = 0;
//
//		System.out.println("Test_RelativeRadiatingCorrection : ");
//
//		/*
//		 * //기준영상의 중첩영역 Histogram 기준 isApplyOverlapArea = true; srcFilePath =
//		 * "E:\\36807062s.tif"; trgFilePath = "E:\\36807063s.tif"; outFilePath =
//		 * "E:\\36807063s_HM_result.tif"; srcMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		 * 
//		 * if(isApplyOverlapArea) { overlapRate =
//		 * imgProcessor.procCalcOverlapRate(srcFilePath, trgFilePath); if(overlapRate >
//		 * 0) isApplyOverlapArea = true; else isApplyOverlapArea = false; }
//		 * 
//		 * procCode = imgProcessor.procRelativeRadiatingCorrection(srcFilePath,
//		 * srcMaxBit16, trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea);
//		 * System.out.println("[1] isApplyOverlapArea : " + isApplyOverlapArea +
//		 * ", procCode : " + procCode.toString());
//		 * 
//		 * //기준영상의 전체영역 Histogram 기준 isApplyOverlapArea = false; srcFilePath =
//		 * "E:\\36807062s.tif"; trgFilePath = "E:\\36807063s.tif"; outFilePath =
//		 * "E:\\36807063s_HM2_result.tif"; srcMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		 * 
//		 * if(isApplyOverlapArea) { overlapRate =
//		 * imgProcessor.procCalcOverlapRate(srcFilePath, trgFilePath); if(overlapRate >
//		 * 0) isApplyOverlapArea = true; else isApplyOverlapArea = false; }
//		 * 
//		 * procCode = imgProcessor.procRelativeRadiatingCorrection(srcFilePath,
//		 * srcMaxBit16, trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea);
//		 * System.out.println("[2] isApplyOverlapArea : " + isApplyOverlapArea +
//		 * ", procCode : " + procCode.toString());
//		 * 
//		 * //----------------------------------------------------------------------//
//		 * //Gray Image (16bit) //기준영상의 중첩영역 Histogram 기준 isApplyOverlapArea = true;
//		 * srcFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif";
//		 * trgFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B7.tif";
//		 * outFilePath =
//		 * "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B7_HM_result.tif"; srcMaxBit16
//		 * = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
//		 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		 * 
//		 * if(isApplyOverlapArea) { overlapRate =
//		 * imgProcessor.procCalcOverlapRate(srcFilePath, trgFilePath); if(overlapRate >
//		 * 0) isApplyOverlapArea = true; else isApplyOverlapArea = false; }
//		 * 
//		 * procCode = imgProcessor.procRelativeRadiatingCorrection(srcFilePath,
//		 * srcMaxBit16, trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea);
//		 * System.out.println("[3] isApplyOverlapArea : " + isApplyOverlapArea +
//		 * ", procCode : " + procCode.toString());
//		 */
//		// Gray Image (16bit)
//		// 기준영상의 중첩영역 Histogram 기준
//		isApplyOverlapArea = true;
//		srcFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_R.tif";
//		// trgFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_B.tif";
//		// outFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_B_HM_result.tif";
//		trgFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_G.tif";
//		outFilePath = "E:\\K3A_20200426044703_28082_00286238_L1R_G_HM_result.tif";
//		srcMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		trgMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		if (isApplyOverlapArea) {
//			overlapRate = imgProcessor.procCalcOverlapRate(srcFilePath, trgFilePath);
//			if (overlapRate > 0)
//				isApplyOverlapArea = true;
//			else
//				isApplyOverlapArea = false;
//		}
//
//		procCode = imgProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16, trgFilePath, trgMaxBit16,
//				outFilePath, isApplyOverlapArea);
//		System.out.println("[4] isApplyOverlapArea : " + isApplyOverlapArea + ", procCode : " + procCode.toString());
//
//	}
//
//	private static void Test_VirtualImageSimulation() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inFilePath = "";
//		String outFilePath = "";
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		double outRes = 2;
//		int outGridWidth = 6025;
//		int outGridHeight = 4380;
//		double shootAngle = -7.0;
//		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
//
//		System.out.println("Test_VirtualImageSimulation : ");
//
//		// Gray Image(8bit)
//		inFilePath = "E:\\36807062s_R.tif"; // 4671 * 5706
//		outFilePath = "E:\\36807062s_R_VIS_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		// : Blue, Green, Red, NIR 영상의 경우
//		outRes = 2;
//		outGridWidth = 6025;
//		outGridHeight = 4380;
//		shootAngle = 0.0; // -7.0;
//		resampleMethod = GTiffDataReader.ResamplingMethod.CUBIC_CONVOLUTION;
//		procCode = imgProcessor.procVirtualImageSimulation(inFilePath, inMaxBit16, outFilePath, outRes, outGridWidth,
//				outGridHeight, shootAngle, resampleMethod);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image(16bit)
//		inFilePath = "E:\\K3_L1R_R.tif";
//		outFilePath = "E:\\K3_L1R_R_VIS_result.tif"; // 0 ~ 16383
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		// : Blue, Green, Red, NIR 영상의 경우
//		outRes = 2;
//		outGridWidth = 6025;
//		outGridHeight = 4380;
//		shootAngle = 0.0; // -7.0;
//		resampleMethod = GTiffDataReader.ResamplingMethod.CUBIC_CONVOLUTION;
//		procCode = imgProcessor.procVirtualImageSimulation(inFilePath, inMaxBit16, outFilePath, outRes, outGridWidth,
//				outGridHeight, shootAngle, resampleMethod);
//		System.out.println("procCode : " + procCode.toString());
//
//		// Gray Image(16bit)
//		inFilePath = "E:\\K3_L1R_P.tif";
//		outFilePath = "E:\\K3_L1R_P_VIS_result.tif"; // 0 ~ 16383
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		// : Panchromatic 영상의 경우
//		outRes = 0.5;
//		outGridWidth = 24060;
//		outGridHeight = 17520;
//		shootAngle = 0.0; // -7.0;
//		resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
//		procCode = imgProcessor.procVirtualImageSimulation(inFilePath, inMaxBit16, outFilePath, outRes, outGridWidth,
//				outGridHeight, shootAngle, resampleMethod);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_AbsoluteRadiatingCorrection() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		String inFilePath = "";
//		String outTOAReflectanceFilePath = "";
//		String outRadianceFilePath = "";
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		AbsoluteRadiatingCorrectionLandsatInputImpl input = new AbsoluteRadiatingCorrectionLandsatInputImpl();
//		AbsoluteRadiatingCorrectionKompsatInputImpl input_k = new AbsoluteRadiatingCorrectionKompsatInputImpl();
//
//		System.out.println("Test_AbsoluteRadiatingCorrection : ");
//
//		// [Landsat]
//		// Gray Image(8bit)
//		inFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1.tif";
//		outTOAReflectanceFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1_TOAREF_result.tif";
//		outRadianceFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1_RAD_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		input.setReflectanceMultiple(1.2293E-03); // REFLECTANCE BAND 1
//		input.setReflectanceAddtion(-0.011017);
//		input.setRadianceMultiple(7.7874E-01); // RADIANCE BAND 1
//		input.setRadianceAddtion(-6.97874);
//		procCode = imgProcessor.procAbsoluteRadiatingCorrectionForLandsat(inFilePath, inMaxBit16, input,
//				outTOAReflectanceFilePath, outRadianceFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// [Landsat]
//		// Gray Image(16bit)
//		inFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif";
//		outTOAReflectanceFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1_TOAREF_result.tif";
//		outRadianceFilePath = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1_RAD_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		input.setReflectanceMultiple(2.0000E-05); // REFLECTANCE BAND 1
//		input.setReflectanceAddtion(-0.100000);
//		input.setRadianceMultiple(1.2524E-02); // RADIANCE BAND 1
//		input.setRadianceAddtion(-62.62227);
//		procCode = imgProcessor.procAbsoluteRadiatingCorrectionForLandsat(inFilePath, inMaxBit16, input,
//				outTOAReflectanceFilePath, outRadianceFilePath);
//		System.out.println("procCode : " + procCode.toString());
//
//		// [kompsat]
//		// Gray Image(16bit)
//		// inFilePath = "E:\\K3_L1R_B.tif";
//		// outRadianceFilePath = "E:\\K3_L1R_B_RAD_result.tif";
//		inFilePath = "E:\\K3_20200423043412_42327_09361273_L1R_B.tif"; // 절대영역 정보는 없고, 좌표계 정보만 존재!!! -> 좌표계 없는 일반 Tiff로
//																		// 인식됨!!!
//		outRadianceFilePath = "E:\\K3_20200423043412_42327_09361273_L1R_B_RAD_result.tif";
//		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		input_k.setRadianceMultiple(0.0275); // RADIANCE
//		input_k.setRadianceAddtion(-28.4989);
//		procCode = imgProcessor.procAbsoluteRadiatingCorrectionForKompsat(inFilePath, inMaxBit16, input_k,
//				outRadianceFilePath);
//		System.out.println("procCode : " + procCode.toString());
//	}
//
//	private static void Test_AutoMosaic() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		ArrayList<GFileData> inFilePaths = new ArrayList<GFileData>();
//		String outFilePath = "";
//		GFileData gFileData = null;
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		System.out.println("Test_AutoMosaic : ");
//
//		/*
//		 * outFilePath = "E:\\AutoMosaic_result.tif";
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath = "E:\\1_rgb.tif";
//		 * gFileData._maxBit16 = inMaxBit16; inFilePaths.add(gFileData);
//		 * 
//		 * //Reference Image gFileData = new GFileData(); gFileData._strFilePath =
//		 * "E:\\2_rgb.tif"; gFileData._maxBit16 = inMaxBit16; gFileData._isReferenced =
//		 * true; inFilePaths.add(gFileData);
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath = "E:\\3_rgb.tif";
//		 * gFileData._maxBit16 = inMaxBit16; inFilePaths.add(gFileData);
//		 * 
//		 * // gFileData = new GFileData(); // gFileData._strFilePath = "E:\\4_rgb.tif";
//		 * // gFileData._maxBit16 = inMaxBit16; // inFilePaths.add(gFileData);
//		 * 
//		 * procCode = imgProcessor.procAutoMosaic(inFilePaths, outFilePath);
//		 * System.out.println("procCode : " + procCode.toString());
//		 */
//
//		/*
//		 * outFilePath = "E:\\AutoMosaic_36807062s_36807063s_UTM52N_result.tif";
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath =
//		 * "E:\\36807062s_UTM52N.tif"; gFileData._maxBit16 = inMaxBit16;
//		 * inFilePaths.add(gFileData);
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath =
//		 * "E:\\36807063s_UTM52N.tif"; gFileData._maxBit16 = inMaxBit16;
//		 * inFilePaths.add(gFileData);
//		 * 
//		 * procCode = imgProcessor.procAutoMosaic(inFilePaths, outFilePath);
//		 * System.out.println("procCode : " + procCode.toString());
//		 */
//
//		/**/
//		outFilePath = "E:\\AutoMosaic_36807062s_36807063s_result.tif";
//
//		gFileData = new GFileData();
//		gFileData._strFilePath = "E:\\36807062s.tif";
//		gFileData._maxBit16 = inMaxBit16;
//		inFilePaths.add(gFileData);
//
//		gFileData = new GFileData();
//		gFileData._strFilePath = "E:\\36807063s.tif";
//		gFileData._maxBit16 = inMaxBit16;
//		inFilePaths.add(gFileData);
//
//		procCode = imgProcessor.procAutoMosaic(inFilePaths, outFilePath);
//		System.out.println("procCode : " + procCode.toString());
//		/**/
//
//		inFilePaths.clear();
//		inFilePaths = null;
//	}
//
//	private static void Test_SimpleMergedMosaic() {
//		GImageProcessor imgProcessor = new GImageProcessor();
//
//		ArrayList<GFileData> inFilePaths = new ArrayList<GFileData>();
//		String outFilePath = "";
//		GFileData gFileData = null;
//		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
//
//		System.out.println("Test_SimpleMergedMosaic : ");
//		/*
//		 * outFilePath = "E:\\SimpleMergedMosaic_result.tif";
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath = "E:\\1_rgb.tif";
//		 * gFileData._maxBit16 = inMaxBit16; inFilePaths.add(gFileData);
//		 * 
//		 * //Reference Image gFileData = new GFileData(); gFileData._strFilePath =
//		 * "E:\\2_rgb.tif"; gFileData._maxBit16 = inMaxBit16; gFileData._isReferenced =
//		 * true; inFilePaths.add(gFileData);
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath = "E:\\3_rgb.tif";
//		 * gFileData._maxBit16 = inMaxBit16; inFilePaths.add(gFileData);
//		 * 
//		 * // gFileData = new GFileData(); // gFileData._strFilePath = "E:\\4_rgb.tif";
//		 * // gFileData._maxBit16 = inMaxBit16; // inFilePaths.add(gFileData);
//		 * 
//		 * procCode = imgProcessor.procSimpleMergedMosaic(inFilePaths, outFilePath,
//		 * resampleMethod); System.out.println("procCode : " + procCode.toString());
//		 */
//
//		/*
//		 * outFilePath = "E:\\SimpleMergedMosaic_36807062s_36807063s_UTM52N_result.tif";
//		 * 
//		 * gFileData = new GFileData(); gFileData._strFilePath =
//		 * "E:\\36807062s_UTM52N.tif"; gFileData._maxBit16 = inMaxBit16;
//		 * inFilePaths.add(gFileData);
//		 * 
//		 * //Reference Image gFileData = new GFileData(); gFileData._strFilePath =
//		 * "E:\\36807063s_UTM52N.tif"; gFileData._maxBit16 = inMaxBit16;
//		 * gFileData._isReferenced = true; inFilePaths.add(gFileData);
//		 * 
//		 * procCode = imgProcessor.procSimpleMergedMosaic(inFilePaths, outFilePath,
//		 * resampleMethod); System.out.println("procCode : " + procCode.toString());
//		 * 
//		 * inFilePaths.clear(); inFilePaths = null;
//		 */
//
//		/**/
//		outFilePath = "E:\\SimpleMergedMosaic_36807062s_36807063s_result.tif";
//
//		gFileData = new GFileData();
//		gFileData._strFilePath = "E:\\36807062s.tif";
//		gFileData._maxBit16 = inMaxBit16;
//		inFilePaths.add(gFileData);
//
//		// Reference Image
//		gFileData = new GFileData();
//		gFileData._strFilePath = "E:\\36807063s.tif";
//		gFileData._maxBit16 = inMaxBit16;
//		gFileData._isReferenced = true;
//		inFilePaths.add(gFileData);
//
//		procCode = imgProcessor.procSimpleMergedMosaic(inFilePaths, outFilePath, resampleMethod);
//		System.out.println("procCode : " + procCode.toString());
//
//		inFilePaths.clear();
//		inFilePaths = null;
//		/**/
//	}
//
//	private static void Test_ConvertRGBAToRGB() {
//		GTiffDataReader gdReader = null;
//		GTiffDataWriter gdWriter = null;
//		String[] inFilePath = new String[4];
//		String[] outFilePath = new String[4];
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		Envelope2D envelope = null;
//		GridEnvelope2D gridEnvelope = null;
//		Coordinate coordLB = new Coordinate();
//		Coordinate coordRT = new Coordinate();
//		CoordinateReferenceSystem outCrs = null;
//		int width = 0;
//		int height = 0;
//		int bc = 3;
//		int inBC = 3;
//		byte[] inPixels = null;
//		byte[] pixels = null;
//		int i = 0, j = 0, k = 0;
//
//		System.out.println("Test_ConvertRGBAToRGB : ");
//
//		try {
//			gdWriter = new GTiffDataWriter();
//
//			inFilePath[0] = "E:\\1.tif";
//			inFilePath[1] = "E:\\2.tif";
//			inFilePath[2] = "E:\\3.tif";
//			inFilePath[3] = "E:\\4.tif";
//			outFilePath[0] = "E:\\1_rgb.tif";
//			outFilePath[1] = "E:\\2_rgb.tif";
//			outFilePath[2] = "E:\\3_rgb.tif";
//			outFilePath[3] = "E:\\4_rgb.tif";
//
//			for (int m = 0; m < inFilePath.length; m++) {
//
//				System.out.println("\t Processing... : " + inFilePath[m]);
//
//				gdReader = new GTiffDataReader(inFilePath[m], inMaxBit16);
//				envelope = gdReader.getEnvelope();
//				gridEnvelope = gdReader.getGridEnvelope();
//				coordLB.setCoordinate(new Coordinate(envelope.getMinX(), envelope.getMinY()));
//				coordRT.setCoordinate(new Coordinate(envelope.getMaxX(), envelope.getMaxY()));
//				outCrs = gdReader.getCrs();
//				width = (int) gridEnvelope.getWidth();
//				height = (int) gridEnvelope.getHeight();
//				inBC = gdReader.getBandCount();
//				inPixels = gdReader.getAllPixelsByte();
//
//				pixels = new byte[width * height * bc];
//
//				for (j = 0; j < height; j++) {
//					for (i = 0; i < width; i++) {
//						for (k = 0; k < bc; k++) {
//							pixels[(i + j * width) * bc + k] = inPixels[(i + j * width) * inBC + k];
//						}
//					}
//				}
//
//				if (!gdWriter.geoTiffWriter(outFilePath[m], outCrs, coordLB, coordRT, width, height, bc, pixels)) {
//
//				}
//
//				gdReader.destory();
//				gdReader = null;
//
//				System.out.println("\t Write OK : " + outFilePath[m]);
//			}
//
//			gdWriter = null;
//		} catch (Exception ex) {
//			System.out.println("Test_Convert4BandTo3Band : " + ex.toString());
//		}
//	}
//
//	private static void Test_ConvertRGBToBGR() {
//		GTiffDataReader gdReader = null;
//		GTiffDataWriter gdWriter = null;
//		String[] inFilePath = new String[4];
//		String[] outFilePath = new String[4];
//		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		Envelope2D envelope = null;
//		GridEnvelope2D gridEnvelope = null;
//		Coordinate coordLB = new Coordinate();
//		Coordinate coordRT = new Coordinate();
//		CoordinateReferenceSystem outCrs = null;
//		int width = 0;
//		int height = 0;
//		int bc = 3;
//		int inBC = 3;
//		byte[] inPixels = null;
//		byte[] pixels = null;
//		int i = 0, j = 0, k = 0;
//
//		System.out.println("Test_ConvertRGBToBGR : ");
//
//		try {
//			gdWriter = new GTiffDataWriter();
//
//			inFilePath[0] = "E:\\1_rgb.tif";
//			inFilePath[1] = "E:\\2_rgb.tif";
//			inFilePath[2] = "E:\\3_rgb.tif";
//			inFilePath[3] = "E:\\4_rgb.tif";
//			outFilePath[0] = "E:\\1_rgb_revese.tif";
//			outFilePath[1] = "E:\\2_rgb_revese.tif";
//			outFilePath[2] = "E:\\3_rgb_revese.tif";
//			outFilePath[3] = "E:\\4_rgb_revese.tif";
//
//			for (int m = 0; m < inFilePath.length; m++) {
//
//				System.out.println("\t Processing... : " + inFilePath[m]);
//
//				gdReader = new GTiffDataReader(inFilePath[m], inMaxBit16);
//				envelope = gdReader.getEnvelope();
//				gridEnvelope = gdReader.getGridEnvelope();
//				coordLB.setCoordinate(new Coordinate(envelope.getMinX(), envelope.getMinY()));
//				coordRT.setCoordinate(new Coordinate(envelope.getMaxX(), envelope.getMaxY()));
//				outCrs = gdReader.getCrs();
//				width = (int) gridEnvelope.getWidth();
//				height = (int) gridEnvelope.getHeight();
//				inBC = gdReader.getBandCount();
//				inPixels = gdReader.getAllPixelsByte();
//
//				pixels = new byte[width * height * bc];
//
//				for (j = 0; j < height; j++) {
//					for (i = 0; i < width; i++) {
//						for (k = 0; k < bc; k++) {
//							pixels[(i + j * width) * bc + k] = inPixels[(i + j * width) * inBC + (bc - 1 - k)];
//						}
//					}
//				}
//
//				if (!gdWriter.geoTiffWriter(outFilePath[m], outCrs, coordLB, coordRT, width, height, bc, pixels)) {
//
//				}
//
//				gdReader.destory();
//				gdReader = null;
//
//				System.out.println("\t Write OK : " + outFilePath[m]);
//			}
//
//			gdWriter = null;
//		} catch (Exception ex) {
//			System.out.println("Test_ConvertRGBToBGR : " + ex.toString());
//		}
//	}
//
//	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//	private static void TestByte() {
//		byte a = (byte) 255;
//		byte b = (byte) 128;
//		byte c = (byte) 127;
//
//		System.out.println("a : " + a + ", (int) : " + (0xff & a));
//		System.out.println("b : " + b + ", (int) : " + (0xff & b));
//		System.out.println("c : " + c + ", (int) : " + (0xff & c));
//	}
//
//	private static void Test_Trans() {
//		String filePath = "E:\\36807062s.tif";
//		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		GTiffDataReader gdReader = null;
//
//		try {
//			gdReader = new GTiffDataReader(filePath, maxBit16);
//
//			// Base Information
//			Envelope2D envelope = gdReader.getEnvelope();
//			GridEnvelope2D gridEnvelope = gdReader.getGridEnvelope();
//			Coordinate crsCoord = new Coordinate(0, 0);
//			Coordinate imgCoord = new Coordinate(0, 0);
//			DirectPosition crsPos = new DirectPosition2D(0, 0);
//			DirectPosition imgPos = new DirectPosition2D(0, 0);
//
//			System.out.println("Envelope : x = " + envelope.getX() + ", y = " + envelope.getY() + ", w = "
//					+ envelope.getWidth() + ", h = " + envelope.getHeight());
//			System.out.println("\t : minX = " + envelope.getMinX() + ", minY = " + envelope.getMinY() + ", maxX = "
//					+ envelope.getMaxX() + ", maxY = " + envelope.getMaxY());
//			System.out.println("Grid Range : x = " + gridEnvelope.getX() + ", y = " + gridEnvelope.getY() + ", w = "
//					+ gridEnvelope.getWidth() + ", h = " + gridEnvelope.getHeight());
//			System.out.println("\t : minX = " + gridEnvelope.getMinX() + ", minY = " + gridEnvelope.getMinY()
//					+ ", maxX = " + gridEnvelope.getMaxX() + ", maxY = " + gridEnvelope.getMaxY());
//
//			System.out.println("-----------------------------------------------------------------------");
//
//			imgCoord.x = 500;
//			imgCoord.y = 500;
//			crsPos = gdReader.getGridToCRS(imgCoord);
//			System.out.println("\t getGridToCRS : imgCoord - " + imgCoord.x + ", " + imgCoord.y);
//			System.out.println(
//					"\t getGridToCRS : crsPos - " + crsPos.getCoordinate()[0] + ", " + crsPos.getCoordinate()[1]);
//
//			System.out.println("-----------------------------------------------------------------------");
//
//			crsCoord.x = crsPos.getCoordinate()[0];
//			crsCoord.y = crsPos.getCoordinate()[1];
//			imgPos = gdReader.getCRSToGrid(crsCoord);
//			System.out.println("\t getCRSToGrid : crsCoord - " + crsCoord.x + ", " + crsCoord.y);
//			System.out.println(
//					"\t getCRSToGrid : imgPos - " + imgPos.getCoordinate()[0] + ", " + imgPos.getCoordinate()[1]);
//
//			System.out.println("-----------------------------------------------------------------------");
//
//			imgCoord.x = gridEnvelope.getWidth() + 100;
//			imgCoord.y = gridEnvelope.getHeight() + 100;
//			crsPos = gdReader.getGridToCRS(imgCoord);
//			System.out.println("\t getGridToCRS : imgCoord - " + imgCoord.x + ", " + imgCoord.y);
//			System.out.println(
//					"\t getGridToCRS : crsPos - " + crsPos.getCoordinate()[0] + ", " + crsPos.getCoordinate()[1]);
//
//			System.out.println("-----------------------------------------------------------------------");
//
//			crsCoord.x = envelope.getX() + envelope.getWidth() + 100;
//			crsCoord.y = envelope.getY() - envelope.getHeight() - 100;
//			imgPos = gdReader.getCRSToGrid(crsCoord);
//			System.out.println("\t getCRSToGrid : crsCoord - " + crsCoord.x + ", " + crsCoord.y);
//			System.out.println(
//					"\t getCRSToGrid : imgPos - " + imgPos.getCoordinate()[0] + ", " + imgPos.getCoordinate()[1]);
//
//		} catch (Exception ex) {
//			System.out.println("TestGeoTiff : " + ex.toString());
//		}
//
//		if (gdReader != null)
//			gdReader.destory();
//		gdReader = null;
//	}
//
//	private static void Test_SampleBitPerPixel() {
//		String testFilePath = "E:\\LE07_L1TP_115037_20210518_20210613_01_T2_B1.tif";
//		String testFilePath2 = "E:\\LC08_L1TP_115037_20211001_20211001_01_RT_B1.tif";
//		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.BIT16ToBIT8 maxBit16_2 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//
//		GTiffDataReader gdReader = null;
//		GTiffDataReader gdReader2 = null;
//
//		try {
//			gdReader = new GTiffDataReader(testFilePath, maxBit16);
//			gdReader2 = new GTiffDataReader(testFilePath2, maxBit16_2);
//
//			System.out.println("\t " + testFilePath + " : BitPerSample = " + gdReader.getBitPerSample());
//			System.out.println("\t " + testFilePath2 + " : BitPerSample = " + gdReader2.getBitPerSample());
//
//			System.out.println("\t " + testFilePath + " : BytePerSample = " + gdReader.getBytePerSample());
//			System.out.println("\t " + testFilePath2 + " : BytePerSample = " + gdReader2.getBytePerSample());
//
//			System.out.println("\t " + testFilePath + " : SamplePerPixel = " + gdReader.getBandCount());
//			System.out.println("\t " + testFilePath2 + " : SamplePerPixel = " + gdReader2.getBandCount());
//
//			System.out.println("\t " + testFilePath + " : BytePerPixel = " + gdReader.getBytePerPixel());
//			System.out.println("\t " + testFilePath2 + " : BytePerPixel = " + gdReader2.getBytePerPixel());
//
//		} catch (Exception ex) {
//			System.out.println("Test_SampleBitPerPixel : " + ex.toString());
//		}
//
//		if (gdReader != null)
//			gdReader.destory();
//		gdReader = null;
//		if (gdReader2 != null)
//			gdReader2.destory();
//		gdReader2 = null;
//	}
//
//	private static void Test1() {
//		double percent = 0;
//		String testFilePath = "E:\\DEM270M.tif";
//		String testFilePath2 = "E:\\음영기복도(UTM-K).tif";
//		String testFilePath3 = "E:\\36807062s.tif";
//		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.BIT16ToBIT8 maxBit16_2 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		GTiffDataReader.BIT16ToBIT8 maxBit16_3 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
//		String destFilePath = "E:\\Out.tif";
//		int pixelShift = 50;
//
//		try {
//			// TestGeoTiff(testFilePath, maxBit16);
//			// TestGeoTiff(testFilePath2, maxBit16_2);
//			TestGeoTiff(testFilePath3, maxBit16_3);
//
//			// TestGeoTiff2(testFilePath2, maxBit16_2, destFilePath, pixelShift);
//			// TestGeoTiff2(testFilePath3, maxBit16_3, destFilePath, pixelShift);
//		} catch (Exception ex) {
//			System.out.println("main : " + ex.toString());
//		}
//
//		System.out.println("progress rate : " + percent + "%");
//	}
//
//	private static void TestGeoTiff(String filePath, GTiffDataReader.BIT16ToBIT8 maxBit16) throws Exception {
//		GTiffDataReader gdReader = null;
//
//		try {
//			gdReader = new GTiffDataReader(filePath, maxBit16);
//
//			// Base Information
//			int bc = gdReader.getBandCount();
//			GridGeometry2D gridGoem = gdReader.getGridGeom();
//			Envelope2D envelope = gdReader.getEnvelope();
//			GridEnvelope2D gridEnvelope = gdReader.getGridEnvelope();
//			double resX = 0, resY = 0;
//			int col = 0, row = 0;
//			double value = -1;
//			byte valueByte = 0; // !!! Java의 Byte는 부호가 존재함 !!! (-128 ~ 127)
//
//			resX = envelope.getWidth() / gridEnvelope.getWidth();
//			resY = envelope.getHeight() / gridEnvelope.getHeight();
//
//			System.out.println("Band Count : " + bc);
//			System.out.println("Envelope : x = " + envelope.getX() + ", y = " + envelope.getY() + ", w = "
//					+ envelope.getWidth() + ", h = " + envelope.getHeight());
//			System.out.println("Grid Range : x = " + gridEnvelope.getX() + ", y = " + gridEnvelope.getY() + ", w = "
//					+ gridEnvelope.getWidth() + ", h = " + gridEnvelope.getHeight());
//
//			System.out.println("Resolution : resX = " + resX + ", resY = " + resY);
//
//			// Pixel Information
//			// ---------------------------------------------------------------------------//
//			// Coordinate coord = new Coordinate(1072479, 1762501); //음영기복도(UTM-K).tif
//			// Coordinate coord = new Coordinate(621050.2766372026, 1419999.509478781);
//			// ---------------------------------------------------------------------------//
//			Coordinate coord = new Coordinate(337498.6389, 444255.1681); // 36807062s.tif
//
//			if (!gdReader.IsAbsoluteCoordinateSystem()) {
//				double orgX = 336405.255;
//				double orgY = 445791.745;
//				double orgW = 2382.210000000021;
//				double orgH = 2910.0599999999977;
//				double orgResX = 0.51;
//				double orgResY = 0.51;
//
//				coord.x = (coord.x - orgX) / orgResX;
//				coord.y = (coord.y - orgY) / orgResY;
//
//				coord.x += orgResX / 2.0;
//				coord.y -= orgResY / 2.0;
//			}
//
//			System.out.println("---------------------------------------");
//			System.out.println("coord : x = " + coord.x + ", y = " + coord.y);
//
//			System.out.println("---------------------------------------");
//			value = gdReader.getValueByCoordDouble(coord, GTiffDataReader.RGBBand.RED_BAND);
//			System.out.println("Value By Coord [0] : " + value);
//			value = gdReader.getValueByCoordDouble(coord, GTiffDataReader.RGBBand.GREEN_BAND);
//			System.out.println("Value By Coord [1] : " + value);
//			value = gdReader.getValueByCoordDouble(coord, GTiffDataReader.RGBBand.BLUE_BAND);
//			System.out.println("Value By Coord [2] : " + value);
//
//			col = (int) ((coord.x - envelope.getX()) / resX);
//			row = (int) (gridEnvelope.getHeight() - 1) - (int) ((coord.y - envelope.getY()) / resY);
//
//			System.out.println("---------------------------------------");
//			System.out.println("Grid : x = " + col + ", y = " + row);
//
//			System.out.println("---------------------------------------");
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.RED_BAND);
//			System.out.println("Value By Pixel [0] : " + value);
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.GREEN_BAND);
//			System.out.println("Value By Pixel [1] : " + value);
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.BLUE_BAND);
//			System.out.println("Value By Pixel [2] : " + value);
//
//			System.out.println("---------------------------------------");
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.RED_BAND);
//			System.out.println("Value By Pixel [0] - Int : " + (int) value);
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.GREEN_BAND);
//			System.out.println("Value By Pixel [1] - Int : " + (int) value);
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.BLUE_BAND);
//			System.out.println("Value By Pixel [2] - Int : " + (int) value);
//
//			System.out.println("---------------------------------------");
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.RED_BAND);
//			valueByte = (byte) value;
//			System.out.println("Value By Pixel [0] - Byte : " + valueByte);
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.GREEN_BAND);
//			valueByte = (byte) value;
//			System.out.println("Value By Pixel [1] - Byte : " + valueByte);
//			value = gdReader.getValueByPixelDouble(col, row, GTiffDataReader.RGBBand.BLUE_BAND);
//			valueByte = (byte) value;
//			System.out.println("Value By Pixel [2] - Byte : " + valueByte);
//		} catch (Exception ex) {
//			System.out.println("TestGeoTiff : " + ex.toString());
//		}
//
//		if (gdReader != null)
//			gdReader.destory();
//		gdReader = null;
//	}
//
//	private static void TestGeoTiff2(String inputFilePath, GTiffDataReader.BIT16ToBIT8 inMaxBit16,
//			String outputFilePath, int pixelShift) throws Exception {
//		CoordinateReferenceSystem outCrs = null;
//		Envelope2D outEnvelope = null;
//		Coordinate coordLB = null;
//		Coordinate coordRT = null;
//		int width = 0, height = 0;
//		int bc = 0;
//		GTiffDataReader gdReader = null;
//		GTiffDataWriter gdWriter = new GTiffDataWriter();
//
//		try {
//			gdReader = new GTiffDataReader(inputFilePath, inMaxBit16);
//
//			// Base Information
//			GridGeometry2D gridGoem = gdReader.getGridGeom();
//			Envelope2D envelope = gdReader.getEnvelope();
//			GridEnvelope2D gridEnvelope = gdReader.getGridEnvelope();
//
//			System.out.println("Band Count : " + gdReader.getBandCount());
//			System.out.println("Envelope : x = " + envelope.getX() + ", y = " + envelope.getY() + ", w = "
//					+ envelope.getWidth() + ", h = " + envelope.getHeight());
//			System.out.println("Grid Range : x = " + gridEnvelope.getX() + ", y = " + gridEnvelope.getY() + ", w = "
//					+ gridEnvelope.getWidth() + ", h = " + gridEnvelope.getHeight());
//
//			// Write GeoTiff
//			byte[] pixels = gdReader.getAllPixelsByte();
//			// 1. Coordinate System
//			// outCrs = CRS.decode("EPSG:5179");
//			// outCrs = DefaultGeographicCRS.WGS84;
//			outCrs = gdReader.getCrs();
//
//			// 2. Boundary
//			coordLB = new Coordinate(envelope.getMinX(), envelope.getMinY());
//			coordRT = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
//
//			// 3. Grid Size
//			width = (int) gridEnvelope.getWidth();
//			height = (int) gridEnvelope.getHeight();
//			bc = gdReader.getBandCount();
//
//			// 4. Process
//			if (pixelShift > 0) {
//				for (int j = 0; j < height; j++) {
//					for (int i = 0; i < width; i++) {
//						for (int k = 0; k < bc; k++) {
//							// !!! Java의 Byte는 부호가 존재함 !!! (-128 ~ 127)
//							if ((0xff & pixels[(i + j * width) * bc + k]) <= pixelShift)
//								pixels[(i + j * width) * bc + k] = (byte) 0;
//							else if (255 < (0xff & pixels[(i + j * width) * bc + k]) + pixelShift)
//								pixels[(i + j * width) * bc + k] = (byte) 255;
//							else
//								pixels[(i + j * width) * bc
//										+ k] = (byte) ((0xff & pixels[(i + j * width) * bc + k]) + pixelShift);
//						}
//					}
//				}
//			}
//
//			gdWriter.geoTiffWriter(outputFilePath, outCrs, coordLB, coordRT, width, height, bc, pixels);
//		} catch (Exception ex) {
//			System.out.println("TestGeoTiff2 : " + ex.toString());
//		}
//
//		if (gdReader != null)
//			gdReader.destory();
//		gdReader = null;
//	}
//
//}
