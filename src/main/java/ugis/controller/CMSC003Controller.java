package ugis.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.cmmn.imgproc.ShpDataReader;
import ugis.service.CMSC003Service;
import ugis.service.CMSC003ServiceOthers;
import ugis.service.vo.CMSC003VO;
import ugis.service.vo.CMSC003VO2;

/**
 * @Class Name : CMSC003Controller.java
 * @Description : 긴급공간정보 생성
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
@Controller
@SuppressWarnings("unchecked")
// 긴급공간정보 생성 처리 Controller
public class CMSC003Controller {

	// 원본데이터 검색 서비스 (자르기 전)
	@Resource(name = "cmsc003Service")
	private CMSC003Service cmsc003Service;

	// 긴급공간정보 검색 서비스 (자른 후)
	@Resource(name = "cmsc003ServiceOthers")
	private CMSC003ServiceOthers cmsc003ServiceOthers;

	@Resource(name = "fileProperties")
	private Properties fileProperties;

	/**
	 * 초기 화면 표시
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc003.do")
	public String cmsc003(ModelMap model) {
		return "cm/cmsc003";
	}

	/**
	 * 시도 리스트 조회
	 * 
	 * @param
	 * @return JSONObject
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc003G001List.do")
	@ResponseBody
	public JSONObject cmsc003G001List(HttpServletRequest req, HttpServletResponse resps) {
		JSONObject obj = new JSONObject();
		obj.put("selectG001List", cmsc003Service.selectG001List());
		return obj;
	}

	/**
	 * 선택 시도 중심 점 및 시도 내 시군구 리스트 조회
	 * 
	 * @param CMSC003VO
	 * @return JSONObject
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc003G010List.do")
	@ResponseBody
	public JSONObject cmsc003G010List(HttpServletRequest req, HttpServletResponse resps, CMSC003VO cmsc003VO) {
		JSONObject obj = new JSONObject();
		obj.put("selectG010List", cmsc003Service.selectG010List(cmsc003VO));
		obj.put("selectG001Geom", cmsc003Service.selectG001Geom(cmsc003VO));
		return obj;
	}

	/**
	 * 선택 시군구 중심 점 및 시군구 내 읍면동 리스트 조회
	 * 
	 * @param CMSC003VO
	 * @return JSONObject
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc003G011List.do")
	@ResponseBody
	public JSONObject cmsc003G011List(HttpServletRequest req, HttpServletResponse resps, CMSC003VO cmsc003VO) {
		JSONObject obj = new JSONObject();
		obj.put("selectG010List", cmsc003Service.selectG011List(cmsc003VO));
		obj.put("selectG010Geom", cmsc003Service.selectG010Geom(cmsc003VO));
		return obj;
	}

	/**
	 * 선택 읍면동 중심점 조회
	 * 
	 * @param CMSC003VO
	 * @return JSONObject
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc003G011Geom.do")
	@ResponseBody
	public JSONObject cmsc003G011Geom(HttpServletRequest req, HttpServletResponse resps, CMSC003VO cmsc003VO) {
		JSONObject obj = new JSONObject();
		obj.put("selectG010Geom", cmsc003Service.selectG011Geom(cmsc003VO));
		return obj;
	}

	@RequestMapping(value = "/cmsc003mosaicResult.do", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject cmsc003mosaicResult(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody JSONObject createObj) throws Exception {

		JSONObject resObj = new JSONObject();
		JSONParser parser = new JSONParser();

		List<String> pathList = new ArrayList<String>();
		try {
			Object obj = parser.parse(createObj.toString());
			pathList = cmsc003Service.cmsc003mosaicResult((JSONObject) obj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		resObj.put("pathList", pathList);
		return resObj;
	}

	@RequestMapping(value = "/cmsc003searchOthers.do")
	// 긴급공간정보 검색
	public ModelAndView cmsc003search2(HttpServletRequest req, HttpServletResponse resp, ModelMap model,
			@ModelAttribute CMSC003VO cmsc003VO) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");

		// 운영 배포용, 테스트 시 주석처리
		// Geoserver 레이어 조회(건물, 도로 등)
		List<?> digitalMapList = cmsc003ServiceOthers.selectDigitalMapList(cmsc003VO);

		// 오라클 항공영상(오라클 연계안할 시 주석처리)
		// 지리원 지오프라 DB 조회
		List<EgovMap> airOrientMapList = (List<EgovMap>) cmsc003ServiceOthers.selectAirOrientMap(cmsc003VO);

		// 오라클 정사영상(오라클 연계안할 시 주석처리)
		// 지리원 지오프라 DB 조회
		List<EgovMap> ortOrientMapList = (List<EgovMap>) cmsc003ServiceOthers.selectOrtOrientMap(cmsc003VO);

		// 오라클 DEM(오라클 연계안할 시 주석처리)
		// 지리원 지오프라 DB 조회
		List<EgovMap> demList = (List<EgovMap>) cmsc003ServiceOthers.selectDemMap(cmsc003VO);

		// 격자 통계 레이어
		// Geoserver 레이어 조회
		List<?> graphicList = cmsc003ServiceOthers.selectGraphicsList(cmsc003VO);

		// 위성영상
		// Postgres DB TN_USGS 조회
		// POTOGRF_VIDO_CD(촬영영상코드)
		// 0 : 국토위성영상 CAS
		// 1 : LANDSAT
		// 2 : KOMPSAT
		// 6 : SENTINEL

		List<?> satelliteList = cmsc003ServiceOthers.selectUsgsSatelliteList(cmsc003VO);

		JSONObject existObj = new JSONObject();
		//existObj.put("digitalMap", new JSONArray());
		//existObj.put("airOrientalMap", new JSONArray());
		//existObj.put("ortOrientalMap", new JSONArray());
		//existObj.put("demMap", new JSONArray());
		
		existObj.put("digitalMap", digitalMapList);
		existObj.put("airOrientalMap", airOrientMapList);
		existObj.put("ortOrientalMap", ortOrientMapList);
		existObj.put("demMap", demList);
		existObj.put("satellite", satelliteList);
		existObj.put("graphicsMap", graphicList);
		mav.addObject("existing", existObj);

		JSONObject disasterList = cmsc003ServiceOthers.selectUsgsDisaterList(cmsc003VO);
		mav.addObject("disaster", disasterList);

		JSONObject analyList = cmsc003ServiceOthers.selectUsgsAnalysisList(cmsc003VO);
		mav.addObject("analysis", analyList);

		return mav;
	}

	@RequestMapping(value = "/cmsc003search.do")
	// 원본데이터 검색
	public ModelAndView cmsc003search(HttpServletRequest req, HttpServletResponse resp, ModelMap model,
			@ModelAttribute CMSC003VO cmsc003VO) throws Exception {

		ModelAndView mav = new ModelAndView("jsonView");
		cmsc003VO.setSearchWork("on");

		// 운영 배포용, 테스트 시 주석처리
		// 연속수치지도
		// Geoserver 레이어 조회(건물, 도로 등)
		List<?> digitalMapList = cmsc003Service.selectDigitalMapList(cmsc003VO);

		// 오라클 항공영상(오라클 연계안할 시 주석처리)
		// 지리원 지오프라 DB 조회
		List<EgovMap> airOrientMapList = (List<EgovMap>) cmsc003Service.selectAirOrientMap(cmsc003VO);

		// 오라클 정사영상(오라클 연계안할 시 주석처리)
		// 지리원 지오프라 DB 조회
		JSONObject ortOrientObj = cmsc003Service.selectOrtOrientMap(cmsc003VO);

		// 정사영상 리스트
		// 지리원 지오프라 DB 조회
		List<EgovMap> ortOrientMapList = (List<EgovMap>) ortOrientObj.get("ortMapList");
		// 도엽명
		String mapNm = (String) ortOrientObj.get("mapNm");

		// 오라클 DEM(오라클 연계안할 시 주석처리)
		// 지리원 지오프라 DB 조회
		List<EgovMap> demList = (List<EgovMap>) cmsc003Service.selectDemMap(cmsc003VO);

		// 격자 통계 레이어
		// Geoserver 레이어 조회
		List<?> graphicList = cmsc003Service.selectGraphicsList(cmsc003VO);

		// 위성영상
		// Postgres DB TN_USGS 조회
		// POTOGRF_VIDO_CD(촬영영상코드)
		// 0 : 국토위성영상 CAS
		// 1 : LANDSAT
		// 2 : KOMPSAT
		// 6 : SENTINEL
		List<?> satelliteList = cmsc003Service.selectUsgsSatelliteList(cmsc003VO);
		JSONObject existObj = new JSONObject();
		//existObj.put("digitalMap", new JSONArray());
		//existObj.put("airOrientalMap", new JSONArray());
		//existObj.put("ortOrientalMap", new JSONArray());
		//existObj.put("demMap", new JSONArray());

		existObj.put("digitalMap", digitalMapList);
		existObj.put("airOrientalMap", airOrientMapList);
		existObj.put("ortOrientalMap", ortOrientMapList);
		existObj.put("demMap", demList);

		existObj.put("satellite", satelliteList);
		existObj.put("graphicsMap", graphicList);
		mav.addObject("existing", existObj);

		// 긴급영상
		// Postgres DB TN_USGS 조회
		// POTOGRF_VIDO_CD(촬영영상코드)
		// 3 : 드론_FLOOD 수해
		// 4 : 드론_LANDSLIDE 산사태
		// 5 : 드론_FOREST FIRE 산불
		// 10 : 드론_FOREST FIRE 지진
		// 11 : 드론_FOREST FIRE 해양
		// 12 : 드론_FOREST FIRE 적조
		JSONObject disasterList = cmsc003Service.selectUsgsDisaterList(cmsc003VO);
		mav.addObject("disaster", disasterList);

		JSONObject analyList = cmsc003Service.selectUsgsAnalysisList(cmsc003VO);
		mav.addObject("analysis", analyList);
		//mav.addObject("mapNm", "");
		mav.addObject("mapNm", mapNm);

		return mav;
	}

	@RequestMapping(value = "/cmsc003searchDataset.do")
	public ModelAndView cmsc003searchDataset(HttpServletRequest req, HttpServletResponse resp, ModelMap model,
			@ModelAttribute CMSC003VO2 cmsc003VO2) throws Exception {
		
		//긴급공간생성 관리페이지 > 검색  2024.02.26
		JSONObject dataset = cmsc003Service.cmsc003searchDataset(cmsc003VO2);
		ModelAndView mav = new ModelAndView("jsonView");

		mav.addObject("existing", dataset.get("existing"));
		mav.addObject("analysis", dataset.get("analysis"));
		mav.addObject("disaster", dataset.get("disaster"));
		mav.addObject("datasetCoursNm", dataset.get("datasetCoursNm"));
		mav.addObject("datasetNm", dataset.get("datasetNm"));

		return mav;

	}

	//국토정보플랫폼 , 국토영상공급시스템전송 (대민, 행정망)
	@RequestMapping(value = "/cmsc003sendDataset.do")
	public ModelAndView cmsc003sendDataset(HttpServletRequest req, HttpServletResponse resp, ModelMap model,
			@ModelAttribute CMSC003VO2 cmsc003VO2) throws Exception {

		cmsc003Service.cmsc003sendDataset(cmsc003VO2);
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("send", cmsc003VO2);
		return mav;

	}
	

	@RequestMapping(value = "/cmsc003thumbnail.do", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] cmsc003thumbnail(HttpServletRequest req, HttpServletResponse resp, CMSC003VO cmsc003VO) {

		String thumbPath = cmsc003VO.getThumbnailFileCoursNm();
		// String gfraPath = fileProperties.getProperty("ugis.gfra.download.path");

		// gfa 썸네일 확장자 -> .jgp 포맷으로 변경
		if (thumbPath.toUpperCase().contains("GEOFRADATA")) {
			thumbPath = thumbPath.replace(".png", ".jpg");
		}

		File thumbFile = new File(thumbPath);
		if (!thumbFile.exists()) {
			if (thumbPath.endsWith(".png")) {
				thumbPath = thumbPath.replace(".png", ".jpg");
			} else if (thumbPath.endsWith(".jpg")) {
				thumbPath = thumbPath.replace(".jpg", ".png");
			}
		}

		Path path = Paths.get(thumbPath);
		byte[] fileByteArray = null;
		try {
			fileByteArray = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileByteArray;
	}

	@RequestMapping(value = "/cmsc003shpThumbnail.do")
	@ResponseBody
	public byte[] cmsc003vectorThumbnail(HttpServletRequest req, HttpServletResponse resp, @RequestParam String json) {

		json = json.replace("&quot;", "\"");
		JSONParser parser = new JSONParser();
		JSONObject thumbObj = null;
		try {
			thumbObj = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String shpPath = (String) thumbObj.get("vectorFileCoursNm");
		ShpDataReader reader = new ShpDataReader(new File(shpPath));

		// if (thumbObj.get("analsVidoId") != null) {
		reader.setAnals(true);
		// }

		double ulx = 0;
		double uly = 0;
		double lrx = 0;
		double lry = 0;

		String epsg = (String) thumbObj.get("mapPrjctnCn");
		try {
			if (thumbObj.get("createInfo") != null) {
				JSONObject createInfo = (JSONObject) thumbObj.get("createInfo");
				JSONObject roi = (JSONObject) createInfo.get("roi");
				JSONObject roi5179 = (JSONObject) roi.get("roi5179");
				ulx = (double) roi5179.get("ulx");
				uly = (double) roi5179.get("uly");
				lrx = (double) roi5179.get("lrx");
				lry = (double) roi5179.get("lry");

				String resultPath = reader.cropAndToImage(ulx, uly, lrx, lry, epsg, shpPath);
				Path path = Paths.get(resultPath);
				byte[] byteArr = Files.readAllBytes(path);

				File pngFile = new File(resultPath);
				pngFile.delete();

				return byteArr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = "/cmsc003shpThumbnail2.do")
	@ResponseBody
	public String cmsc003vectorThumbnail(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody JSONObject param) {

		JSONParser parser = new JSONParser();
		JSONObject thumbObj = null;
		try {
			thumbObj = (JSONObject) parser.parse(param.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String thumbPath = (String) thumbObj.get("thumbnailFileCoursNm");
		ShpDataReader reader = new ShpDataReader(new File(thumbPath));
		try {
			return reader.readToByte(thumbPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	//재난 지역 영상 저장
	@RequestMapping(value = "/cmsc003saveData.do", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject cmsc003saveData(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody JSONObject createObj) throws Exception {
		

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(createObj.toString());
		return cmsc003Service.cmsc003saveData((JSONObject) obj);
	}

	//긴급공간정보 생성
	@RequestMapping(value = "/cmsc003createData2.do", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject cmsc003createData2(HttpServletRequest req, HttpServletResponse resp,
			@RequestBody JSONObject createObj) throws Exception {

		JSONObject resObj = new JSONObject();
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(createObj.toString());
			resObj = cmsc003Service.cmsc003createData((JSONObject) obj);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return resObj;

	}

	
	
	@RequestMapping(value = "/cmsc003searchDisasterId.do")
	@ResponseBody
	public JSONArray cmsc003searchDisasterId(HttpServletRequest req, HttpServletResponse resp,
			@ModelAttribute CMSC003VO2 cmsc003VO2) throws UnsupportedEncodingException, IOException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		List<?> list = cmsc003Service.selectDisasterInfoId(cmsc003VO2);
		JSONArray jsonArr = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonObj = new JSONObject();
			Map<String, Object> info = (Map<String, Object>) list.get(i);
			System.out.println("user:" + info);
			Set<Map.Entry<String, Object>> entries = info.entrySet();

			for (Map.Entry<String, Object> entry : entries) {
				jsonObj.put(entry.getKey(), entry.getValue());
			}
			jsonArr.add(jsonObj);
		}
		return jsonArr;
	}

}
