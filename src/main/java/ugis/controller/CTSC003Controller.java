package ugis.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ugis.service.CMSC002Service;
import ugis.service.CTSCBaseService;
import ugis.service.vo.CTSC003VO;

@Controller
public class CTSC003Controller extends CTSCBaseController<CTSC003VO>{

	public static final String context = "/ctsc003";
	
	@Resource(name="ctsc003Service")
	private CTSCBaseService<CTSC003VO> service;
	
	@Resource(name = "cmsc002Service")
	private CMSC002Service cmsc002Service;
	
	@Override
	public CTSCBaseService<CTSC003VO> getService() {
		return service;
	}
	
	@RequestMapping(context + ".do")
	public String viewCtsc001(Model model) throws Exception {
		model.addAttribute("tab", "003");
		List<?> siList = null ;
		siList = cmsc002Service.selectSiList();
		model.addAttribute("siList",siList);
		return "ct" + context;
	}
	
	@RequestMapping(value= context + "/add.do")
	public ModelAndView addCtsc003(CTSC003VO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("test", "test");
		return mav;
	}
	
	@RequestMapping(context + "/upt.do")
	public @ResponseBody ResponseEntity<CTSC003VO> uptCtsc003(@RequestBody CTSC003VO vo) throws Exception {
		return super.upt(vo);
	}
	
	@RequestMapping(context + "/del.do")
	public @ResponseBody ResponseEntity<CTSC003VO> delCtsc003(@RequestBody CTSC003VO vo) throws Exception {
		return super.del(vo);
	}
	
	@RequestMapping(context + "/get.do")
	public @ResponseBody ResponseEntity<CTSC003VO> getCtsc003(@RequestBody CTSC003VO vo) throws Exception {
		return super.get(vo);
	}
	
	@RequestMapping(context + "/search.do")
	public @ResponseBody ResponseEntity<List<CTSC003VO>> searchCtsc003(@RequestParam Integer page, CTSC003VO vo) throws Exception {
		return super.search(page, vo);
	}
}
