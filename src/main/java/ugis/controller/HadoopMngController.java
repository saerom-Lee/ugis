package ugis.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ugis.service.CMSC001Service;

/**
 * 
 * @author cwj
 * @since 2021.10.20
 * 
 * Hadoop 관리
 *
 */
@Controller
public class HadoopMngController {

	
	/**
	 * 워크플로우 관리
	 * 
	 * @author cwj
	 * @since 2021.10.20
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc019.do")
	public String hadoop01(ModelMap model) throws Exception {
		return "hadoop/hadoop01";
	}
	
	/**
	 * 스케쥴 모니터링
	 * 
	 * @author cwj
	 * @since 2021.10.20
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc020.do")
	public String hadoop02(ModelMap model) throws Exception {
		return "hadoop/hadoop02";
	}
	
	/**
	 * 장비 모니터링
	 * 
	 * @author cwj
	 * @since 2021.10.20
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc021.do")
	public String hadoop03(ModelMap model) throws Exception {
		return "hadoop/hadoop03";
	}
	
	/**
	 * SQL관리 및 검색
	 * 
	 * @author cwj
	 * @since 2021.10.20
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc022.do")
	public String hadoop04(ModelMap model) throws Exception {
		return "hadoop/hadoop04";
	}
	
	/**
	 * HDFS 브라우즈 관리
	 * 
	 * @author cwj
	 * @since 2021.10.20
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc023.do")
	public String hadoop05(ModelMap model) throws Exception {
		return "hadoop/hadoop05";
	}
}
