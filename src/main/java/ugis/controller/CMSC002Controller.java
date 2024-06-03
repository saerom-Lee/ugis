package ugis.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.GTiffDataReader.ResamplingMethod;
import ugis.service.CMSC002Service;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * @Class Name : CMSC002Controller.java
 * @Description : 국토위성영상 시뮬레이터
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
public class CMSC002Controller {

	// 서비스
	@Resource(name = "cmsc002Service")
	private CMSC002Service cmsc002Service;
	GImageProcessor imgProcessor = new GImageProcessor();
	
	@Resource(name = "fileProperties")
	private Properties fileProperties;
	/**
	 * 초기 화면 표시
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value = "/cmsc002.do")
	public String cmsc002(ModelMap model) throws Exception {
		List<?> siList = null ;
		siList = cmsc002Service.selectSiList();
		model.addAttribute("siList",siList);
		return "cm/cmsc002";
	}

    @RequestMapping(value="cmsc002/sgg.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public String sgg(@RequestParam HashMap<String, Object> params, HttpServletRequest request, ModelMap model) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonMap = new HashMap<String, Object>();
        List<?> sggList = null ;
		String si = (String) params.get("si");
        sggList  =  cmsc002Service.selectSggList(si);	
        jsonMap.put("sggList", sggList);
        return mapper.writeValueAsString(jsonMap);
    }
	@RequestMapping(value="cmsc002/emd.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String emd(@RequestParam HashMap<String, Object> params, HttpServletRequest request, ModelMap model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		List<?> sggList = null ;
		String sgg = (String) params.get("sgg");
		sggList  =  cmsc002Service.selectEmdList(sgg);
		jsonMap.put("sggList", sggList);
		return mapper.writeValueAsString(jsonMap);
	}

	@RequestMapping(value="cmsc002/simulResult.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String Result(@RequestParam HashMap<String, Object> params, HttpServletRequest request, ModelMap model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();
		 String filName = (String) params.get("result_video"); 
		String file_path =fileProperties.getProperty("file.download.path");
		String outThumFilePath = fileProperties.getProperty("thumnail.path");
		//String input_video = file_path + params.get("input_video"); //입력영상
		String input_video = (String) params.get("input_video"); //입력영상
		String wavelength = (String)params.get("wavelength"); //파장대역
		double angle = Double.valueOf((String) params.get("angle")); //각도
		String outFilePath = file_path + params.get("result_video");
		//String outFilePath = (String) params.get("result_video");
		String rearrangement  = (String)params.get("rearrangement"); //영상재배열
		
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		String inFilePath = input_video;
		double outRes = 2;
		int outGridWidth = 6025;
		int outGridHeight = 4380;
		if (wavelength.equals("red")||wavelength.equals("green")||wavelength.equals("blue")||wavelength.equals("nir"))
		{
			outRes = 2 ;
			outGridWidth = 6025;
			outGridHeight = 4380;
		}
		else{
			outRes = 0.5 ;
			outGridWidth = 24060;
			outGridHeight = 17520;
		}
		switch(rearrangement) {
			case "Neighbor": resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR ;
			break;
			case "Bilinear": resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR ;
			break;
			case "Convolution": resampleMethod = GTiffDataReader.ResamplingMethod.CUBIC_CONVOLUTION;
			break; 
		}
		//가상시뮬레이터 실행.
		
		GImageProcessor.ProcessCode  procCode = imgProcessor.procVirtualImageSimulation(inFilePath, inMaxBit16, outFilePath, outRes, outGridWidth, outGridHeight, angle, resampleMethod);
		jsonMap.put("procCode", procCode);
		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		
		int thumbnailWidth = 1000;
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		
		
		
		
	    int lastIdxtemp = outFilePath.lastIndexOf(".");
        String outputNametemp = outFilePath.substring(0, lastIdxtemp);
//        int outtemp   = outputNametemp.lastIndexOf("\\\\");
//        String tempname  = outputNametemp.substring(outtemp, lastIdxtemp);
       
        String outThumFilePathName = outThumFilePath+outputNametemp+".tif";
		thumprocCode = imgProcessor.createThumbnailImage(outFilePath, inMaxBit16 , outFilePath, outImgFormat, thumbnailWidth,resampleMethod);
		int lastIdx = filName.lastIndexOf(".");
		String outputName = filName.substring(0, lastIdx);
		jsonMap.put("thumprocCode", thumprocCode);
		jsonMap.put("fileName", outputName+".png");
		return mapper.writeValueAsString(jsonMap);
	}

		
}
