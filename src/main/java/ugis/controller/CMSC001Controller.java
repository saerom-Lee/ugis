package ugis.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.CMSC001Service;
import ugis.service.vo.CMSC001VO;
import ugis.util.HttpGeoserverAPI;

/**
 * @Class Name : CMSC001Controller.java
 * @Description : 로그인
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
public class CMSC001Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(CMSC001Controller.class);

	@Autowired
	private CMSC001Service cmsc001Service;

	@RequestMapping(value = "/signUpUser.do", method = RequestMethod.POST)
	public String signUpUser(HttpServletRequest req, HttpServletResponse resp, @RequestBody CMSC001VO user)
			throws Exception {

		cmsc001Service.signUpUser(user);

		return "cm/cmsc001";
	}

	/**
	 * 로그인 처리
	 * 
	 * @param req
	 * @param resp
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc001.do")
	public String login(HttpServletRequest req, HttpServletResponse resp, ModelMap model) throws Exception {

		LOGGER.info("access: /login.do");
		String result = "/cm/cmsc001";

//		// delete geoserver test
//		JSONArray dArr = new JSONArray();
//
//		JSONObject dJson1 = new JSONObject();
//		dJson1.put("layername", "Agriculture_as");
//		dJson1.put("extention", "shp");
//		dJson1.put("msfrtnid", "2107060101");
//		dArr.add(dJson1);
//		
//		JSONObject dJson2 = new JSONObject();
//		dJson2.put("layername", "Buldgraphics_as");
//		dJson2.put("extention", "shp");
//		dJson2.put("msfrtnid", "2107060101");
//		dArr.add(dJson2);
//		
//		JSONObject dataJson = new JSONObject();
//		dataJson.put("data", dArr);
//
//		// send delete API
//		HttpGeoserverAPI api = new HttpGeoserverAPI();
//		api.send("", dataJson.toString());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (!(auth instanceof AnonymousAuthenticationToken)) {
			UserDetails loggedInUser = cmsc001Service.loadUserByUsername(auth.getName());
			/* The user is logged in :) */
			if (loggedInUser != null) {
				result = "redirect:/mnsc001.do";
			}
		}
		return result;
	}

	@RequestMapping(value = "/updateUserPwd.do")
	public String updateUserPwd(HttpServletRequest req, HttpServletResponse resp) throws Exception {

		String id = req.getParameter("id");
		String beforePwd = req.getParameter("before_pwd");

		CMSC001VO user = cmsc001Service.getUserById(id, beforePwd);
		if (user != null) {
			String afterPwd = req.getParameter("after_pwd");
			String afterPwdCheck = req.getParameter("after_pwd_check");
			if (afterPwd.equals(afterPwdCheck)) {
				cmsc001Service.updateUserPwd(id, beforePwd, afterPwd);
			}
		}
		return "cm/cmsc001";
	}

	/**
	 * 비밀번호 변경 팝업
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc001_pop_01.do")
	public String changePwdPopUp() throws Exception {
		return "user/cmsc001_pop_01";
	}
}
