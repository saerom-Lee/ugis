package ugis.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ugis.service.MNSC001Service;

/**
 * @Class Name : MNSC001Controller.java
 * @Description : 재난정보 모니터링
 * @Modification Information
 * @
 * @  수정일 / 수정자 / 수정내용
 * @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 * SVN 동기화 오류 수정 - 2022.02.25
 */
@Controller
public class MNSC001Controller {
	
	// 서비스
	@Resource(name = "mnsc001Service")
	private MNSC001Service mnsc001Service;
	
	/**
	 * 초기 화면 표시
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mnsc001.do")
	public String mnsc001(ModelMap model){
		
		return "mn/mnsc001";
	}
	
	/*모니터링 리스트 출력 */
	@ResponseBody
	@RequestMapping(value = "/mntrng_tbl.do", method=RequestMethod.PUT)
	public String MNTRNG_TBL () {
		String url = "http://127.0.0.1:11000/api/v1/monitor/news";
		//String url = ApiUrl+"/monitor/news";
		String sb = "";
		String jsonInputString = "{\"start\": 0 , \"rows\":100, \"order_field\":\"NEWS_COLCT_DT\",\"order_type\" : \"desc\" }";
		//System.out.println( jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	/*모니터링 뉴스 상세보기 출력*/
	@ResponseBody
	@RequestMapping(value = "/mntrng_news_detail.do", method=RequestMethod.POST)
	public String MNTRNG_NEWS_DETAIL (String id) {
		String url = "http://127.0.0.1:11000/api/v1/monitor/news/one";
		String sb = "";
		String jsonInputString = "{\"id\":"+id+"}";
		//System.out.println( jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	/* 수집 요청 - 재난 유형, 재난POI-광역시도 */
	@ResponseBody
	@RequestMapping(value = "/col_requst.do", method = RequestMethod.PUT)
	public String COLECT_REQUST(String opt) {
		String url = "http://127.0.0.1:11000/api/v1/options";
		String sb = "";
		String jsonInputString = "{\"opt\":\"" + opt + "\"}";
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
			//br.close();
		} catch(MalformedURLException e) {
			//e.printStackTrace();
			return null;
		} catch(IOException e) {
			//e.printStackTrace();
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
				//e.printStackTrace();
			}
		}
		return sb.toString();
	}

	/* 수집 요청 - 재난POI-시군구,읍면동 */
	@ResponseBody
	@RequestMapping(value = "/col_requst2.do", method = RequestMethod.PUT)
	public String COLECT_REQUST2(String opt, String code) {
		String url = "http://127.0.0.1:11000/api/v1/options";
		String sb = "";
		String jsonInputString =  "{\"opt\":\""+opt+"\",\"code\":\""+code+"\"}";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	
	/*수집요청*/
	@ResponseBody
	@RequestMapping(value = "/col_requst_submit.do", method = RequestMethod.POST)
	public String COLECT_REQUST_SUBMIT(String Disaster_Date, String MSFRTN_TY_CD, String MSFRTN_TY_SRCHWRD, String MSFRTN_AREA_SRCHWRD, String CTPRVN_NM, String SGG_NM,String EMD_NM, String LNBR_ADDR, String force) {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/request";
		String sb = "";
		String jsonInputString =  "{\"Disaster_Date\":\""+Disaster_Date+"\",\"MSFRTN_TY_CD\":\""+MSFRTN_TY_CD+"\",\"MSFRTN_TY_SRCHWRD\":\""+MSFRTN_TY_SRCHWRD+"\",\"MSFRTN_AREA_SRCHWRD\":\""+MSFRTN_AREA_SRCHWRD
				+"\",\"CTPRVN_NM\":\""+CTPRVN_NM+"\",\"SGG_NM\":\""+SGG_NM+"\",\"EMD_NM\":\""+EMD_NM+"\",\"LNBR_ADDR\":\""+LNBR_ADDR+"\",\"force\":\""+force+"\"}";
		// String jsonInputString =  "{\"MSFRTN_TY_CD\":\""+MSFRTN_TY_CD+"\",\"MSFRTN_TY_SRCHWRD\":\""+MSFRTN_TY_SRCHWRD+"\",\"MSFRTN_AREA_SRCHWRD\":\""+MSFRTN_AREA_SRCHWRD
		// 		+"\",\"CTPRVN_NM\":\""+CTPRVN_NM+"\",\"SGG_NM\":\""+SGG_NM+"\",\"EMD_NM\":\""+EMD_NM+"\",\"LNBR_ADDR\":\""+LNBR_ADDR+"\",\"force\":\""+force+"\"}";	
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
			
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	
	/* 최근 수집 목록 출력 */
	@ResponseBody
	@RequestMapping(value = "/colct_tbl.do", method = RequestMethod.PUT)
	public String COLCT_TBL() {
		String url = "http://127.0.0.1:11000/api/v1/portal/news";
		String sb = "";
		String jsonInputString = "{\"start\": 0 , \"rows\":100, \"order_field\":\"NEWS_COLCT_DT\",\"order_type\" : \"desc\" }";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		// System.out.println(sb.toString());
		return sb.toString();
	}
	
	/* 최근 수집 목록  상세보기 출력 */
	@ResponseBody
	@RequestMapping(value = "/colct_tbl_detail.do", method = RequestMethod.GET)
	public String COLCT_TBL_DETAIL(String id) {
		String url = "http://127.0.0.1:11000/api/v1/portal/news/one?news_id="+id;
		String sb = "";
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
			br.close();
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	
	/*재난정보 수집 추이 그래프 출력 */
	@ResponseBody
	@RequestMapping(value = "/mntrng_stats.do", method = RequestMethod.PUT)
	public String MNTRNG_STATS(String before) {
		String url = "http://127.0.0.1:11000/api/v1/portal/news/timeline";
		String sb = "";
		String jsonInputString =  "{\"before\":"+before+"}";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	
	/*워드클라우드*/
	@ResponseBody
	@RequestMapping(value = "/wordcloud.do", method = RequestMethod.PUT)
	public String WORDCLOUD(String before) {
		String url = "http://127.0.0.1:11000/api/v1/sat/disaster/wordcloud";
		String sb = "";
		String jsonInputString =  "{\"before\":"+before+"}";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	/*재난 유형별 수집 통계*/
	@ResponseBody
	@RequestMapping(value = "/typie.do", method = RequestMethod.PUT)
	public String TY_PIE_CHART(String before) {
		String url = "http://127.0.0.1:11000/api/v1/sat/disaster/type";
		String sb = "";
		String jsonInputString =  "{\"before\":"+before+"}";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	
	/*재난 발생지역별 수집 통계*/
	@ResponseBody
	@RequestMapping(value = "/areapie.do", method = RequestMethod.PUT)
	public String AREA_PIE_CHART(String before) {
		String url = "http://127.0.0.1:11000/api/v1/sat/disaster/area";
		String sb = "";
		String jsonInputString =  "{\"before\":"+before+"}";
		//System.out.println(jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		return sb.toString();
	}
	
	/*재난 발생 알림 데이터 전송 */
	@ResponseBody
	@RequestMapping(value = "/mntrng_notice_send.do", method = RequestMethod.PUT)
	public String MNTRNG_NOTICE_SEND(String data) {
		String url = "http://127.0.0.1:11000/api/v1/monitor/sms";
		String sb = "";
		String jsonInputString = "{\"data\":" + data + "}";
		jsonInputString = jsonInputString.replaceAll("&quot;" , "\"");
		//System.out.println(jsonInputString);
		
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("PUT");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
	        conn.setRequestProperty("Accept", "application/json");
	        try(OutputStream os = conn.getOutputStream()) {
	            byte[] input = jsonInputString.getBytes("utf-8");
	            os.write(input, 0, input.length);			
	        }
	        
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while((line = br.readLine())!=null) {
				sb = sb + line + "\n";
			}
		} catch(MalformedURLException e) {
			return null;
		} catch(IOException e) {
			return null;
		}finally {
			try {
				br.close();
			} catch (IOException e) {
				return null;
			}
		}
		// System.out.println(sb.toString());
		 
		return sb.toString();
	}
}

