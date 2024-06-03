package ugis.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import lombok.RequiredArgsConstructor;
import ugis.service.CTSC004Service;
import ugis.service.vo.CTSC004VO;

@RequiredArgsConstructor
@Controller
public class CTSC004Controller {

	public static final String context = "/ctsc004";
	
	private final CTSC004Service<CTSC004VO> ctsc004service;
	
//	@Resource(name="ctsc004Service")
//	private CTSCBaseService<CTSC004VO> service;
	
//	@Override
//	public CTSCBaseService<CTSC004VO> getService() {
//		return service;
//	}
//	
	@RequestMapping(context + ".do")
	public String viewCtsc004() throws Exception {
		return "ct" + context;
	}
	
	@RequestMapping(context + "/selectTnHistList.do")
	@ResponseBody
	public ModelAndView selectTnHistList(@ModelAttribute CTSC004VO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			List<EgovMap> result = ctsc004service.selectTnHistList(vo);
			mav.addObject("result", result);
		} catch (Exception e) {
			// TODO: handle exception
			mav.addObject("result", "false");
		}
		return mav;
	}
	
	@RequestMapping(context + "/selectTnLogList.do")
	@ResponseBody
	public ModelAndView selectTnLogList(@ModelAttribute CTSC004VO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			List<EgovMap> result = ctsc004service.selectTnLogList(vo);
			mav.addObject("result", result);
			if(vo.getPotogrfVidoCd().equals("free")) {
				mav.addObject("vidoCd", "free");
			} else if(vo.getPotogrfVidoCd().equals("emer")) {
				mav.addObject("vidoCd", "emer");
			}
		} catch (Exception e) {
			// TODO: handle exception
			mav.addObject("result", "false");
		}
		return mav;
	}
	
	@RequestMapping(context + "/selectTnMetaList.do")
	@ResponseBody
	public ModelAndView selectTnMetaList(@ModelAttribute CTSC004VO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		try {
			List<EgovMap> result = ctsc004service.selectTnMetaList(vo);
			mav.addObject("result", result);
		} catch (Exception e) {
			// TODO: handle exception
			mav.addObject("result", "false");
		}
		return mav;
	}
	
//	@RequestMapping(value= context + "/add.do")
//	public ModelAndView addCtsc004(CTSC004VO vo) throws Exception {
//		ModelAndView mav = new ModelAndView("jsonView");
//		mav.addObject("test", "test");
//		return mav;
//	}
//	
//	@RequestMapping(context + "/upt.do")
//	public @ResponseBody ResponseEntity<CTSC004VO> uptCtsc004(@RequestBody CTSC004VO vo) throws Exception {
//		return super.upt(vo);
//	}
//	
//	@RequestMapping(context + "/del.do")
//	public @ResponseBody ResponseEntity<CTSC004VO> delCtsc004(@RequestBody CTSC004VO vo) throws Exception {
//		return super.del(vo);
//	}
//	
//	@RequestMapping(context + "/get.do")
//	public @ResponseBody ResponseEntity<CTSC004VO> getCtsc004(@RequestBody CTSC004VO vo) throws Exception {
//		return super.get(vo);
//	}
//	
//	@RequestMapping(context + "/search.do")
//	public @ResponseBody ResponseEntity<List<CTSC004VO>> searchCtsc004(@RequestParam Integer page, CTSC004VO vo) throws Exception {
//		return super.search(page, vo);
//	}
	
	
}
