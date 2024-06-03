package ugis.controller;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import lombok.RequiredArgsConstructor;
import ugis.service.CTSC005Service;
import ugis.service.CTSCBaseService;
import ugis.service.vo.CTSC005VO;

@RequiredArgsConstructor
@Controller
public class CTSC005Controller {

	private final CTSC005Service<CTSC005VO> ctsc005Service;
	
	public static final String context = "/ctsc005";
	
	
	@RequestMapping(context + ".do")
	public String viewCtsc005() throws Exception {
		return "ct" + context;
	}
	
	@RequestMapping(value= context + "/add.do")
	public ModelAndView addCtsc005(CTSC005VO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("test", "test");
		return mav;
	}
	
	
	@RequestMapping(value = context + "/selectYearStatic.do")
	@ResponseBody
	public ModelAndView selectYearStatic(@RequestParam HashMap<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

			if(params.containsKey("free")) {
				List<EgovMap> staticFree  = ctsc005Service.selectYearStaticFree(params);
				mav.addObject("staticFree", staticFree);
			} 
			if(params.containsKey("emer")) {
				List<EgovMap> staticEmer = ctsc005Service.selectYearStaticEmer(params);
				mav.addObject("staticEmer", staticEmer);
			}

		
		
		return mav;
	}
	
	@RequestMapping(value = context + "/selectKindsStatic.do")
	@ResponseBody
	public ModelAndView selectKindsStatic(@RequestParam HashMap<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			List<EgovMap> staticKinds  = ctsc005Service.selectKindsStatic(params);
			mav.addObject("staticKinds", staticKinds);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			mav.addObject("result", "false");
		}
		return mav;
	}
	
	
}
