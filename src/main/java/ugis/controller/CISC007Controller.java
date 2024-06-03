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

import egovframework.rte.psl.dataaccess.util.EgovMap;
import ugis.service.CISC007Service;
import ugis.service.CMSC003Service;
import ugis.service.vo.CMSC003VO3;

@Controller
public class CISC007Controller {

	@Resource(name = "cisc007Service")
	CISC007Service cisc007Service;

	@Resource(name = "cmsc003Service")
	private CMSC003Service cmsc003Service;

	@Resource(name = "fileProperties")
	private Properties fileProperties;

	/*
	 * @Resource(name = "procColorComposite") private GImageProcessor
	 * gImageProcessor;
	 */

	/**
	 * 초기 화면 표시
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc007.do")
	public String cmsc001(ModelMap model) throws Exception {

		return "ci/cisc007";
	}

	@RequestMapping(value = "cisc007/colorSearch.do", method = RequestMethod.POST, produces = "application/json; charset=utf8")
	@ResponseBody
	public String search(@RequestParam HashMap<String, Object> params,
			@RequestParam(value = "checkArr[]") List<String> checkArr, HttpServletRequest request, ModelMap model)
			throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		HashMap<String, Object> jsonMap = new HashMap<String, Object>();

		List<EgovMap> aeroList = null; // 기타위성
		List<EgovMap> soilList = null; // 국토위성
		List<EgovMap> usgsAirList = null; // 항공영상
		List<EgovMap> usgsDroneList = null; // 드론영상

		boolean etc = false;
		boolean soil = false;
		boolean air = false;
		boolean drone = false;

		if (checkArr.contains("etc") || checkArr.contains("etc1") || checkArr.contains("etc2")) {
			etc = true;
		}
		if (checkArr.contains("soil") || checkArr.contains("soil1") || checkArr.contains("soil2")) {
			soil = true;
		}
		if (checkArr.contains("air1") || checkArr.contains("air2")) {
			air = true;
			params.put("air", "air");
		}
		if (checkArr.contains("drone1") || checkArr.contains("drone2")) {
			drone = true;
			params.put("drone", "drone");
		}

		if (etc) { // 기타위성
			aeroList = cisc007Service.selectEtcList(params);
		}
		if (soil) { // 국토위성
			soilList = cisc007Service.selectSoilList(params);
		}
		if (air) { // 항공,정사영상
			// usgsAirList = cisc007Service.selectUsgsAirList(params);
			CMSC003VO3 cmsc003vo3 = new CMSC003VO3();
			if (params.get("disasterId") != null) {
				cmsc003vo3.setMsfrtnInfoColctRequstId(Long.valueOf(params.get("disasterId").toString()));
			}
			cmsc003vo3.setDateFrom((String) params.get("date1"));
			cmsc003vo3.setDateTo((String) params.get("date2"));
			cmsc003vo3.setDataKind(Integer.valueOf(params.get("dataKind").toString()));
			usgsAirList = cmsc003Service.selectDatasetByDisasterId(cmsc003vo3);
		}
		if (drone) { // 드론영상
			usgsDroneList = cisc007Service.selectUsgsDroneList(params);
		}
		jsonMap.put("AeroList", aeroList);
		jsonMap.put("soilList", soilList);
		jsonMap.put("airList", usgsAirList);
		jsonMap.put("droneList", usgsDroneList);
		return mapper.writeValueAsString(jsonMap);
	}

	//
	// @RequestMapping(value="cisc007/colorPerform.do",method = RequestMethod.POST,
	// produces = "application/json; charset=utf8")
	// @ResponseBody
	// public String Perform(@RequestParam HashMap<String, Object> params,
	// HttpServletRequest request, ModelMap model) throws Exception {
	// ObjectMapper mapper = new ObjectMapper();
	// HashMap<String, Object> jsonMap = new HashMap<String, Object>();
	// List<?> PerformList = null ; //수행 검색 리스트.
	//
	//
	//
	// PerformList = cisc007Service.selectUsgsList(params);
	// jsonMap.put("AeroList", AeroList);
	//
	// return mapper.writeValueAsString(PerformList);
	// }

	/*
	 * @RequestMapping(value="cisc007/colorResult.do",method = RequestMethod.POST,
	 * produces = "application/json; charset=utf8")
	 * 
	 * @ResponseBody public String Result(@RequestParam HashMap<String, Object>
	 * params, HttpServletRequest request, ModelMap model) throws Exception {
	 * ObjectMapper mapper = new ObjectMapper(); HashMap<String, Object> jsonMap =
	 * new HashMap<String, Object>(); String filName = (String) params.get("name");
	 * //String file_path ="d:\\test\\"; String file_path =
	 * fileProperties.getProperty("file.download.path");
	 * 
	 * GImageProcessor imgProcessor = new GImageProcessor();
	 * 
	 * GTiffDataReader.BIT16ToBIT8 inMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; GTiffDataReader.ResamplingMethod
	 * resampleMethod = GTiffDataReader.ResamplingMethod.NEAREST_NEIGHBOR; //String
	 * outThumFilePath = "C:\\ugis\\src\\main\\webapp\\img\\thumnail\\"; String
	 * outThumFilePath = fileProperties.getProperty("thumnail.path"); //String
	 * outThumFilePath=
	 * "C:\\Users\\kjhg\\eclipse-workspace_new\\ugis\\src\\main\\webapp\\img\\thumnail\\";
	 * 
	 * String inRedFilePath = (String) params.get("red"); String inGreenFilePath =
	 * (String) params.get("green"); String inBlueFilePath = (String)
	 * params.get("blue"); String outFilePath = outThumFilePath+(String)
	 * params.get("name"); GTiffDataReader.BIT16ToBIT8 inRedMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; GTiffDataReader.BIT16ToBIT8
	 * inGreenMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
	 * GTiffDataReader.BIT16ToBIT8 inBlueMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; GImageProcessor.ProcessCode procCode =
	 * imgProcessor.procColorComposite(inRedFilePath, inRedMaxBit16,
	 * inGreenFilePath, inGreenMaxBit16, inBlueFilePath, inBlueMaxBit16,
	 * outFilePath) ; jsonMap.put("procCode", procCode); GImageProcessor.ProcessCode
	 * thumprocCode = GImageProcessor.ProcessCode.SUCCESS; String
	 * outThumFilePathName = outThumFilePath+params.get("name"); int thumbnailWidth
	 * = 1000; GImageProcessor.ImageFormat outImgFormat =
	 * GImageProcessor.ImageFormat.IMG_PNG; inMaxBit16 =
	 * GTiffDataReader.BIT16ToBIT8.MAX_BIT16; thumprocCode =
	 * imgProcessor.createThumbnailImage(outFilePath, inMaxBit16 ,
	 * outThumFilePathName, outImgFormat, thumbnailWidth,resampleMethod);
	 * 
	 * int lastIdx = filName.lastIndexOf("."); String outputName =
	 * filName.substring(0, lastIdx);
	 * 
	 * jsonMap.put("thumprocCode", thumprocCode); jsonMap.put("fileName",
	 * outputName+".png");
	 * 
	 * return mapper.writeValueAsString(jsonMap); }
	 */

}
