package ugis.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ugis.cmmn.imgproc.GImageEnhancement;
import ugis.cmmn.imgproc.GImageProcessor;
import ugis.cmmn.imgproc.GTiffDataReader;
import ugis.cmmn.imgproc.data.GFileData;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

@Controller
public class CISC009Controller {
	@Resource(name = "fileProperties")
	private Properties fileProperties;
	/**
	 * 해당 영상 파일의 모자이크 정보를 가져 온다. 
	 * @param params
	 * {mosaic_vido=D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_B.tif,
	 *  mosaic_vido_input=D:\TestData\03_SatImage\kompsat\K3A_20200426044703_28082_00286238_L1R\K3A_20200426044703_28082_00286238_L1R_B.tif, 
	 *  vido_resul=test.tif}
	 * @param requestR
	 * @param model
	 * @return
	 * @throws Exception
	 */
	/*@RequestMapping(value="cisc009/mosaicResult.do",method = RequestMethod.POST, produces = "application/json; charset=utf8")
    @ResponseBody
    public String search(@RequestParam HashMap<String, Object> params,  HttpServletRequest request, ModelMap model) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> jsonMap = new HashMap<String, Object>();
        String input_vido =   (String)params.get("input_vod");

		GImageProcessor imgProcessor = new GImageProcessor();
		GTiffDataReader.ResamplingMethod resampleMethod = GTiffDataReader.ResamplingMethod.BILINEAR;
		GTiffDataReader.BIT16ToBIT8 inMaxBit16 = GTiffDataReader.BIT16ToBIT8.MAX_BIT16;
		GImageProcessor.ProcessCode procCode = GImageProcessor.ProcessCode.SUCCESS;
		ArrayList<GFileData> inFilePaths = new ArrayList<GFileData>();
		String outFilePath = "";
		GFileData gFileData = null;

		String mosaic_vido  = (String) params.get("mosaic_vido"); //기준영상
		String mosaic_vido_input  = (String) params.get("mosaic_vido_input"); //입력영상
		String vido_resul  = (String) params.get("vido_resul"); //결과영상
		System.out.println("Test_AutoMosaic : ");
		String file_path = fileProperties.getProperty("file.download.path");
		outFilePath = file_path +vido_resul;

		gFileData = new GFileData();
		gFileData._strFilePath = mosaic_vido;
		gFileData._maxBit16 = inMaxBit16;
		inFilePaths.add(gFileData);

		//Reference Image
		gFileData = new GFileData();
		gFileData._strFilePath = mosaic_vido_input;
		gFileData._maxBit16 = inMaxBit16;
		gFileData._isReferenced = true;
		inFilePaths.add(gFileData);

		*//*gFileData = new GFileData();
		gFileData._strFilePath = "E:\\3_rgb.tif";
		gFileData._maxBit16 = inMaxBit16;
		inFilePaths.add(gFileData);*//*


		procCode = imgProcessor.procSimpleMergedMosaic(inFilePaths, outFilePath, resampleMethod);
		System.out.println("procCode : " + procCode.toString());
		String outThumFilePath = fileProperties.getProperty("thumnail.path");


		String outThumFilePathName = outThumFilePath+vido_resul;
		GImageProcessor.ImageFormat outImgFormat = GImageProcessor.ImageFormat.IMG_PNG;
		int thumbnailWidth = 1000;
		GImageProcessor.ProcessCode thumprocCode = GImageProcessor.ProcessCode.SUCCESS;
		thumprocCode = imgProcessor.createThumbnailImage(vido_resul,inMaxBit16, outThumFilePathName, outImgFormat, thumbnailWidth,resampleMethod);
		int lastIdxtemp = vido_resul.lastIndexOf(".");
		String outputNametemp = vido_resul.substring(0, lastIdxtemp);
		inFilePaths.clear();
		inFilePaths = null;
		jsonMap.put("procCode", procCode);
		jsonMap.put("fileName", outputNametemp+".png");
        return mapper.writeValueAsString(jsonMap);
	}*/
	
	


}
