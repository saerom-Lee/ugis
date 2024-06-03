package ugis.controller;

import java.net.*;
import java.io.*;
import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import ugis.service.MNSC001Service;

@Controller
public class MNSCNDMSController {
	@ResponseBody
	@RequestMapping(value = "/test.do", method=RequestMethod.GET)
	public String myTest() {
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
}