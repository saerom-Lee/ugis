package ugis.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.MainService;

@Controller
public class MainController {

	@Resource(name = "mainService")
	private MainService mainService;
	
	@RequestMapping(value = "/main.do")
	public String main(ModelMap model) throws Exception {
		
		List<?> selectList = mainService.selectRadarList();
		
		System.out.println("xxxx" + selectList.size());
		
		return "main";
	}
	
	@RequestMapping(value = "/common/fileDown.do")
	public void imgFileDown(String fileNm, String msfrtnId, String type, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> param = new HashMap<String, Object>();
		param.put("fileNm", fileNm);
		param.put("msfrtnId", msfrtnId);
		
		String filePath = "";
		List<?> selectList = mainService.selectFileDown(param);
		if (selectList != null && selectList.size() > 0) {
			EgovMap map = (EgovMap) selectList.get(0);
			if (map.get("fileNm") != null && map.get("fullFileCoursNm") != null) {
				filePath = (String)map.get("fullFileCoursNm");
				if ("img".equals(type)) filePath = filePath.replace("tif", "png");
				else if ("dbf".equals(type)) filePath = filePath.replace("shp", "dbf");
				else if ("prj".equals(type)) filePath = filePath.replace("shp", "prj");
				else if ("shx".equals(type)) filePath = filePath.replace("shp", "shx");
			}
		}
		
		System.out.println("다운도르 파일 경로 확인");
		System.out.println(filePath);
		
		File uFile = new File(filePath);
		String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
		System.out.println("filePath >> "+filePath);
		System.out.println("fileName >> "+fileName);
		int fSize = (int) uFile.length();

		System.out.println("fSize >> "+fSize);
		
		if (fSize != 0) {
			System.out.println("--------1");
			String mimetype = "application/x-msdownload";
			response.setContentType(mimetype);
			System.out.println("--------2");
			System.out.println("mimetype >> "+mimetype);
			
			setDisposition(fileName, request, response);
			System.out.println("--------3");
			System.out.println("fileName >> "+fileName);
			System.out.println("--------4");
			System.out.println("request >> "+request);
			System.out.println("--------5");
			System.out.println("response >> "+response);
			
			
			response.setContentLength(fSize);
			
			System.out.println("--------6");
			System.out.println("fSize >> "+fSize);

			BufferedInputStream in = null;
			BufferedOutputStream out = null;

			try {
				in = new BufferedInputStream(new FileInputStream(uFile));
				
				System.out.println("--------7");
				System.out.println("uFile >> "+uFile);
				System.out.println("in >> "+in);
				
				out = new BufferedOutputStream(response.getOutputStream());
				
				System.out.println("-------8");
				System.out.println("out >> "+out);

				
				FileCopyUtils.copy(in, out);
				
				System.out.println("-------9");
				System.out.println("in >> "+in);
				System.out.println("out >> "+out);
				
				out.flush();
			} catch (Exception ex) {
				// ex.printStackTrace();
				System.out.println("-------Exception Exception");
				System.out.println("ex >> "+ex);
				
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ignore) {
						// no-op
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (Exception ignore) {
						// no-op
					}
				}
			}
		}else {
			System.out.println("filePath_2222 >> "+filePath);
			System.out.println("fSize_2222 >> "+fSize);
		}
	}
	    
	private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE") || browser.equals("Trident")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			// throw new RuntimeException("Not supported browser");
			throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}
	
	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Trident") > -1) { // IE11 문자열 깨짐 방지
			return "Trident";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}
	
}
