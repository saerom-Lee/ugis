package ugis.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.CISC001Service;
import ugis.service.CISC005Service;
import ugis.service.CMSC003Service;
import ugis.service.vo.CISC005VO;
import ugis.service.vo.CMSC003VO3;
import ugis.util.XmlParser;

@Controller
public class CISC005Controller {

	@Resource(name = "cisc005Service")
	CISC005Service cisc005Service;

	@Resource(name = "cisc001Service")
	CISC001Service cisc001Service;
	@Resource(name = "cmsc003Service")
	private CMSC003Service cmsc003Service;

	/**
	 * 초기 화면 표시
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc005.do")
	public String cisc005(ModelMap model) throws Exception {

		return "ci/cisc005_6";
	}

	@RequestMapping(value = "cisc005/absSatSearch.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String absSatSearch(@RequestParam HashMap<String, Object> params, HttpServletRequest request, ModelMap model)
			throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		List<CISC005VO> soilList = null;
		List<CISC005VO> etcList = null;

		String searchKind = (String) params.get("searchKind");
		String[] search_kind = searchKind.split(",");

		String separator = "/";
		// String prefix_img = "/ugis";
		// String prefix_img = "";
		// if (System.getProperty("os.name").indexOf("Windows") > -1) {
		// separator = "\\";
		// // prefix_img = "";
		// }

		for (int i = 0; i < search_kind.length; i++) {
			if (search_kind[i].equals("soil")) {
				soilList = cisc005Service.selectSoilSatList(params); // 국가위성
				for (int j = 0; j < soilList.size(); j++) {
					CISC005VO vo = (CISC005VO) soilList.get(j);

					int idx = vo.getInnerFileCoursNm().lastIndexOf("\\");
					String dirName = vo.getInnerFileCoursNm().substring(0, idx + 1);
					String fileName = vo.getInnerFileCoursNm().substring(idx + 1);
					vo.setFileName(fileName);
					String imgfileName = vo.getInnerFileCoursNm().replaceAll("(?i).tif", ".png");

					// "http://localhost:8080/img/thumnail/36807062s_CC_result.png"
					vo.setImgFullFileName(imgfileName);
					vo.setSatName(fileName.replaceAll("(?i).tif", ""));
					vo.setTifFileName(fileName);
					vo.setDirName(dirName);
					seekFileInfo(vo);
				}

			} else if (search_kind[i].equals("etc")) { // 기타위성
				etcList = cisc005Service.selectEtcSatList(params);
				for (int j = 0; j < etcList.size(); j++) {
					CISC005VO vo = (CISC005VO) etcList.get(j);
					int idx = vo.getInnerFileCoursNm().lastIndexOf(separator);
					String dirName = vo.getInnerFileCoursNm().substring(0, idx + 1);
					String fileName = vo.getInnerFileCoursNm().substring(idx + 1);
					vo.setFileName(fileName);
					String imgfileName = vo.getInnerFileCoursNm().replaceAll("(?i).tif", ".png");
					// "http://localhost:8080/img/thumnail/36807062s_CC_result.png"
					vo.setImgFullFileName(imgfileName);
					vo.setSatName(fileName.replaceAll("(?i).tif", ""));
					vo.setTifFileName(fileName);
					vo.setDirName(dirName);
					seekFileInfo(vo);

				}
			}
		}
		jsonMap.put("soilList", soilList);
		jsonMap.put("etcList", etcList);
		return mapper.writeValueAsString(jsonMap);
	}

	@RequestMapping(value = "cisc005/absSatSearch2.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String absSatSearch2(@RequestParam HashMap<String, Object> params, HttpServletRequest request,
			ModelMap model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		List<CISC005VO> soilList = null;
		List<CISC005VO> etcList = null;

		String searchKind = (String) params.get("searchKind");
		String[] search_kind = searchKind.split(",");

		String separator = "/";
		// String prefix_img = "/ugis";
		// String prefix_img = "";
		// if (System.getProperty("os.name").indexOf("Windows") > -1) {
		// separator = "\\";
		// // prefix_img = "";
		// }

		for (int i = 0; i < search_kind.length; i++) {
			if (search_kind[i].equals("soil")) {
				soilList = cisc005Service.selectSoilSatList(params); // 국가위성
				for (int j = 0; j < soilList.size(); j++) {
					CISC005VO vo = (CISC005VO) soilList.get(j);

					int idx = vo.getInnerFileCoursNm().lastIndexOf("\\");
					String dirName = vo.getInnerFileCoursNm().substring(0, idx + 1);
					String fileName = vo.getInnerFileCoursNm().substring(idx + 1);
					vo.setFileName(fileName);
					String imgfileName = vo.getInnerFileCoursNm().replaceAll("(?i).tif", ".png");

					// "http://localhost:8080/img/thumnail/36807062s_CC_result.png"
					vo.setImgFullFileName(imgfileName);
					vo.setSatName(fileName.replaceAll("(?i).tif", ""));
					vo.setTifFileName(fileName);
					vo.setDirName(dirName);
					seekFileInfo(vo);
				}

			} else if (search_kind[i].equals("etc")) { // 기타위성
				etcList = cisc005Service.selectEtcSatList(params);
				for (int j = 0; j < etcList.size(); j++) {
					CISC005VO vo = (CISC005VO) etcList.get(j);
					int idx = vo.getInnerFileCoursNm().lastIndexOf(separator);
					String dirName = vo.getInnerFileCoursNm().substring(0, idx + 1);
					String fileName = vo.getInnerFileCoursNm().substring(idx + 1);
					vo.setFileName(fileName);
					String imgfileName = vo.getInnerFileCoursNm().replaceAll("(?i).tif", ".png");
					// "http://localhost:8080/img/thumnail/36807062s_CC_result.png"
					vo.setImgFullFileName(imgfileName);
					vo.setSatName(fileName.replaceAll("(?i).tif", ""));
					vo.setTifFileName(fileName);
					vo.setDirName(dirName);
					seekFileInfo(vo);

				}
			}
		}
		jsonMap.put("soilList", soilList);
		jsonMap.put("etcList", etcList);
		return mapper.writeValueAsString(jsonMap);
	}

	// D:\TestData\03_SatImage\kompsat\K3_20200423043412_42327_09361273_L1R\K3_20200423043412_42327_09361273_L1R_B.tif
	public void seekFileInfo(CISC005VO vo) {

		String separator = "/";
		// if (System.getProperty("os.name").indexOf("Windows") > -1) {
		// separator = "\\";
		// }

		String fileFullName = vo.getInnerFileCoursNm(); // tif파일명
		if (fileFullName.contains("LC08_L1TP_114035_20200414_20200422_01_T1_B1.tif")) {
			System.out.println();
		}

		int idx = fileFullName.lastIndexOf(separator);
		String dirName = fileFullName.substring(0, idx + 1);
		String fileName = fileFullName.substring(idx + 1);

		idx = fileName.lastIndexOf("_");
		String filePrefixName = fileName.substring(0, idx);

		if (vo.getPotogrfVidoCd().equals("1")) { // Landsat txt

			try {
				File dir = new File(dirName);
				String[] filenames = dir.list((f, name) -> name.endsWith(".txt") && name.startsWith(filePrefixName));

				if (filenames == null) {
					System.out.println("메타데이터를 찾을수 없습니다." + "[" + filePrefixName + "*.txt]");
					return;
				}
				for (int i = 0; i < filenames.length; i++) {
					if (readTxtFileInfo(dirName + filenames[i], fileName, vo)) {
						vo.setMetaData(filenames[i]);
						break;
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (vo.getPotogrfVidoCd().equals("2")) { // Kompsat Xml
			File dir = new File(dirName);
			String[] filenames = dir.list((f, name) -> name.endsWith(".xml") && name.startsWith(filePrefixName));
			if (filenames == null) {
				System.out.println("메타데이터를 찾을수 없습니다." + "[" + filePrefixName + "*.xml]");
				return;
			}
			try {
				for (int i = 0; i < filenames.length; i++) {
					if (readXmlFileInfo(dirName + filenames[i], fileName, vo)) {
						vo.setMetaData(filenames[i]);
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean readTxtFileInfo(String txtFile, String tifFile, CISC005VO vo) throws Exception {

		boolean findOK = false;

		List<String> lines = Files.readAllLines(Paths.get(txtFile));

		String FILE_NAME_BAND = "";
		String RADIANCE_MULT_BAND_NAME = "";
		String RADIANCE_ADD_BAND_NAME = "";
		String REFLECTANCE_MULT_NAME = "";
		String REFLECTANCE_ADD_NAME = "";

		for (int i = 0; i < lines.size(); i++) {
			if (lines.get(i).indexOf(tifFile.toUpperCase()) > -1) {
				FILE_NAME_BAND = lines.get(i).substring(0, lines.get(i).indexOf("="));
				FILE_NAME_BAND = FILE_NAME_BAND.trim();
				break;
			}
		}
		// BAND 명을 찾았으면
		if (FILE_NAME_BAND.length() > 0) {
			RADIANCE_MULT_BAND_NAME = FILE_NAME_BAND.replace("FILE_NAME", "RADIANCE_MULT");
			RADIANCE_ADD_BAND_NAME = FILE_NAME_BAND.replace("FILE_NAME", "RADIANCE_ADD");
			REFLECTANCE_MULT_NAME = FILE_NAME_BAND.replace("FILE_NAME", "REFLECTANCE_MULT");
			REFLECTANCE_ADD_NAME = FILE_NAME_BAND.replace("FILE_NAME", "REFLECTANCE_ADD");

			String radianceMultVal = "";
			String radianceAddVal = "";
			String reflectanceMultVal = "";
			String reflectanceAddVal = "";

			for (int i = 0; i < lines.size(); i++) {

				if (radianceMultVal.equals("")) {
					if (lines.get(i).indexOf(RADIANCE_MULT_BAND_NAME) > -1) {
						radianceMultVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1, lines.get(i).length());
						vo.setRadianceMult(radianceMultVal.trim());
					}
				}
				if (radianceAddVal.equals("")) {
					if (lines.get(i).indexOf(RADIANCE_ADD_BAND_NAME) > -1) {
						radianceAddVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1, lines.get(i).length());
						vo.setRadianceAdd(radianceAddVal.trim());
					}
				}
				if (reflectanceMultVal.equals("")) {
					if (lines.get(i).indexOf(REFLECTANCE_MULT_NAME) > -1) {
						reflectanceMultVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1,
								lines.get(i).length());
						vo.setReflectanceMult(reflectanceMultVal.trim());
					}
				}
				if (reflectanceAddVal.equals("")) {
					if (lines.get(i).indexOf(REFLECTANCE_ADD_NAME) > -1) {
						reflectanceAddVal = lines.get(i).substring(lines.get(i).indexOf("=") + 1,
								lines.get(i).length());
						vo.setReflectanceAdd(reflectanceAddVal.trim());
					}
				}

				if (!radianceMultVal.equals("") && !radianceAddVal.equals("") && !reflectanceMultVal.equals("")
						&& !reflectanceAddVal.equals("")) {
					findOK = true;
					break;
				}
			}

		}
		return findOK;
	}

	public boolean readXmlFileInfo(String xmlFile, String tifFile, CISC005VO vo) throws Exception {
		// System.out.println(xmlFile);
		XmlParser p = new XmlParser(xmlFile);

		boolean findOK = false;
		// MS1~4까지 잧는다.
		for (int i = 1; i < 5; i++) {
			String ImageFileName = "//Image/" + "MS" + i;

			if (tifFile.equals(p.getString(ImageFileName + "/ImageFileName"))) {
				vo.setGain(p.getString(ImageFileName + "/RadianceConversion/Gain"));
				vo.setOffset(p.getString(ImageFileName + "/RadianceConversion/Offset"));
				/*
				 * System.out.println("Find!!!! "+tifFile);
				 * System.out.println("gain "+vo.getGain());
				 * System.out.println("offset "+vo.getOffset());
				 */

				findOK = true;
				break;
			}
		}

		return findOK;

	}

	/*
	 * @RequestMapping(value="cisc005/absoluteRadiatingCorrection.do",method =
	 * RequestMethod.POST, produces = "application/json; charset=utf8")
	 * 
	 * @ResponseBody public String absoluteRadiatingCorrection(@RequestParam
	 * HashMap<String, Object> params, HttpServletRequest request, ModelMap model)
	 * throws Exception { ObjectMapper mapper = new ObjectMapper(); HashMap<String,
	 * Object> jsonMap = new HashMap<String, Object>();
	 * 
	 * 
	 * String inFilePath; String outTOAReflectanceFilePath; String
	 * outRadianceFilePath; GImageProcessor.ProcessCode procCode =
	 * GImageProcessor.ProcessCode.SUCCESS; GTiffDataReader.BIT16ToBIT8 inMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
	 * AbsoluteRadiatingCorrectionLandsatInputImpl input = new
	 * AbsoluteRadiatingCorrectionLandsatInputImpl();
	 * AbsoluteRadiatingCorrectionKompsatInputImpl input_k = new
	 * AbsoluteRadiatingCorrectionKompsatInputImpl();
	 * 
	 * String satKind = (String)params.get("satKind");; inFilePath =
	 * (String)params.get("inFilePath"); outRadianceFilePath =
	 * (String)params.get("outRadianceFilePath");
	 * 
	 * System.out.println("satKind : " + satKind);
	 * System.out.println("inFilePath : " + inFilePath);
	 * System.out.println("outRadianceFilePath : "+ outRadianceFilePath);
	 * 
	 * 
	 * //처리 로그 기록 //if(procCode.equals("success")){ CISC001ProjectLogVO logVO = new
	 * CISC001ProjectLogVO();
	 * logVO.setProjectId((String)request.getSession().getAttribute("projectKey"));
	 * //프로젝트 ID logVO.setWorkKind("2"); //절대방사보정
	 * logVO.setMetaDataNm((String)params.get("metaData"));
	 * logVO.setInputFileNm(inFilePath); logVO.setOutputFileNm(outRadianceFilePath);
	 * logVO.setAutoAreaControl(""); logVO.setControlType("");
	 * logVO.setRadiatingFormula((String)params.get("radiatingFormula"));
	 * cisc001Service.insertProjectLog(logVO); //}
	 * 
	 * 
	 * if (satKind.equals("1")) { //Landsat //[Landsat] //Gray Image(16bit)
	 * outTOAReflectanceFilePath = (String)params.get("TOAReflectanceFilePath");
	 * //TOA결과 파일은 TOA_Result.... 임시파일로 저장한다.
	 * 
	 * inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
	 * 
	 * input.setReflectanceMultiple(Double.parseDouble((String)params.get(
	 * "ReflectanceMultiple"))); //REFLECTANCE BAND 1
	 * input.setReflectanceAddtion(Double.parseDouble((String)params.get(
	 * "ReflectanceAddtion")));
	 * input.setRadianceMultiple(Double.parseDouble((String)params.get(
	 * "RadianceMultiple"))); //RADIANCE BAND 1
	 * input.setRadianceAddtion(Double.parseDouble((String)params.get(
	 * "RadianceAddtion")));
	 * 
	 * System.out.println("inFilePath : " + inFilePath);
	 * System.out.println("outRadianceFilePath : "+ outRadianceFilePath);
	 * System.out.println("outTOAReflectanceFilePath : "+
	 * outTOAReflectanceFilePath);
	 * System.out.println("input.getRadianceMultiple() : " +
	 * input.getRadianceMultiple());
	 * System.out.println("input.getRadianceAddtion() : " +
	 * input.getRadianceAddtion());
	 * System.out.println("input.getReflectanceMultiple() : " +
	 * input.getReflectanceMultiple());
	 * System.out.println("input.getReflectanceAddtion() : " +
	 * input.getReflectanceAddtion());
	 * 
	 * procCode = imgProcessor.procAbsoluteRadiatingCorrectionForLandsat(inFilePath,
	 * inMaxBit16, input, outTOAReflectanceFilePath, outRadianceFilePath);
	 * System.out.println("procCode : " + procCode.toString());
	 * jsonMap.put("procCode", procCode);
	 * 
	 * 
	 * }else { //Kompsat //[kompsat] //Gray Image(16bit) inMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
	 * input_k.setRadianceMultiple(Double.parseDouble((String)params.get("gain")));
	 * input_k.setRadianceAddtion(Double.parseDouble((String)params.get("offset")));
	 * procCode = imgProcessor.procAbsoluteRadiatingCorrectionForKompsat(inFilePath,
	 * inMaxBit16, (AbsoluteRadiatingCorrectionKompsatInput) input_k,
	 * outRadianceFilePath); jsonMap.put("procCode", procCode); }
	 * 
	 * return mapper.writeValueAsString(jsonMap); }
	 */

	@RequestMapping(value = "cisc005/relPhotoSearch.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String relPhotoSearch(@RequestParam HashMap<String, Object> params, HttpServletRequest request,
			ModelMap model) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		// List<?> aeroList = null;
		List<CISC005VO> soilList = null;
		List<CISC005VO> etcList = null;
		// List<CISC005VO> orthophotoList = null;
		// CMSC003VO cmsc003VO = new CMSC003VO();
		String searchKind = (String) params.get("searchKind");
		String[] search_kind = searchKind.split(",");

		String separator = "/";
		// String prefix_img = "";
		// if (System.getProperty("os.name").indexOf("Windows") > -1) {
		// separator = "\\";
		// prefix_img = "";
		// }
		List<EgovMap> aeroList = null;
		// List<EgovMap> orthophotoList = null;
		for (int i = 0; i < search_kind.length; i++) {
			if (search_kind[i].equals("aero")) { // 항공/정사영상
				System.out.println(">>>>>>>>>>>>>>>> aero search");
				CMSC003VO3 cmsc003Vo3 = new CMSC003VO3();
				cmsc003Vo3.setDateFrom((String) params.get("date1"));
				cmsc003Vo3.setDateTo((String) params.get("date2"));
				cmsc003Vo3.setMsfrtnInfoColctRequstId(Long.valueOf(params.get("disasterId").toString()));
				cmsc003Vo3.setDataKind(Integer.valueOf(params.get("dataKind").toString()));
				aeroList = cmsc003Service.selectDatasetByDisasterId(cmsc003Vo3);
			} else if (search_kind[i].equals("soil")) {
				soilList = cisc005Service.selectSoilSatList(params); // 국가위성
				for (int j = 0; j < soilList.size(); j++) {
					CISC005VO vo = (CISC005VO) soilList.get(j);
					int idx = vo.getInnerFileCoursNm().lastIndexOf(separator);
					String dirName = vo.getInnerFileCoursNm().substring(0, idx + 1);
					String fileName = vo.getInnerFileCoursNm().substring(idx + 1);
					vo.setFileName(fileName);
					String imgfileName = vo.getInnerFileCoursNm().replaceAll("(?i).tif", ".png");
					// String imgfileName =
					// prefix_img+"/img/thumnail/"+fileName.replaceAll("(?i).tif", ".png");
					vo.setImgFullFileName(imgfileName);
					vo.setSatName(fileName.replaceAll("(?i).tif", ""));
					vo.setDirName(dirName);
					vo.setTifFileName(fileName);
				}
			} else if (search_kind[i].equals("etc")) {
				etcList = cisc005Service.selectEtcSatList(params); // 기타위성
				for (int j = 0; j < etcList.size(); j++) {
					CISC005VO vo = (CISC005VO) etcList.get(j);
					int idx = vo.getInnerFileCoursNm().lastIndexOf(separator);
					String dirName = vo.getInnerFileCoursNm().substring(0, idx + 1);
					String fileName = vo.getInnerFileCoursNm().substring(idx + 1);
					vo.setFileName(fileName);
					String imgfileName = vo.getInnerFileCoursNm().replaceAll("(?i).tif", ".png");
					// String imgfileName =
					// prefix_img+"/img/thumnail/"+fileName.replaceAll("(?i).tif", ".png");
					vo.setImgFullFileName(imgfileName);
					vo.setSatName(fileName.replaceAll("(?i).tif", ""));
					vo.setDirName(dirName);
					vo.setTifFileName(fileName);
				}

			}
			// else if (search_kind[i].equals("orthophoto")) {
			//// orthophotoList = cisc005Service.selectSoilSatList(params); // 정사영상
			// CMSC003VO3 cmsc003Vo3 = new CMSC003VO3();
			// cmsc003Vo3.setDateFrom((String) params.get("date1"));
			// cmsc003Vo3.setDateTo((String) params.get("date2"));
			// cmsc003Vo3.setMsfrtnInfoColctRequstId(Long.valueOf(params.get("disasterId").toString()));
			// cmsc003Vo3.setDataKind(Integer.valueOf(params.get("dataKind").toString()));
			// orthophotoList = cmsc003Service.selectDatasetOrtByDisasterId(cmsc003Vo3);
			// }
		}

		jsonMap.put("aeroList", aeroList);
		jsonMap.put("soilList", soilList);
		jsonMap.put("etcList", etcList);
		// jsonMap.put("orthophotoList", orthophotoList);
		return mapper.writeValueAsString(jsonMap);
	}

	/*
	 * @RequestMapping(value="cisc005/relativeRadiatingCorrection.do",method =
	 * RequestMethod.POST, produces = "application/json; charset=utf8")
	 * 
	 * @ResponseBody public String relativeRadiatingCorrection(@RequestParam
	 * HashMap<String, Object> params, HttpServletRequest request, ModelMap model)
	 * throws Exception {
	 * 
	 * System.out.println("algorithm : "+params.get("algorithm"));
	 * System.out.println("srcFilePath : "+params.get("srcFilePath"));
	 * System.out.println("trgFilePath : "+params.get("trgFilePath"));
	 * System.out.println("outFilePath : "+params.get("outFilePath"));
	 * 
	 * String algorithm = (String)params.get("algorithm"); String srcFilePath =
	 * (String)params.get("srcFilePath"); String trgFilePath =
	 * (String)params.get("trgFilePath"); String outFilePath =
	 * (String)params.get("outFilePath");
	 * 
	 * //처리 로그 기록 //if(procCode.equals("success")){ //프로젝트 key가 있으면 로그 기록 if
	 * (request.getSession().getAttribute("projectKey") != null &&
	 * !request.getSession().getAttribute("projectKey").equals("")) {
	 * CISC001ProjectLogVO logVO = new CISC001ProjectLogVO();
	 * logVO.setProjectId((String)request.getSession().getAttribute("projectKey"));
	 * //프로젝트 ID logVO.setWorkKind("3"); //상대방사보정 logVO.setAlgorithmNm(algorithm);
	 * logVO.setInputFileNm(srcFilePath); logVO.setTargetFileNm(trgFilePath);
	 * logVO.setOutputFileNm(outFilePath); cisc001Service.insertProjectLog(logVO); }
	 * 
	 * 
	 * //}
	 * 
	 * 
	 * ObjectMapper mapper = new ObjectMapper(); HashMap<String, Object> jsonMap =
	 * new HashMap<String, Object>();
	 * 
	 * boolean isApplyOverlapArea = true; GImageProcessor.ProcessCode procCode =
	 * GImageProcessor.ProcessCode.SUCCESS; GTiffDataReader.BIT16ToBIT8 srcMaxBit16
	 * = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; GTiffDataReader.BIT16ToBIT8
	 * trgMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
	 * 
	 * 
	 * 
	 * 
	 * isApplyOverlapArea = true; srcMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
	 * GImageProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16,
	 * trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea);
	 * 
	 * if (algorithm.equals("1")) { //기준영상의 중첩영역 Histogram 기준 isApplyOverlapArea =
	 * true; srcMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
	 * imgProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16,
	 * trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea);
	 * 
	 * }else if (algorithm.equals("2")) { //기준영상의 전체영역 Histogram 기준
	 * isApplyOverlapArea = false; srcMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
	 * imgProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16,
	 * trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea); }else { //Gray
	 * Image (16bit) 기준영상의 중첩영역 Histogram 기준 isApplyOverlapArea = true; srcMaxBit16
	 * = GTiffDataReader.BIT16ToBIT8.MAX_BIT16; trgMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; procCode =
	 * imgProcessor.procRelativeRadiatingCorrection(srcFilePath, srcMaxBit16,
	 * trgFilePath, trgMaxBit16, outFilePath, isApplyOverlapArea); }
	 * 
	 * 
	 * 
	 * 
	 * jsonMap.put("procCode", procCode); return mapper.writeValueAsString(jsonMap);
	 * }
	 */

}
