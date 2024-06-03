package ugis.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import ugis.service.CMSC002Service;
import ugis.service.CTSCBaseService;
import ugis.service.impl.CTSC001BatchFileServiceImpl;
import ugis.service.impl.CTSC001BatchFreeSatelliteImageServiceImpl;
import ugis.service.impl.CTSC001BatchServiceImpl;
import ugis.service.vo.CTSC001VO;
import ugis.util.fileUtil;
import ugis.util.XmlParser;

@Controller
public class CTSC001Controller extends CTSCBaseController<CTSC001VO>{

	public static final String context = "/ctsc001";
	
	@Autowired
	private fileUtil fileUtil;
	
	@Resource(name="ctsc001Service")
	private CTSCBaseService<CTSC001VO> service;

	@Resource(name = "cmsc002Service")
	private CMSC002Service cmsc002Service;

	@Resource(name = "ctsc001BatchFreeSatelliteImageServiceImpl")
	CTSC001BatchFreeSatelliteImageServiceImpl batchFreeSatelliteImageService;

	@Override
	public CTSCBaseService<CTSC001VO> getService() {
		return service;
	}
	
	@RequestMapping(context + ".do")
	public String viewCtsc001(Model model) throws Exception {
		model.addAttribute("tab", "001");
		List<?> siList = null ;
		siList = cmsc002Service.selectSiList();
		model.addAttribute("siList",siList);

		return "ct" + context;
	}
	
	@RequestMapping(value= context + "/add.do")
	public ModelAndView addCtsc001(CTSC001VO vo) throws Exception {
		service.add(vo);
		Thread.sleep(1000);

		batchFreeSatelliteImageService.setCTSC001VO(vo);
		batchFreeSatelliteImageService.startDownload(vo);

		long insertcd = 1;
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("insertcd", insertcd);
		return mav;
	}
	
	@RequestMapping(context + "/upt.do")
	public @ResponseBody ResponseEntity<CTSC001VO> uptCtsc001(@RequestBody CTSC001VO vo) throws Exception {
		return super.upt(vo);
	}
	
	@RequestMapping(context + "/del.do")
	public @ResponseBody ResponseEntity<CTSC001VO> delCtsc001(@RequestBody CTSC001VO vo) throws Exception {
		return super.del(vo);
	}
	
	@RequestMapping(context + "/get.do")
	public @ResponseBody ResponseEntity<CTSC001VO> getCtsc001(@RequestBody CTSC001VO vo) throws Exception {
		return super.get(vo);
	}
	
	@RequestMapping(context + "/search.do")
	public @ResponseBody ResponseEntity<List<CTSC001VO>> searchCtsc001(@RequestParam Integer page, CTSC001VO vo) throws Exception {
		return super.search(page, vo);
	}
}
