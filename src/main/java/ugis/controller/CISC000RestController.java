package ugis.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.omg.CORBA.IMP_LIMIT;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.data.AbsoluteRadiatingCorrectionKompsatInputImpl;
import ugis.cmmn.imgproc.data.AbsoluteRadiatingCorrectionLandsatInputImpl;
import ugis.cmmn.imgproc.data.GFileData;
import ugis.service.CISC001Service;
import ugis.service.CMSC003Service;
import ugis.service.vo.CISC001ProjectLogVO;
import ugis.service.vo.CISC001WorkResultVO;
import ugis.service.vo.CISC004VO;
import ugis.service.vo.CMSC003DataSetVO;

// 대기보정, 방사보정, 영상처리 기능 controller
// 결과 TN_USGS_WORK 테이블 저장
@RestController
public class CISC000RestController {

	@Resource(name = "cisc001Service")
	CISC001Service cisc001Service;

	@Resource(name = "cmsc003Service")
	CMSC003Service cmsc003Service;

	@Resource(name = "fileProperties")
	private Properties fileProperties;
	GImageProcessor imgProcessor = new GImageProcessor();

	// 대기보정
	// WORK_KIND 1
	@RequestMapping(value = "cisc004/AtmosphereResult.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String Result(@ModelAttribute("paramVO") CISC004VO cisc004VO, HttpServletRequest request, ModelMap model)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		String input_vido = (String) cisc004VO.getInnerFileCoursNm();
		String algorithm = (String) cisc004VO.getAlgorithm();
		String outputNm = (String) cisc004VO.getOutputpath();
		String outputDir = new File(input_vido).getParent();
		String outputPath = outputDir + "/" + outputNm + ".tif";

		// 파일 이름 중복 시
		File outputFile = new File(outputPath);
		if (outputFile.exists()) {
			String nm = outputNm;
			File upFile = outputFile.getParentFile();
			File[] files = upFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(nm) && !name.endsWith(".tif");
				}
			});
			if (files != null) {
				int length = files.length;
				outputNm += "(" + length + ")";
			}
			outputPath = outputDir + "/" + outputNm + ".tif";
		}

		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GImageProcessor gImageProcessor = new GImageProcessor();

		procCode = gImageProcessor.procRelativeAtmosphericCorrection(input_vido, inMaxBit16, outputPath);
		System.out.println("procCode : " + procCode.toString());

		String outThumbPath = outputPath.replace(".tif", ".png");
		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		int thumbnailWidth = 1000;
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
		if ("SUCCESS".equals(procCode.name())) {
			thumprocCode = gImageProcessor.createThumbnailImage(outputPath, inMaxBit16, outThumbPath, outImgFormat,
					thumbnailWidth, resampleMethod);
			// int lastIdx = outputNm.lastIndexOf(".");
			// outputName = outputNm.substring(0, lastIdx);
		}
		jsonMap.put("procCode", procCode);
		jsonMap.put("thumprocCode", thumprocCode);
		jsonMap.put("fileName", outputPath);
		jsonMap.put("filePath", outputPath);

		String disasterId = cisc004VO.getDisasterId();
		// 처리내역 저장
		CISC001WorkResultVO workVO = new CISC001WorkResultVO();
		workVO.setDisasterId(disasterId);

		EgovMap usgs = cmsc003Service.selectUsgsByInFileCours(input_vido); // 원본
		if (usgs == null) {
			usgs = cmsc003Service.selectUsgsWorkByInFileCours(input_vido); // 처리
		}
		if (usgs != null) {
			workVO.setVidoId((Integer) usgs.get("vidoId"));
			workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
			workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
			workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
			workVO.setVidoNm(outputNm);
			workVO.setInnerFileCoursNm(outputPath);
			workVO.setLtopCrdntX(((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			workVO.setLtopCrdntY(((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			workVO.setRbtmCrdntX(((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			workVO.setRbtmCrdntY(((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			workVO.setWorkKind("1"); // 대기보정
			cisc001Service.insertWorkResult2(workVO);

			jsonMap.put("ltopCrdntX", ((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			jsonMap.put("ltopCrdntY", ((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			jsonMap.put("rbtmCrdntX", ((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			jsonMap.put("rbtmCrdntY", ((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
		} else {
			// 긴급
			EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(input_vido); // 원본
			if (dataset != null) {
				// inert dataset info
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
				dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
				dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
				dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
				dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
				dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
				dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
				dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
				dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));
				dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
				dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
				dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
				dataSetVo.setRoiYn("N");
				dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
				dataSetVo.setDataCd((String) dataset.get("dataCd"));
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr((String) dataset.get("addr"));
				dataSetVo.setFileNm(outputNm);
				dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
				dataSetVo.setFileTy((String) dataset.get("fileTy"));
				dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
				dataSetVo.setYear((String) dataset.get("year"));
				dataSetVo.setDpi((String) dataset.get("dpi"));
				dataSetVo.setFullFileCoursNm(outputPath);
				Date dateBegin = (Date) dataset.get("potogrfBeginDt");
				Date dateEnd = (Date) dataset.get("potogrfEndDt");
				dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
				dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
				dataSetVo.setWorkKind("1");
				dataSetVo.setSatDir((String) dataset.get("satDir"));
				dataSetVo.setMapNm((String) dataset.get("mapNm"));
				cmsc003Service.insertDatasetInfo(dataSetVo);

				// 좌표없는 tif 생성
				File cropFile = new File(outputPath);
				BufferedImage image = ImageIO.read(cropFile);
				File outputfile = new File(outputPath.replace(".tif", "_s.tif"));
				ImageIO.write(image, "tif", outputfile);

				jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
				jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
				jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
				jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());
				jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
			}
		}

		// 프로젝트 key가 있으면 로그 기록
		if (request.getSession().getAttribute("projectKey") != null
				&& !request.getSession().getAttribute("projectKey").equals("")) {
			CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
			logVO.setProjectId((String) request.getSession().getAttribute("projectKey")); // 프로젝트 ID
			logVO.setWorkKind("1"); // 상대대기보정
			logVO.setAlgorithmNm(algorithm);
			logVO.setInputFileNm(input_vido);
			logVO.setTargetFileNm("");
			logVO.setOutputFileNm(outputPath);
			cisc001Service.insertProjectLog(logVO);
		}
		return mapper.writeValueAsString(jsonMap);
	}

	// 절대방사보정
	// WORK_KIND 2
	@RequestMapping(value = "cisc005/absoluteRadiatingCorrection.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String absoluteRadiatingCorrection(@RequestParam HashMap<String, Object> params, HttpServletRequest request,
			ModelMap model) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		GImageProcessor imgProcessor = new GImageProcessor();

		String outTOAReflectanceFileNm = "";
		String outTOAReflectanceFilePath = "";
		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		AbsoluteRadiatingCorrectionLandsatInputImpl input = new AbsoluteRadiatingCorrectionLandsatInputImpl();
		AbsoluteRadiatingCorrectionKompsatInputImpl input_k = new AbsoluteRadiatingCorrectionKompsatInputImpl();

		String satKind = (String) params.get("satKind");
		String inFilePath = (String) params.get("inFilePath");
		String outRadianceFileNm = (String) params.get("outRadianceFilePath");
		String outputDir = new File(inFilePath).getParent();
		String outRadianceFilePath = outputDir + "/" + outRadianceFileNm + ".tif";

		// 파일 이름 중복 시
		File outputFile = new File(outRadianceFilePath);
		if (outputFile.exists()) {
			String nm = outTOAReflectanceFileNm;
			File upFile = outputFile.getParentFile();
			File[] files = upFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(nm) && !name.endsWith(".tif");
				}
			});
			if (files != null) {
				int length = files.length;
				outTOAReflectanceFileNm += "(" + length + ")";
			}
			outRadianceFilePath = outputDir + "/" + outTOAReflectanceFileNm + ".tif";
		}

		if (satKind.equals("1")) { // Landsat
			// [Landsat]
			// Gray Image(16bit)
			outTOAReflectanceFileNm = (String) params.get("TOAReflectanceFilePath");
			outTOAReflectanceFilePath = outputDir + "/" + outTOAReflectanceFileNm + ".tif";
			String outTOANm = outTOAReflectanceFileNm;

			// 파일 이름 중복 시
			File outTOAFile = new File(outTOAReflectanceFilePath);
			if (outTOAFile.exists()) {
				File upFile = outTOAFile.getParentFile();
				File[] files = upFile.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.startsWith(outTOANm) && !name.endsWith(".tif");
					}
				});
				if (files != null) {
					int length = files.length;
					outTOAReflectanceFileNm += "(" + length + ")";
				}
				outTOAReflectanceFilePath = outputDir + "/" + outTOAReflectanceFileNm + ".tif";
			}

			inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			input.setReflectanceMultiple(Double.parseDouble((String) params.get("ReflectanceMultiple"))); // REFLECTANCE
																											// BAND 1
			input.setReflectanceAddtion(Double.parseDouble((String) params.get("ReflectanceAddtion")));
			input.setRadianceMultiple(Double.parseDouble((String) params.get("RadianceMultiple"))); // RADIANCE BAND 1
			input.setRadianceAddtion(Double.parseDouble((String) params.get("RadianceAddtion")));

			procCode = imgProcessor.procAbsoluteRadiatingCorrectionForLandsat(inFilePath, inMaxBit16, input,
					outTOAReflectanceFilePath, outRadianceFilePath);
			System.out.println("procCode : " + procCode);
			jsonMap.put("procCode", procCode);

		} else { // Kompsat
			// [kompsat]
			// Gray Image(16bit)
			inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			input_k.setRadianceMultiple(Double.parseDouble((String) params.get("gain")));
			input_k.setRadianceAddtion(Double.parseDouble((String) params.get("offset")));
			procCode = imgProcessor.procAbsoluteRadiatingCorrectionForKompsat(inFilePath, inMaxBit16, input_k,
					outRadianceFilePath);
			System.out.println("procCode : " + procCode);
			jsonMap.put("procCode", procCode);
		}

		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR;

		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		int thumbnailWidth = 1000;
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

		String outRadianceThumbPath = outRadianceFilePath.replace("(?i).tif", ".png");
		thumprocCode = imgProcessor.createThumbnailImage(outRadianceFilePath, inMaxBit16, outRadianceThumbPath,
				outImgFormat, thumbnailWidth, resampleMethod);
		jsonMap.put("thumprocCode", thumprocCode);
		jsonMap.put("resultFileName", outRadianceFilePath);
		jsonMap.put("thumFullName", outRadianceThumbPath);

		if (request.getSession().getAttribute("projectKey") != null
				&& !request.getSession().getAttribute("projectKey").equals("")) {
			CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
			logVO.setProjectId((String) request.getSession().getAttribute("projectKey")); // 프로젝트 ID
			logVO.setWorkKind("2"); // 절대방사보정
			logVO.setMetaDataNm((String) params.get("metaData"));
			logVO.setInputFileNm(inFilePath);
			logVO.setOutputFileNm(outRadianceFileNm);
			logVO.setAutoAreaControl("");
			logVO.setControlType("");
			logVO.setRadiatingFormula((String) params.get("radiatingFormula"));
			logVO.setToaFormula((String) params.get("toaFormula"));
			logVO.setToaOutputFileNm((String) params.get("TOAReflectanceFilePath"));
			cisc001Service.insertProjectLog(logVO);
		}

		String disasterId = (String) params.get("disasterId"); // 재난 ID
		CISC001WorkResultVO workVO = new CISC001WorkResultVO();
		workVO.setDisasterId(disasterId);
		workVO.setWorkKind("2"); // 절대방사보정
		EgovMap usgs = cmsc003Service.selectUsgsByInFileCours(inFilePath); // 원본
		if (usgs == null) {
			usgs = cmsc003Service.selectUsgsWorkByInFileCours(inFilePath); // 처리
		}
		if (usgs != null) {
			workVO.setVidoId((Integer) usgs.get("vidoId"));
			workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
			workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
			workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
			workVO.setVidoNm(outRadianceFileNm);
			workVO.setInnerFileCoursNm(outRadianceFilePath);
			workVO.setLtopCrdntX(((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			workVO.setLtopCrdntY(((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			workVO.setRbtmCrdntX(((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			workVO.setRbtmCrdntY(((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			cisc001Service.insertWorkResult2(workVO);

			jsonMap.put("ltopCrdntX", ((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			jsonMap.put("ltopCrdntY", ((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			jsonMap.put("rbtmCrdntX", ((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			jsonMap.put("rbtmCrdntY", ((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
		} else {
			// 긴급
			EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(inFilePath); // 원본
			if (dataset != null) {
				// inert dataset info
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
				dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
				dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
				dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
				dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
				dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
				dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
				dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
				dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));
				dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
				dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
				dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
				dataSetVo.setRoiYn("N");
				dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
				dataSetVo.setDataCd((String) dataset.get("dataCd"));
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr((String) dataset.get("addr"));
				dataSetVo.setFileNm(outRadianceFileNm);
				dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
				dataSetVo.setFileTy((String) dataset.get("fileTy"));
				dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
				dataSetVo.setYear((String) dataset.get("year"));
				dataSetVo.setDpi((String) dataset.get("dpi"));
				dataSetVo.setFullFileCoursNm(outRadianceFilePath);
				Date dateBegin = (Date) dataset.get("potogrfBeginDt");
				Date dateEnd = (Date) dataset.get("potogrfEndDt");
				dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
				dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
				dataSetVo.setWorkKind("2");
				dataSetVo.setSatDir((String) dataset.get("satDir"));
				dataSetVo.setMapNm((String) dataset.get("mapNm"));
				cmsc003Service.insertDatasetInfo(dataSetVo);

				// 좌표없는 tif 생성
				File cropFile = new File(outRadianceFilePath);
				BufferedImage image = ImageIO.read(cropFile);
				File outputfile = new File(outRadianceFilePath.replace(".tif", "_s.tif"));
				ImageIO.write(image, "tif", outputfile);

				jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
				jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
				jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
				jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());
				jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
			}
		}

		if (satKind.equals("1")) { // Landsat
			String toaThumFilePath = outTOAReflectanceFilePath.replaceAll("(?i).tif", ".png");
			thumbnailWidth = 1000;
			outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
			inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			thumprocCode = imgProcessor.createThumbnailImage(outTOAReflectanceFilePath, inMaxBit16, toaThumFilePath,
					outImgFormat, thumbnailWidth, resampleMethod);

			jsonMap.put("resultToaFileName", outTOAReflectanceFilePath);
			jsonMap.put("thumToaFullName", toaThumFilePath);

			workVO = new CISC001WorkResultVO();
			workVO.setInnerFileCoursNm(outTOAReflectanceFilePath);
			workVO.setWorkKind("2"); // 절대방사보정

			if (usgs == null) {
				usgs = cmsc003Service.selectUsgsWorkByInFileCours(inFilePath); // 처리
			}
			if (usgs != null) {
				workVO.setVidoId((Integer) usgs.get("vidoId"));
				workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
				workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
				workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
				workVO.setVidoNm(outTOAReflectanceFileNm);
				workVO.setInnerFileCoursNm(outTOAReflectanceFilePath);
				workVO.setLtopCrdntX(((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
				workVO.setLtopCrdntY(((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
				workVO.setRbtmCrdntX(((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
				workVO.setRbtmCrdntY(((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
				cisc001Service.insertWorkResult2(workVO);

				jsonMap.put("ltopCrdntX", ((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
				jsonMap.put("ltopCrdntY", ((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
				jsonMap.put("rbtmCrdntX", ((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
				jsonMap.put("rbtmCrdntY", ((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
				jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
			} else {
				// 긴급
				EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(inFilePath); // 원본
				if (dataset != null) {
					// inert dataset info
					CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
					dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
					dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
					dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
					dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
					dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
					dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
					dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
					dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
					dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));
					dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
					dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
					dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
					dataSetVo.setRoiYn("N");
					dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
					dataSetVo.setDataCd((String) dataset.get("dataCd"));
					dataSetVo.setMsfrtnId(disasterId);
					dataSetVo.setAddr((String) dataset.get("addr"));
					dataSetVo.setFileNm(outTOAReflectanceFileNm);
					dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
					dataSetVo.setFileTy((String) dataset.get("fileTy"));
					dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
					dataSetVo.setYear((String) dataset.get("year"));
					dataSetVo.setDpi((String) dataset.get("dpi"));
					dataSetVo.setFullFileCoursNm(outTOAReflectanceFilePath);
					Date dateBegin = (Date) dataset.get("potogrfBeginDt");
					Date dateEnd = (Date) dataset.get("potogrfEndDt");
					dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
					dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
					dataSetVo.setWorkKind("2");
					dataSetVo.setSatDir((String) dataset.get("satDir"));
					dataSetVo.setMapNm((String) dataset.get("mapNm"));
					cmsc003Service.insertDatasetInfo(dataSetVo);

					// 좌표없는 tif 생성
					File cropFile = new File(outTOAReflectanceFilePath);
					BufferedImage image = ImageIO.read(cropFile);
					File outputfile = new File(outTOAReflectanceFilePath.replace(".tif", "_s.tif"));
					ImageIO.write(image, "tif", outputfile);

					jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
					jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
					jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
					jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());
					jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
				}
			}
		}

		return mapper.writeValueAsString(jsonMap);
	}

	// 상대방사보정
	// WORK_KIND 3
	@RequestMapping(value = "cisc005/relativeRadiatingCorrection.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String relativeRadiatingCorrection(@RequestParam HashMap<String, Object> params, HttpServletRequest request,
			ModelMap model) throws Exception {

		String histogramArea = (String) params.get("histogramArea");
		String algorithm = (String) params.get("algorithm");
		String srcFilePath = (String) params.get("srcFilePath");
		String trgFilePath = (String) params.get("trgFilePath");
		String outFileNm = (String) params.get("outFilePath");

		// 처리 로그 기록
		if (request.getSession().getAttribute("projectKey") != null
				&& !request.getSession().getAttribute("projectKey").equals("")) {
			CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
			logVO.setProjectId((String) request.getSession().getAttribute("projectKey")); // 프로젝트 ID
			logVO.setWorkKind("3"); // 상대방사보정
			logVO.setAlgorithmNm(algorithm);
			logVO.setInputFileNm(srcFilePath);
			logVO.setTargetFileNm(trgFilePath);
			logVO.setOutputFileNm(outFileNm);
			cisc001Service.insertProjectLog(logVO);
		}

		String outputDir = new File(srcFilePath).getParent();
		String outFilePath = outputDir + "/" + outFileNm + ".tif";

		// 파일 이름 중복 시
		File outputFile = new File(outFilePath);
		if (outputFile.exists()) {
			String nm = outFileNm;
			File upFile = outputFile.getParentFile();
			File[] files = upFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(nm) && !name.endsWith(".tif");
				}
			});
			if (files != null) {
				int length = files.length;
				outFileNm += "(" + length + ")";
			}
			outFilePath = outputDir + "/" + outFileNm + ".tif";
		}

		GImageProcessor imgProcessor = new GImageProcessor();
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();

		boolean isApplyOverlapArea = true;
		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		GTiffDataReader.BIT16ToBIT8 srcMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader.BIT16ToBIT8 trgMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

		if (histogramArea.equals("1")) { // 전체영역
			isApplyOverlapArea = false;
			srcMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			trgMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			procCode = imgProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16, trgFilePath, trgMaxBit16,
					outFilePath, isApplyOverlapArea);
		} else { // 중첩영역
			isApplyOverlapArea = true;
			srcMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			trgMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
			procCode = imgProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16, trgFilePath, trgMaxBit16,
					outFilePath, isApplyOverlapArea);
		}
		jsonMap.put("procCode", procCode);

		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR;
		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;

		String outThumFilePath = outFilePath.replaceAll("(?i).tif", ".png");

		int thumbnailWidth = 1000;
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		thumprocCode = imgProcessor.createThumbnailImage(outFilePath, inMaxBit16, outThumFilePath, outImgFormat,
				thumbnailWidth, resampleMethod);

		jsonMap.put("thumprocCode", thumprocCode);
		jsonMap.put("resultFileName", outFilePath);
		jsonMap.put("thumFullName", outThumFilePath);

		String disasterId = (String) params.get("disasterId");
		EgovMap usgs = cmsc003Service.selectUsgsByInFileCours(trgFilePath); // 원본
		if (usgs == null) {
			usgs = cmsc003Service.selectUsgsWorkByInFileCours(trgFilePath); // 처리
		}
		if (usgs != null) {
			CISC001WorkResultVO workVO = new CISC001WorkResultVO();
			workVO.setWorkKind("3"); // 상대방사보정
			workVO.setVidoId((Integer) usgs.get("vidoId"));
			workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
			workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
			workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
			workVO.setVidoNm(outFileNm);
			workVO.setInnerFileCoursNm(outFilePath);
			workVO.setLtopCrdntX(((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			workVO.setLtopCrdntY(((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			workVO.setRbtmCrdntX(((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			workVO.setRbtmCrdntY(((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			workVO.setDisasterId(disasterId);
			cisc001Service.insertWorkResult2(workVO);

			jsonMap.put("ltopCrdntX", ((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			jsonMap.put("ltopCrdntY", ((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			jsonMap.put("rbtmCrdntX", ((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			jsonMap.put("rbtmCrdntY", ((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
		} else {
			// 긴급
			EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(trgFilePath); // 원본
			if (dataset != null) {
				// inert dataset info
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
				dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
				dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
				dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
				dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
				dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
				dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
				dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
				dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));
				dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
				dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
				dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
				dataSetVo.setRoiYn("N");
				dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
				dataSetVo.setDataCd((String) dataset.get("dataCd"));
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr((String) dataset.get("addr"));
				dataSetVo.setFileNm(outFileNm);
				dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
				dataSetVo.setFileTy((String) dataset.get("fileTy"));
				dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
				dataSetVo.setYear((String) dataset.get("year"));
				dataSetVo.setDpi((String) dataset.get("dpi"));
				dataSetVo.setFullFileCoursNm(outFilePath);
				Date dateBegin = (Date) dataset.get("potogrfBeginDt");
				Date dateEnd = (Date) dataset.get("potogrfEndDt");
				dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
				dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
				dataSetVo.setWorkKind("3");
				dataSetVo.setSatDir((String) dataset.get("satDir"));
				dataSetVo.setMapNm((String) dataset.get("mapNm"));
				cmsc003Service.insertDatasetInfo(dataSetVo);

				// 좌표없는 tif 생성
				File cropFile = new File(outFilePath);
				BufferedImage image = ImageIO.read(cropFile);
				File outputfile = new File(outFilePath.replace(".tif", "_s.tif"));
				ImageIO.write(image, "tif", outputfile);

				jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
				jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
				jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
				jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());
				jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
			}
		}

		return mapper.writeValueAsString(jsonMap);
	}

	/**
	 * 컬러합성
	 * 
	 * @param params  "red" ->
	 *                "D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_B.tif"
	 *                "green" ->
	 *                "D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_G.tif"
	 *                "blue" ->
	 *                "D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_N.tif"
	 *                "name" -> "test.tif"
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	// 컬러합성
	// WORK_KIND 4
	@RequestMapping(value = "cisc007/colorResult.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String Result(@RequestParam HashMap<String, Object> params, HttpServletRequest request, ModelMap model)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		String filName = (String) params.get("name");

		GImageProcessor imgProcessor = new GImageProcessor();
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR;

		String inRedFilePath = (String) params.get("red");
		String inGreenFilePath = (String) params.get("green");
		String inBlueFilePath = (String) params.get("blue");
		String inExtraFilePath = (String) params.get("extra");

		String outputDir = new File(inRedFilePath).getParent();
		outputDir = outputDir.replaceAll("\\\\", "\\/");
		String outFilePath = outputDir + "/" + filName + ".tif"; // /home/TempData/usgs_work/file.tif
		// 파일 이름 중복 시
		File outFile = new File(outFilePath);
		if (outFile.exists()) {
			String nm = filName;
			File upFile = outFile.getParentFile();
			File[] files = upFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(nm) && !name.endsWith(".tif");
				}
			});
			if (files != null) {
				int length = files.length;
				filName += "(" + length + ")";
			}
			outFilePath = outputDir + "/" + filName + ".tif";
		}

		GTiffDataReader.BIT16ToBIT8 inRedMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader.BIT16ToBIT8 inGreenMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader.BIT16ToBIT8 inBlueMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		if (inExtraFilePath.contentEquals("none")) { // 3band 컬러합성
			procCode = imgProcessor.procColorComposite(inRedFilePath, inRedMaxBit16, inGreenFilePath, inGreenMaxBit16,
					inBlueFilePath, inBlueMaxBit16, outFilePath);
		} else { // 4band 컬러합성
			procCode = imgProcessor.procMerge4Band(inRedFilePath, inGreenFilePath, inBlueFilePath, inExtraFilePath,
					outFilePath);
		}
		jsonMap.put("procCode", procCode);
		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		int thumbnailWidth = 1000;
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

		String outThumFilePathName = outFilePath.replace(".tif", ".png"); // /home/TempData/usgs_work/file.png
		thumprocCode = imgProcessor.createThumbnailImage(outFilePath, inMaxBit16, outThumFilePathName, outImgFormat,
				thumbnailWidth, resampleMethod);

		jsonMap.put("thumprocCode", thumprocCode);
		jsonMap.put("fileName", outFilePath);
		jsonMap.put("filePath", outThumFilePathName);

		// 처리내역 저장
		String disasterId = (String) params.get("disasterId"); // 재난 ID
		EgovMap usgs = cmsc003Service.selectUsgsByInFileCours(inRedFilePath); // 원본
		if (usgs == null) {
			usgs = cmsc003Service.selectUsgsWorkByInFileCours(inRedFilePath); // 처리
		}
		if (usgs != null) {
			CISC001WorkResultVO workVO = new CISC001WorkResultVO();
			workVO.setWorkKind("4"); // 컬러합성
			workVO.setVidoId((Integer) usgs.get("vidoId"));
			workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
			workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
			workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
			workVO.setVidoNm(filName);
			workVO.setInnerFileCoursNm(outFilePath);
			workVO.setLtopCrdntX(((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			workVO.setLtopCrdntY(((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			workVO.setRbtmCrdntX(((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			workVO.setRbtmCrdntY(((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			workVO.setDisasterId(disasterId);
			cisc001Service.insertWorkResult2(workVO);

			jsonMap.put("ltopCrdntX", ((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			jsonMap.put("ltopCrdntY", ((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			jsonMap.put("rbtmCrdntX", ((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			jsonMap.put("rbtmCrdntY", ((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
		} else {
			// 긴급
			EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(inRedFilePath); // 원본
			if (dataset != null) {
				// inert dataset info
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
				dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
				dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
				dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
				dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
				dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
				dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
				dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
				dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));
				dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
				dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
				dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
				dataSetVo.setRoiYn("N");
				dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
				dataSetVo.setDataCd((String) dataset.get("dataCd"));
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr((String) dataset.get("addr"));
				dataSetVo.setFileNm(filName);
				dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
				dataSetVo.setFileTy((String) dataset.get("fileTy"));
				dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
				dataSetVo.setYear((String) dataset.get("year"));
				dataSetVo.setDpi((String) dataset.get("dpi"));
				dataSetVo.setFullFileCoursNm(outFilePath);
				Date dateBegin = (Date) dataset.get("potogrfBeginDt");
				Date dateEnd = (Date) dataset.get("potogrfEndDt");
				dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
				dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
				dataSetVo.setWorkKind("4");
				dataSetVo.setSatDir((String) dataset.get("satDir"));
				dataSetVo.setMapNm((String) dataset.get("mapNm"));
				cmsc003Service.insertDatasetInfo(dataSetVo);

				// 좌표없는 tif 생성
				File cropFile = new File(outFilePath);
				BufferedImage image = ImageIO.read(cropFile);
				File outputfile = new File(outFilePath.replace(".tif", "_s.tif"));
				ImageIO.write(image, "tif", outputfile);

				jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
				jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
				jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
				jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());
				jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
			}
		}

		// 프로젝트 key가 있으면 로그 기록
		if (request.getSession().getAttribute("projectKey") != null
				&& !request.getSession().getAttribute("projectKey").equals("")) {
			CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
			logVO.setProjectId((String) request.getSession().getAttribute("projectKey")); // 프로젝트 ID
			logVO.setWorkKind("4"); // 컬러합성
			logVO.setAlgorithmNm("");
			logVO.setInputFileNm(filName);
			logVO.setRedBand(inRedFilePath);
			logVO.setGreenBand(inGreenFilePath);
			logVO.setBlueBand(inBlueFilePath);
			logVO.setTargetFileNm("");
			logVO.setOutputFileNm(filName);
			cisc001Service.insertProjectLog(logVO);
		}

		return mapper.writeValueAsString(jsonMap);
	}

	/**
	 * 해당 영상 파일의 히소토그램 을 처리한다. 정보를 가져 온다.
	 * 
	 * @param params  {histo[]=[0, 198, 0, 188, 0, 192]
	 *                input_vido=D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_B.tif,
	 *                result_vido=test.tif, algorithm=Stretch}
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	// 히스토그램
	// WORK_KIND 5
	@RequestMapping(value = "cisc008/histcompl.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String Result(@RequestParam HashMap<String, Object> params,
			@RequestParam(value = "histo[]") List<String> histo, HttpServletRequest request, ModelMap model)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();

		String input_vido = (String) params.get("input_vido");
		String result_vido = (String) params.get("result_vido");
		String aligorithm = (String) params.get("algorithm");
		String auto = (String) params.get("auto");

		String outputDir = new File(input_vido).getParent();
		String outFilePath = outputDir + "/" + result_vido + ".tif"; // /home/TempData/usgs_work/file.tif
		// 파일 이름 중복 시
		File outFile = new File(outFilePath);
		if (outFile.exists()) {
			String nm = result_vido;
			File upFile = outFile.getParentFile();
			File[] files = upFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(nm) && !name.endsWith(".tif");
				}
			});
			if (files != null) {
				int length = files.length;
				result_vido += "(" + length + ")";
			}
			outFilePath = outputDir + "/" + result_vido + ".tif";
		}

		Boolean isApplyBlueBand = true;
		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

		int[] stretchingMin = new int[3];
		int[] stretchingMax = new int[3];

		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;

		if (auto.equals("true")) {
			if (aligorithm.equals("Stretch")) { // auto linearStretch
				procCode = imgProcessor.procHistogramAutoLinearStretching(input_vido, inMaxBit16, outFilePath);
			} else { // auto Equalization
				procCode = imgProcessor.procHistogramAutoEqualization(input_vido, inMaxBit16, outFilePath,
						isApplyBlueBand);
			}
		} else {
			stretchingMin[0] = Integer.parseInt(histo.get(0));
			stretchingMax[0] = Integer.parseInt(histo.get(1));
			stretchingMin[1] = Integer.parseInt(histo.get(2));
			stretchingMax[1] = Integer.parseInt(histo.get(3));
			stretchingMin[2] = Integer.parseInt(histo.get(4));
			stretchingMax[2] = Integer.parseInt(histo.get(5));
			if (aligorithm.equals("Stretch")) {
				procCode = imgProcessor.procHistogramLinearStretching(input_vido, inMaxBit16, outFilePath,
						stretchingMin, stretchingMax);
			} else {
				procCode = imgProcessor.procHistogramAutoEqualization(input_vido, inMaxBit16, outFilePath,
						isApplyBlueBand);
			}
		}

		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		String outThumFilePathName = outFilePath.replace(".tif", ".png");
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		int thumbnailWidth = 1000;
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
		thumprocCode = imgProcessor.createThumbnailImage(outFilePath, inMaxBit16, outThumFilePathName, outImgFormat,
				thumbnailWidth, resampleMethod);
		jsonMap.put("procCode", procCode);
		jsonMap.put("fileName", outFilePath);
		jsonMap.put("filePath", outThumFilePathName);

		// 처리내역 저장
		String disasterId = (String) params.get("disasterId");// 재난 ID
		EgovMap usgs = cmsc003Service.selectUsgsByInFileCours(input_vido); // 원본
		if (usgs == null) {
			usgs = cmsc003Service.selectUsgsWorkByInFileCours(input_vido); // 처리
		}
		if (usgs != null) {
			CISC001WorkResultVO workVO = new CISC001WorkResultVO();
			workVO.setInnerFileCoursNm(outFilePath); // /home/TempData/usgs_work/file.tif
			workVO.setWorkKind("5"); // 히스토그램 조정
			workVO.setDisasterId(disasterId);
			workVO.setVidoId((Integer) usgs.get("vidoId"));
			workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
			workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
			workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
			workVO.setVidoNm(result_vido);
			workVO.setLtopCrdntX(((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			workVO.setLtopCrdntY(((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			workVO.setRbtmCrdntX(((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			workVO.setRbtmCrdntY(((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			cisc001Service.insertWorkResult2(workVO);

			jsonMap.put("ltopCrdntX", ((BigDecimal) usgs.get("ltopCrdntX")).doubleValue());
			jsonMap.put("ltopCrdntY", ((BigDecimal) usgs.get("ltopCrdntY")).doubleValue());
			jsonMap.put("rbtmCrdntX", ((BigDecimal) usgs.get("rbtmCrdntX")).doubleValue());
			jsonMap.put("rbtmCrdntY", ((BigDecimal) usgs.get("rbtmCrdntY")).doubleValue());
			jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
		} else {
			// 긴급
			EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(input_vido); // 원본
			if (dataset != null) {
				// inert dataset info
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
				dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
				dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
				dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
				dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
				dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
				dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
				dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
				dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));
				dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
				dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
				dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
				dataSetVo.setRoiYn("N");
				dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
				dataSetVo.setDataCd((String) dataset.get("dataCd"));
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr((String) dataset.get("addr"));
				dataSetVo.setFileNm(result_vido);
				dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
				dataSetVo.setFileTy((String) dataset.get("fileTy"));
				dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
				dataSetVo.setYear((String) dataset.get("year"));
				dataSetVo.setDpi((String) dataset.get("dpi"));
				dataSetVo.setFullFileCoursNm(outFilePath);
				Date dateBegin = (Date) dataset.get("potogrfBeginDt");
				Date dateEnd = (Date) dataset.get("potogrfEndDt");
				dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
				dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
				dataSetVo.setWorkKind("5");
				dataSetVo.setSatDir((String) dataset.get("satDir"));
				dataSetVo.setMapNm((String) dataset.get("mapNm"));
				cmsc003Service.insertDatasetInfo(dataSetVo);

				// 좌표없는 tif 생성
				File cropFile = new File(outFilePath);
				BufferedImage image = ImageIO.read(cropFile);
				File outputfile = new File(outFilePath.replace(".tif", "_s.tif"));
				ImageIO.write(image, "tif", outputfile);

				jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
				jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
				jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
				jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());
				jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
			}
		}

		// 프로젝트 key가 있으면 로그 기록
		if (request.getSession().getAttribute("projectKey") != null
				&& !request.getSession().getAttribute("projectKey").equals("")) {
			CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
			logVO.setProjectId((String) request.getSession().getAttribute("projectKey")); // 프로젝트 ID
			logVO.setWorkKind("5"); // 히스토그램
			logVO.setAlgorithmNm(aligorithm);
			logVO.setControlType("밴드별 조정"); // 값이 없어서 임시로 하드코딩
			logVO.setAutoAreaControl("MIN/MAX"); // 값이 없어서 임시로 하드코딩
			logVO.setInputFileNm(input_vido);
			logVO.setTargetFileNm("");

			logVO.setOutputFileNm(result_vido);
			cisc001Service.insertProjectLog(logVO);
		}

		return mapper.writeValueAsString(jsonMap);
	}

	/**
	 * 해당 영상 파일의 모자이크 정보를 가져 온다.
	 * 
	 * @param params   {mosaic_vido=D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_B.tif,
	 *                 mosaic_vido_input=D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_B.tif,
	 *                 vido_resul=test.tif}
	 * @param requestR
	 * @param model
	 * @return
	 * @throws Exception
	 */
	// 모자이크
	// WORK_KIND 6
	@RequestMapping(value = "cisc009/mosaicResult.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String search(@RequestParam HashMap<String, Object> params, HttpServletRequest request, ModelMap model)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();

		GImageProcessor imgProcessor = new GImageProcessor();
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		ArrayList<GFileData> inFilePaths = new ArrayList<GFileData>();
		// String outFilePath = "";

		String mosaic_vido = (String) params.get("mosaic_vido"); // 기준영상
		String mosaic_vido_input = (String) params.get("mosaic_vido_input"); // 입력영상
		String mosaic_vido_input2 = (String) params.get("mosaic_vido_input2"); // 입력영상2
		String mosaic_vido_input3 = (String) params.get("mosaic_vido_input3"); // 입력영상3
		String mosaic_vido_input4 = (String) params.get("mosaic_vido_input4"); // 입력영상4

		String vido_resul = (String) params.get("vido_resul"); // 결과영상
		String outputDir = new File(mosaic_vido).getParent();
		String outFilePath = outputDir + "/" + vido_resul + ".tif";
		// 파일 이름 중복 시
		File outTOAFile = new File(outFilePath);
		if (outTOAFile.exists()) {
			String nm = vido_resul;

			File upFile = outTOAFile.getParentFile();
			File[] files = upFile.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					return name.startsWith(nm) && !name.endsWith(".tif");
				}
			});
			if (files != null) {
				int length = files.length;
				vido_resul += "(" + length + ")";
			}
			outFilePath = outputDir + "/" + vido_resul + ".tif";
		}

		GFileData gFileData = new GFileData();
		gFileData._strFilePath = mosaic_vido;
		gFileData._maxBit16 = inMaxBit16;
		inFilePaths.add(gFileData);

		// Reference Image
		GFileData gFileDataInput = new GFileData();
		gFileDataInput._strFilePath = mosaic_vido_input;
		gFileDataInput._maxBit16 = inMaxBit16;
		gFileDataInput._isReferenced = true;
		inFilePaths.add(gFileDataInput);

		// Reference Image
		if (!"".equals(mosaic_vido_input2)) {
			GFileData gFileData2 = new GFileData();
			gFileData2._strFilePath = mosaic_vido_input2;
			gFileData2._maxBit16 = inMaxBit16;
			gFileData2._isReferenced = true;
			inFilePaths.add(gFileData2);
		}
		if (!"".equals(mosaic_vido_input3)) {
			GFileData gFileData3 = new GFileData();
			gFileData3._strFilePath = mosaic_vido_input3;
			gFileData3._maxBit16 = inMaxBit16;
			gFileData3._isReferenced = true;
			inFilePaths.add(gFileData3);
		}
		if (!"".equals(mosaic_vido_input4)) {
			GFileData gFileData4 = new GFileData();
			gFileData4._strFilePath = mosaic_vido_input4;
			gFileData4._maxBit16 = inMaxBit16;
			gFileData4._isReferenced = true;
			inFilePaths.add(gFileData4);
		}

		procCode = imgProcessor.procSimpleMergedMosaic(inFilePaths, outFilePath, resampleMethod);
		String outThumFilePath = outFilePath.replace(".tif", ".png");

		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		int thumbnailWidth = 1000;
		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		thumprocCode = imgProcessor.createThumbnailImage(outFilePath, inMaxBit16, outThumFilePath, outImgFormat,
				thumbnailWidth, resampleMethod);
		inFilePaths.clear();
		inFilePaths = null;
		jsonMap.put("procCode", procCode);
		jsonMap.put("fileName", outFilePath);
		jsonMap.put("filePath", outThumFilePath);

		String disasterId = (String) params.get("disasterId"); // 재난 ID

		// 처리내역 저장
		EgovMap usgs = cmsc003Service.selectUsgsByInFileCours(mosaic_vido); // 원본
		if (usgs == null) {
			usgs = cmsc003Service.selectUsgsWorkByInFileCours(mosaic_vido); // 처리
		}
		if (usgs != null) {
			CISC001WorkResultVO workVO = new CISC001WorkResultVO();
			workVO.setInnerFileCoursNm(outFilePath); // /home/TempData/usgs_work/file.tif
			workVO.setLtopCrdntX(imgProcessor.getMaxY());
			workVO.setLtopCrdntY(imgProcessor.getMinX());
			workVO.setRbtmCrdntX(imgProcessor.getMinY());
			workVO.setRbtmCrdntY(imgProcessor.getMaxX());
			workVO.setVidoNm(vido_resul);
			workVO.setDisasterId(disasterId);
			workVO.setWorkKind("6"); // 모자이크
			workVO.setVidoId((Integer) usgs.get("vidoId"));
			workVO.setPotogrfVidoCd((String) usgs.get("potogrfVidoCd"));
			workVO.setPotogrfBeginDt((Date) usgs.get("potogrfBeginDt"));
			workVO.setPotogrfEndDt((Date) usgs.get("potogrfEndDt"));
			cisc001Service.insertWorkResult2(workVO);

			jsonMap.put("ltopCrdntX", imgProcessor.getMaxY());
			jsonMap.put("ltopCrdntY", imgProcessor.getMinX());
			jsonMap.put("rbtmCrdntX", imgProcessor.getMinY());
			jsonMap.put("rbtmCrdntY", imgProcessor.getMaxX());
			jsonMap.put("mapPrjctnCn", usgs.get("mapPrjctnCn"));
		} else {
			// 긴급
			EgovMap dataset = cmsc003Service.selectDatasetByInFileCours(mosaic_vido); // 원본
			if (dataset != null) {
				// inert dataset info
				CMSC003DataSetVO dataSetVo = new CMSC003DataSetVO();
				dataSetVo.setDatasetNm((String) dataset.get("datasetNm"));
//				dataSetVo.setLtopCrdntX(Double.valueOf(dataset.get("ltopCrdntX").toString()));
//				dataSetVo.setLtopCrdntY(Double.valueOf(dataset.get("ltopCrdntY").toString()));
//				dataSetVo.setRbtmCrdntX(Double.valueOf(dataset.get("rbtmCrdntX").toString()));
//				dataSetVo.setRbtmCrdntY(Double.valueOf(dataset.get("rbtmCrdntY").toString()));
//				dataSetVo.setRtopCrdntX(Double.valueOf(dataset.get("rtopCrdntX").toString()));
//				dataSetVo.setRtopCrdntY(Double.valueOf(dataset.get("rtopCrdntY").toString()));
//				dataSetVo.setLbtmCrdntX(Double.valueOf(dataset.get("lbtmCrdntX").toString()));
//				dataSetVo.setLbtmCrdntY(Double.valueOf(dataset.get("lbtmCrdntY").toString()));

				// minX -> ltopX, lbtmX
				// maxX -> rtopX, rbtmX
				// minY -> lbtmY, rbtmY
				// maxY -> ltopY, rtopY

				dataSetVo.setLtopCrdntX(imgProcessor.getMaxY());
				dataSetVo.setLtopCrdntY(imgProcessor.getMinX());
				dataSetVo.setRbtmCrdntX(imgProcessor.getMinY());
				dataSetVo.setRbtmCrdntY(imgProcessor.getMaxX());
				dataSetVo.setRtopCrdntX(imgProcessor.getMaxY());
				dataSetVo.setRtopCrdntY(imgProcessor.getMaxX());
				dataSetVo.setLbtmCrdntX(imgProcessor.getMinY());
				dataSetVo.setLbtmCrdntY(imgProcessor.getMinX());

				dataSetVo.setMsfrtnTyNm((String) dataset.get("msfrtnTyNm"));
				dataSetVo.setMapPrjctnCn((String) dataset.get("mapPrjctnCn"));
				dataSetVo.setDatasetCoursNm((String) dataset.get("datasetCoursNm"));
				dataSetVo.setRoiYn("N");
				dataSetVo.setUploadDt((Date) dataset.get("uploadDt"));
				dataSetVo.setDataCd((String) dataset.get("dataCd"));
				dataSetVo.setMsfrtnId(disasterId);
				dataSetVo.setAddr((String) dataset.get("addr"));
				dataSetVo.setFileNm(vido_resul);
				dataSetVo.setMsfrtnTyCd((String) dataset.get("msfrtnTyCd"));
				dataSetVo.setFileTy((String) dataset.get("fileTy"));
				dataSetVo.setFileKorNm((String) dataset.get("fileKorNm"));
				dataSetVo.setYear((String) dataset.get("year"));
				dataSetVo.setDpi((String) dataset.get("dpi"));
				dataSetVo.setFullFileCoursNm(outFilePath);
				Date dateBegin = (Date) dataset.get("potogrfBeginDt");
				Date dateEnd = (Date) dataset.get("potogrfEndDt");
				dataSetVo.setPotogrfBeginDt(DateFormatUtils.format(dateBegin, "yyyy-MM-dd"));
				dataSetVo.setPotogrfEndDt(DateFormatUtils.format(dateEnd, "yyyy-MM-dd"));
				dataSetVo.setWorkKind("6");
				dataSetVo.setSatDir((String) dataset.get("satDir"));
				dataSetVo.setMapNm((String) dataset.get("mapNm"));
				cmsc003Service.insertDatasetInfo(dataSetVo);

				// 좌표없는 tif 생성
				File cropFile = new File(outFilePath);
				BufferedImage image = ImageIO.read(cropFile);
				File outputfile = new File(outFilePath.replace(".tif", "_s.tif"));
				ImageIO.write(image, "tif", outputfile);

//				jsonMap.put("ltopCrdntX", ((BigDecimal) dataset.get("ltopCrdntX")).doubleValue());
//				jsonMap.put("ltopCrdntY", ((BigDecimal) dataset.get("ltopCrdntY")).doubleValue());
//				jsonMap.put("rbtmCrdntX", ((BigDecimal) dataset.get("rbtmCrdntX")).doubleValue());
//				jsonMap.put("rbtmCrdntY", ((BigDecimal) dataset.get("rbtmCrdntY")).doubleValue());

				jsonMap.put("ltopCrdntX", dataSetVo.getLtopCrdntX());
				jsonMap.put("ltopCrdntY", dataSetVo.getLtopCrdntY());
				jsonMap.put("rbtmCrdntX", dataSetVo.getRbtmCrdntX());
				jsonMap.put("rbtmCrdntY", dataSetVo.getRbtmCrdntY());

//				jsonMap.put("ltopCrdntX", imgProcessor.getMaxY());
//				jsonMap.put("ltopCrdntY", imgProcessor.getMinX());
//				jsonMap.put("rbtmCrdntX", imgProcessor.getMinY());
//				jsonMap.put("rbtmCrdntY", imgProcessor.getMaxX());

				jsonMap.put("mapPrjctnCn", dataset.get("mapPrjctnCn"));
			}
		}

		// 프로젝트 key가 있으면 로그 기록
		if (request.getSession().getAttribute("projectKey") != null
				&& !request.getSession().getAttribute("projectKey").equals("")) {
			CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
			logVO.setProjectId((String) request.getSession().getAttribute("projectKey")); // 프로젝트 ID
			logVO.setWorkKind("6"); // 모자이크
			logVO.setAlgorithmNm("");
			logVO.setInputFileNm(mosaic_vido_input);
			logVO.setTargetFileNm(mosaic_vido);
			logVO.setOutputFileNm(vido_resul);
			cisc001Service.insertProjectLog(logVO);
		}
		return mapper.writeValueAsString(jsonMap);
	}

}
