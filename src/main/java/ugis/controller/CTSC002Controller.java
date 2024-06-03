package ugis.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.opengis.geometry.DirectPosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.service.CMSC002Service;
import ugis.service.CTSC002Service;
import ugis.service.CTSCBaseService;
import ugis.service.vo.CTSC002VO;
import ugis.util.fileUtil;

@Controller
public class CTSC002Controller extends CTSCBaseController<CTSC002VO>{

	public static final String context = "/ctsc002";
	@Autowired
	private ugis.util.fileUtil fileUtil;



	@Resource(name="ctsc002Service")
	private CTSC002Service<CTSC002VO> service;


	@Resource(name = "cmsc002Service")
	private CMSC002Service cmsc002Service;

	
	@RequestMapping(context + ".do")
	public String viewCtsc001(Model model) throws Exception {
		model.addAttribute("tab", "002");
		List<?> siList = null ;
		siList = cmsc002Service.selectSiList();
		model.addAttribute("siList",siList);
		return "ct" + context;
	}

	@RequestMapping(value= context + "/add.do")
	public ModelAndView addCtsc002(CTSC002VO vo) throws Exception {
		/**
		 * 1. 파일 저장
		 * 2. 압축 풀기 
		 * 3. 썸네일 만들기
		 * 4. 데이터 저장
		 */
		/*1. 파일 저장*/
		GTiffDataReader gdReader = null;
		String extpath = vo.getSatlitVidoExtrlPath();
		String innerpath = vo.getSatlitVidoInnerPath();
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		String filenames[]=  fileUtil.FileNames(extpath);
		GImageProcessor.ProcessCode ret = GImageProcessor.ProcessCode.SUCCESS;
		String srcFile="";
		String dstFile="";
		//영상정보의 갯수만큼 파일을 이동한다.
		if(filenames.length>0) {
			for (int i = 0; i < filenames.length; i++) {
				srcFile = extpath + filenames[i];
				dstFile = innerpath + filenames[i];
				File src = new File(srcFile);
				File dst = new File(dstFile);
				FileUtils.moveFile(src, dst);
			}
			try {
				String DATA_DIRECTORY = innerpath;
				File dir = new File(DATA_DIRECTORY);
				String[] filelist = dir.list();
				String filenametemp = "";

				for (String tempfile : filelist) {

					int j = tempfile.lastIndexOf('.');
					if (j > 0) {
						filenametemp = tempfile.substring(j + 1);
					}
					try {
						if ("tif".equals(filenametemp) || "TIF".equals(filenametemp)) {
							gdReader = new GTiffDataReader(innerpath+tempfile, inMaxBit16);
							double[] lower = gdReader.getEnvelope().getLowerCorner().getCoordinate();
							double[] upper = gdReader.getEnvelope().getUpperCorner().getCoordinate();
							vo.setLtcr_x(lower[0]);
							vo.setLtcr_y(lower[1]);
							vo.setRtcr_x(upper[0]);
							vo.setRtcr_y(lower[1]);
							vo.setSatlitVidoInnerPath(innerpath+tempfile);

							service.add(vo);

						}
					} catch (Exception ex) {
						System.out.println("GImageProcessor.procVirtualImageSimulation : " + ex.toString());
						ret = GImageProcessor.ProcessCode.ERROR_FAIL_READ;
						if (gdReader != null) gdReader.destory();
						gdReader = null;
					}
				}
			}
			catch (Exception e){

			}
		}

		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("ret", ret);
		return mav;
	}

	//@RequestMapping(context + "/get.do")
	//public @ResponseBody ResponseEntity<CTSC002VO> getCtsc002( CTSC002VO vo) throws Exception {
//		public ModelAndView getCtsc002(CTSC002VO vo) throws Exception {
	@RequestMapping(value=context +"/get.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String getCtsc002(CTSC002VO vo) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		 List<CTSC002VO> C002search = service.ctsc002_search(vo);

		///mav.addObject("result", C002search);
		jsonMap.put("soilList", C002search);
		return mapper.writeValueAsString(jsonMap);
	//	return mav;
	}
	
	@RequestMapping(context + "/upt.do")
	public @ResponseBody ResponseEntity<CTSC002VO> uptCtsc002(@RequestBody CTSC002VO vo) throws Exception {
		return super.upt(vo);
	}
	
	@RequestMapping(context + "/del.do")
	public @ResponseBody ResponseEntity<CTSC002VO> delCtsc002(@RequestBody CTSC002VO vo) throws Exception {
		return super.del(vo);
	}
	

	
	@RequestMapping(context + "/search.do")
	public @ResponseBody ResponseEntity<List<CTSC002VO>> searchCtsc002(@RequestParam Integer page, CTSC002VO vo) throws Exception {
		return super.search(page, vo);
	}

	@Override
	public CTSCBaseService<CTSC002VO> getService() {
		return null;
	}
}
