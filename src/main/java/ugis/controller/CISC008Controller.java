package ugis.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;

@Controller
public class CISC008Controller {
	@Resource(name = "fileProperties")
	private Properties fileProperties;
	GImageProcessor imgProcessor = new GImageProcessor();
	/**
	 * 해당 영상 파일의 히소토그램 정보를 가져 온다. 
	 * @param params
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="cisc008/histResult.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public String search(@RequestParam HashMap<String, Object> params,  HttpServletRequest request, ModelMap model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonMap = new HashMap<String, Object>();
        String input_vido =   (String)params.get("input_vod");
        
        GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GTiffDataReader gdReader = null;    
		int[] histgR = new int[256];
		int[] histgG = new int[256];
		int[] histgB = new int[256];
		int i=0;		
		try{
			gdReader = new GTiffDataReader(input_vido, maxBit16);
			gdReader.getHistogram(histgR, histgG, histgB);
		
		}catch(Exception ex){
			System.out.println("Test_Histogram : " + ex.toString()); 
		}
		jsonMap.put("histgR", histgR);
		jsonMap.put("histgG", histgG);
		jsonMap.put("histgB", histgB);
        return mapper.writeValueAsString(jsonMap);
	}
	
	

	
	/**
	 * 해당 영상 파일의 히소토그램 을 처리한다. 정보를 가져 온다. 
	 * @param params
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="cisc008/histAuto.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public String Auto(@RequestParam HashMap<String, Object> params,@RequestParam(value="histo[]") List<String> histo,  HttpServletRequest request, ModelMap model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonMap = new HashMap<String, Object>();
        
        String input_vido =   (String)params.get("input_vido");
        String result_vido =  (String)params.get("result_vido");
        String aligorithm = (String)params.get("algorithm");
        Boolean isApplyBlueBand = true;
        String auto = (String)params.get("Auto");

		String outThumFilePath = fileProperties.getProperty("thumnail.path");
        
		GTiffDataReader.BIT16ToBIT8 maxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GImageEnhancement imgEnhance = new GImageEnhancement();
		GTiffDataReader gdReader = null;    
		int[] histgR = new int[256];
		int[] histgG = new int[256];
		int[] histgB = new int[256];
		int[] stretchingMin = new int[3];
		int[] stretchingMax = new int[3];
		int i=0;
		try{
			//gdReader = new GTiffDataReader(input_vido, maxBit16);
			gdReader.getHistogram(histgR, histgG, histgB);
			
			System.out.println("Histogram : ");
			System.out.println("\tPixel \tR \tG \tB");
			for(i=0; i<256; i++) {
				System.out.println("\t" + i + " \t" + histgR[i] + " \t" + histgG[i] + " \t" + histgB[i]);
			}
			if(auto.equals("auto")) {
				//자동 Min, Max 검색
				imgEnhance.calcAutoHistogramMinMax(histgR, histgG, histgB, stretchingMin, stretchingMax);
				System.out.println("\t (Geneal) Min : \t" + stretchingMin[0] + " \t" + stretchingMin[1] + " \t" + stretchingMin[2]);
				System.out.println("\t (Geneal) Max : \t" + stretchingMax[0] + " \t" + stretchingMax[1] + " \t" + stretchingMax[2]);
			}
			else {
			//1 Sigma 기준 자동 Min, Max 검색
			imgEnhance.calcAutoHistogramMinMaxByLinearSigma(GImageEnhancement.LinearSigma.LINEAR_1SIGMA, histgR, histgG, histgB, stretchingMin, stretchingMax);
			System.out.println("\t (1 Sigma) Min : \t" + stretchingMin[0] + " \t" + stretchingMin[1] + " \t" + stretchingMin[2]);
			System.out.println("\t (1 Sigma) Max : \t" + stretchingMax[0] + " \t" + stretchingMax[1] + " \t" + stretchingMax[2]);
			}
		}
		catch(IndexOutOfBoundsException ex){
			//System.out.println("Test_Histogram : " + ex.toString());
		}
		catch(Exception ex){
			//System.out.println("Test_Histogram : " + ex.toString());
		}

//		System.out.println("procCode : " + procCode.toString());
		jsonMap.put("stretchingMin", stretchingMin);
		jsonMap.put("stretchingMax", stretchingMax);
        return mapper.writeValueAsString(jsonMap);
	}
}
