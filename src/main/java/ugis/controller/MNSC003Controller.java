package ugis.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import ugis.service.MNSC003Service;
import ugis.util.GeoManager;

/**
 * @Class Name : MNSC002Controller.java
 * @Description : 재난정보 수집 설정
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 * SVN 동기화 오류 수정 - 2022.02.25
 */
@Controller
public class MNSC003Controller extends EgovAbstractServiceImpl {
	// 헉!
	/*
	 * @Value("http://222.108.12.130:9999/geoserver/") private String geoserverUrl;
	 * 
	 * @Value("admin") private String geoserverId;
	 * 
	 * @Value("geoserver") private String geoserverPw;
	 */

	@Value("#{fileProperties['geoserver2.url']}")
	private String geoserverUrl;

	@Value("#{fileProperties['geoserver2.id']}")
	private String geoserverId;

	@Value("#{fileProperties['geoserver2.pw']}")
	private String geoserverPw;

	// 서비스
	@Resource(name = "mnsc003Service")
	private MNSC003Service mnsc003Service;

	/**
	 * 초기 화면 설정
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mnsc003.do")
	public String mnsc003(ModelMap model) {

		return "mn/mnsc003_4";
	}

	/* 재난 정보 검색 */
	@ResponseBody
	@RequestMapping(value = "/search.do", method = RequestMethod.PUT)
	public String SEARCH(String mid, String SEARCH_KEYWORD, String MSFRTN_TY_CD, String CTPRVN_NM, String SGG_NM, String EMD_NM,
			String START_PERIOD, String END_PERIOD) {
		String url = "http://127.0.0.1:11000/api/v1/search";
		String sb = "";		
		// String jsonInputString = "{\"keyword\":\"" + SEARCH_KEYWORD + "\",\"MSFRTN_TY_CD\":\"" + MSFRTN_TY_CD
		// 		+ "\",\"start_date\":\"" + START_PERIOD + "\",\"end_date\":\"" + END_PERIOD + "\",\"CTPRVN_NM\":\""
		// 		+ CTPRVN_NM + "\",\"SGG_NM\":\"" + SGG_NM + "\",\"EMD_NM\":\"" + EMD_NM
		// 		+ "\",\"order_field\":\"NEWS_NES_DT\",\"order_type\":\"desc\"}";
		String jsonInputString = "{\"mid\":\""+mid+"\",\"keyword\":\"" + SEARCH_KEYWORD + "\",\"MSFRTN_TY_CD\":\"" + MSFRTN_TY_CD
				+ "\",\"start_date\":\"" + START_PERIOD + "\",\"end_date\":\"" + END_PERIOD + "\",\"CTPRVN_NM\":\""
				+ CTPRVN_NM + "\",\"SGG_NM\":\"" + SGG_NM + "\",\"EMD_NM\":\"" + EMD_NM
				+ "\",\"order_field\":\"NEWS_NES_DT\",\"order_type\":\"desc\"}";
		// System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb = sb + line + "\n";
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}

		return sb.toString();
	}

	/* 재난 정보 검색 결과 추이 그래프 */
	@ResponseBody
	@RequestMapping(value = "/search_timeline.do", method = RequestMethod.PUT)
	public String SEARCH_TIMELINE(String mid , String SEARCH_KEYWORD, String MSFRTN_TY_CD, String CTPRVN_NM, String SGG_NM,
			String EMD_NM, String START_PERIOD, String END_PERIOD) {
		String url = "http://127.0.0.1:11000/api/v1/search/timeline";
		String sb = "";
		// String jsonInputString = "{\"keyword\":\"" + SEARCH_KEYWORD + "\",\"MSFRTN_TY_CD\":\"" + MSFRTN_TY_CD
		// 		+ "\",\"start_date\":\"" + START_PERIOD + "\",\"end_date\":\"" + END_PERIOD + "\",\"CTPRVN_NM\":\""
		// 		+ CTPRVN_NM + "\",\"SGG_NM\":\"" + SGG_NM + "\",\"EMD_NM\":\"" + EMD_NM
		// 		+ "\",\"order_field\":\"NEWS_NES_DT\",\"order_type\":\"desc\"}";

		String jsonInputString = "{\"mid\":\""+mid+"\",\"keyword\":\"" + SEARCH_KEYWORD + "\",\"MSFRTN_TY_CD\":\"" + MSFRTN_TY_CD
				+ "\",\"start_date\":\"" + START_PERIOD + "\",\"end_date\":\"" + END_PERIOD + "\",\"CTPRVN_NM\":\""
				+ CTPRVN_NM + "\",\"SGG_NM\":\"" + SGG_NM + "\",\"EMD_NM\":\"" + EMD_NM
				+ "\",\"order_field\":\"NEWS_NES_DT\",\"order_type\":\"desc\"}";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb = sb + line + "\n";
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}

		return sb.toString();
	}

	/* 결과 내 검색 */
	@ResponseBody
	@RequestMapping(value = "/search_within.do", method = RequestMethod.POST)
	public String WITHIN_SEARCH(String options) {
		String url = "http://127.0.0.1:11000/api/v1/search";
		String sb = "";
		String jsonInputString = "{\"options\":" + options + "}";
		jsonInputString = jsonInputString.replaceAll("&quot;", "\"");
		// System.out.println(jsonInputString);

		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb = sb + line + "\n";
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();

	}

	/* 결과 내 검색- 재난 정보 검색 결과 추이 그래프 */
	@ResponseBody
	@RequestMapping(value = "/search_timeline_within.do", method = RequestMethod.POST)
	public String SEARCH_TIMELINE_WITHIN(String options) {
		String url = "http://127.0.0.1:11000/api/v1/search/timeline";
		String sb = "";
		String jsonInputString = "{\"options\":" + options + "}";
		jsonInputString = jsonInputString.replaceAll("&quot;", "\"");
		// System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb = sb + line + "\n";
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();

	}

	/* 공간 정보 검색 - 재난 정보 목록 */
	@ResponseBody
	@RequestMapping(value = "/space_search.do", method = RequestMethod.PUT)
	public String SPACE_SEARCH(String mid , String SEARCH_KEYWORD, String MSFRTN_TY_CD, String CTPRVN_NM, String SGG_NM,
			String EMD_NM) {
		String url = "http://127.0.0.1:11000/api/v1/space/search";
		String sb = "";
		// String jsonInputString = "{\"keyword\":\"" + SEARCH_KEYWORD + "\",\"MSFRTN_TY_CD\":\"" + MSFRTN_TY_CD
		// 		+ "\",\"CTPRVN_NM\":\"" + CTPRVN_NM + "\",\"SGG_NM\":\"" + SGG_NM + "\",\"EMD_NM\":\"" + EMD_NM + "\"}";
		String jsonInputString = "{\"mid\":\""+mid+"\",\"keyword\":\"" + SEARCH_KEYWORD + "\",\"MSFRTN_TY_CD\":\"" + MSFRTN_TY_CD
				+ "\",\"CTPRVN_NM\":\"" + CTPRVN_NM + "\",\"SGG_NM\":\"" + SGG_NM + "\",\"EMD_NM\":\"" + EMD_NM + "\"}";
		// System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb = sb + line + "\n";
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}

		return sb.toString();
	}

	/* 공간 정보 검색 - 공간 정보 목록 */
	@ResponseBody
	@RequestMapping(value = "/space_search2.do", method = RequestMethod.POST)
	public String SPACE_SEARCH2(String rid) {
		String url = "http://127.0.0.1:11000/api/v1/space/search";
		String sb = "";
		String jsonInputString = "{\"rid\":" + rid + "}";
		// System.out.println(jsonInputString);

		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setDoOutput(true);
			conn.setRequestProperty("Accept", "application/json");
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb = sb + line + "\n";
			}
		} catch (MalformedURLException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}

	/* 공간 정보 검색 - 공간 정보 목록 */
	@ResponseBody
	@RequestMapping(value = "/space_search3.do", method = RequestMethod.POST)
	public List<String> SPACE_SEARCH3(double x, double y) throws MalformedURLException {
		double[] data = { x, y };
		String workspace = "vector";
		// List<String> layers = new ArrayList();
		List<String> layers = new ArrayList<String>();

		GeoManager geoManager = new GeoManager(geoserverUrl, geoserverId, geoserverPw);
		try {
			System.out.println("geoserverUrl : " + geoserverUrl);
			System.out.println("geoserverId : " + geoserverId);
			System.out.println("geoserverPw : " + geoserverPw);
			System.out.println("x : " + x);
			System.out.println("y : " + y);
			layers = geoManager.getIntersectionLayersXY(workspace, data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return layers;
	}

	/* 지오프라 - 썸네일 미리보기 */
	@RequestMapping(value = "/mnsc003thumbnail.do", produces = MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getThumbnail(HttpServletRequest req, HttpServletResponse resp, @RequestParam String json) {

		json = json.replace("&quot;", "\"");

		byte[] fileByteArray = null;
		JSONParser parser = new JSONParser();
		JSONObject thumbObj = null;
		try {
			thumbObj = (JSONObject) parser.parse(json);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(">>>>>>>>>>>>>> mnsc thumbObj " + thumbObj.toJSONString());
		String folder = (String) thumbObj.get("FOLDER_NAM");
		JSONObject resObj = null;
		if (folder.contains("air")) {
			resObj = mnsc003Service.getAirThumbnail(thumbObj);
		}
		if (folder.contains("ort")) {
			resObj = mnsc003Service.getOrtThumbnail(thumbObj);
		}
		if (folder.contains("dem")) {
			resObj = mnsc003Service.getDemThumbnail(thumbObj);
		}
		System.out.println(">>>>>>>>>>>>>> mnsc resObj " + resObj.toJSONString());
		String thumbPath = (String) resObj.get("thumbnailFileCoursNm");
		System.out.println(">>>>>>>>>>>>>> mnsc thumbPath " + thumbPath);
		Path path = Paths.get(thumbPath);
		try {
			fileByteArray = Files.readAllBytes(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileByteArray;
	}
}