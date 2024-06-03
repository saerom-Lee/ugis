package ugis.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import ugis.service.MNSC002Service;

/**
 * @Class Name : MNSC002Controller.java
 * @Description : 재난정보 수집 설정
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
 *
 */
@Controller
public class MNSC002Controller {

	// 서비스
	@Resource(name = "mnsc002Service")
	private MNSC002Service mnsc002Service;
	
	/**
	 * 초기 화면 설정
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/mnsc002.do")
	public String mnsc002(ModelMap model){
		
		return "mn/mnsc002";
	}
	/*관계기관 URL 변경 */
	@ResponseBody
	@RequestMapping(value = "/gvmagnc.do", method=RequestMethod.POST)
	public String Gvmagnc(String gvmagnc) {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/gvmagnc";
		String sb = "";
		String jsonInputString = "{\"url\":\""+gvmagnc+"\"}";
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
	
	/*현재 설정된 대상기관 조회 */
	@ResponseBody
	@RequestMapping(value = "/today_gvmagnc.do", method=RequestMethod.GET)
	public String Today_Gvmagnc() {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/gvmagnc";
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
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	/*현재 설정된 웹크롤링 설정 조회 - 대상 포털 */
	@ResponseBody
	@RequestMapping(value = "/today_potal.do", method=RequestMethod.GET)
	public String WEBCRWL_POTAL_Today() {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/portal";
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
		
		//System.out.println();
		return sb.toString();
				
	}
	/*현재 설정되어있는 웹크롤링 설정 출력 - 수집주기, 수집 시점, 수집기간*/
	@ResponseBody
	@RequestMapping(value = "/webcrwl_today.do", method=RequestMethod.GET)
	public String WEBCRWL_Today() {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/setup";
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
		
		
		//System.out.println();
		return sb.toString();
				
	}
	/*현재 설정되어있는 웹크롤링 설정 출력 - 키워드*/
	@ResponseBody
	@RequestMapping(value = "/keyword_today.do", method=RequestMethod.GET)
	public String Keyword_Today() {
		String url = "http://127.0.0.1:11000/api/v1/monitor/keyword";
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
		
		//System.out.println();
		return sb.toString();
				
	}
	/*대상포털 변경 */
	@ResponseBody
	@RequestMapping(value = "/potal_change.do", method=RequestMethod.POST)
	public String potal_change (String potal) {
		System.out.println(potal);
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/portal";
		String sb = "";
		String jsonInputString = "{\"url\":\""+potal+"\"}";
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
	
	/*수집 주기 변경 */
	@ResponseBody
	@RequestMapping(value = "/cycle_change.do", method=RequestMethod.POST)
	public String WEBCRWL_COLCT_CYCLE_CHANGE (String cycle ) {
		System.out.println(cycle);
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/setup?opt=cycle";
		String sb = "";
		String jsonInputString = "{\"cycle\":"+cycle+"}";
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
		return sb.toString();
	}
	
	/*수집 기간 변경 */
	@ResponseBody
	@RequestMapping(value = "/pd_change.do", method=RequestMethod.POST)
	public String WEBCRWL_COLCT_PD_CHANGE (String period) {
		System.out.println(period);
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/setup?opt=period";
		String sb = "";
		String jsonInputString = "{\"period\":"+period+"}";
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
	
	/*수집 시점 변경 */
	@ResponseBody
	@RequestMapping(value = "/yr_change.do", method=RequestMethod.POST)
	public String WEBCRWL_COLCT_YR_CHANGE (String year) {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/setup?opt=year";
		String sb = "";
		String jsonInputString = "{\"year\":"+year+"}";
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
	/*키워드 추가 */
	@ResponseBody
	@RequestMapping(value = "/keyword_add.do", method=RequestMethod.POST)
	public String WEBCRWL_MNFRNG_KWRD_ADD (String keyword) {
		String url = "http://127.0.0.1:11000/api/v1/monitor/keyword";
		String sb = "";
		String jsonInputString = "{\"keyword\":\""+keyword+"\"}";
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
	/*키워드 삭제 */
	@ResponseBody
	@RequestMapping(value = "/keyword_delete.do", method= {RequestMethod.DELETE})
	public String WEBCRWL_MNFRNG_KWRD_DELTE (String Delete_keyword) {
		String url = "http://127.0.0.1:11000/api/v1/monitor/keyword";
		String sb = "";
		String jsonInputString = "{\"keyword\":\""+Delete_keyword+"\"}";
		//System.out.println( jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("DELETE");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
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
	/*수집 요청 목록 출력*/
	@ResponseBody
	@RequestMapping(value = "/request_tbl.do", method=RequestMethod.PUT)
	public String Request_TBL () {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/request/list";
		String sb = "";
		String jsonInputString = "{\"order_field\":\"COLCT_BEGIN_DE\",\"order_type\" : \"desc\" }";
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
	
	/*수집요청 삭제*/
	@ResponseBody
	@RequestMapping(value = "/request_delete.do", method= {RequestMethod.DELETE})
	public String REQUEST_DELTE (String id) {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/request/list";
		String sb = "";
		String jsonInputString = "{\"MSFRTN_INFO_COLCT_REQUST_ID\":"+id+"}";
		System.out.println( jsonInputString);
		BufferedReader br = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	        conn.setRequestMethod("DELETE");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	        conn.setDoOutput(true);
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
	/*수집요청 상태 변경*/
	@ResponseBody
	@RequestMapping(value = "/request_state.do", method=RequestMethod.POST)
	public String REQUEST_STATUS (String id, String state) {
		String url = "http://127.0.0.1:11000/api/v1/webcrwl/request/list";
		String sb = "";
		String jsonInputString = "{\"MSFRTN_INFO_COLCT_REQUST_ID\":"+id+",\"COLCT_STTUS_CD\":\""+state+"\"}";
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
}
