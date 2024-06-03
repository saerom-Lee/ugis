package ugis.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ugis.service.CISC001Service;
import ugis.service.vo.CISC001AlgorithmVO;
import ugis.service.vo.CISC001ProjectLogVO;
import ugis.service.vo.CISC001ProjectVO;
import ugis.service.vo.CISC001ScriptVO;


@Controller
public class CISC001Controller {

    @Resource(name = "cisc001Service")
    CISC001Service cisc001Service;
    
    @Resource(name = "fileProperties")
	private Properties fileProperties;
    
    /**
     * 프로젝트 관리 메인화면
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cisc001.do")
    public String cisc001(ModelMap model) throws Exception {

    	SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd"); //yyyyMMddhhmmssSSS
		String time = sDate.format(new Date());
		model.addAttribute("schDate", time);
        return "ci/cisc001_3";
    }
    
    @RequestMapping(value = "/cisc001/createProject.do")
	public ModelAndView createProject(@ModelAttribute("paramVO") CISC001ProjectVO cisc001projectVO) throws Exception {
    	
    	int result = cisc001Service.insertProject(cisc001projectVO);    	
    	
    	Map<String, Object> resData = new HashMap<String, Object>();
    	resData.put("result", result);
    	ModelAndView mav = new ModelAndView("jsonView", resData);
        return mav;
	}
    
    @RequestMapping(value = "/cisc001/updateProject.do")
   	public ModelAndView updateProject(@ModelAttribute("paramVO") CISC001ProjectVO cisc001projectVO) throws Exception {
       	
       	int result = cisc001Service.updateProject(cisc001projectVO);    	
       	
       	Map<String, Object> resData = new HashMap<String, Object>();
       	resData.put("result", result);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
           return mav;
   	}
       
    
    
    @RequestMapping(value = "/cisc001/searchProject.do")
   	public ModelAndView searchProject(@ModelAttribute("paramVO") CISC001ProjectVO cisc001projectVO) throws Exception {
       	
        Map<String, Object> resultMap = new HashMap<String, Object>();

   		List<CISC001ProjectVO> projectList =  cisc001Service.selectProjectList(cisc001projectVO);
   		resultMap.put("resultList", projectList);
   		ModelAndView mav = new ModelAndView("jsonView", resultMap);
   		return mav;   
   	}
    
    @RequestMapping(value = "/cisc001/searchProjectLog.do")
   	public ModelAndView searchProjectLog(@ModelAttribute("paramVO") CISC001ProjectLogVO cisc001projectLogVO) throws Exception {
       	
        Map<String, Object> resultMap = new HashMap<String, Object>();

   		List<CISC001ProjectLogVO> projectLogList =  cisc001Service.selectProjectLogList(cisc001projectLogVO);
   		resultMap.put("resultList", projectLogList);
   		ModelAndView mav = new ModelAndView("jsonView", resultMap);
   		return mav;   
           
   	}
    
    @RequestMapping(value = "/cisc001/setProjectKey.do")
   	public ModelAndView setProject(@ModelAttribute("paramVO") CISC001ProjectVO projectVO, HttpServletRequest request) throws Exception {
       	
        HttpSession session = request.getSession();
        session.setAttribute("projectKey", projectVO.getProjectId());
        Map<String, Object> resData = new HashMap<String, Object>();
    	resData.put("result", "OK");
    	ModelAndView mav = new ModelAndView("jsonView", resData);
   		return mav;   
   	}
    
    @RequestMapping(value = "/cisc001/deleteProjectLog.do")
   	public ModelAndView deleteProjectLog(@ModelAttribute("paramVO") CISC001ProjectLogVO cisc001projectLogVO) throws Exception {
       	
       	int result = cisc001Service.deleteProjectLog(cisc001projectLogVO);    	
       	
       	Map<String, Object> resData = new HashMap<String, Object>();
       	resData.put("result", result);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
           return mav;
   	}
    
    
    @RequestMapping(value = "/cisc001/searchAlgorithmRegList.do")
   	public ModelAndView searchAlgorithmRegList(@ModelAttribute("paramVO") CISC001AlgorithmVO cisc001algorithmVO) throws Exception {
       	
        Map<String, Object> resultMap = new HashMap<String, Object>();

   		List<CISC001AlgorithmVO> algorithmRegList =  cisc001Service.selectAlgorithmRegList(cisc001algorithmVO);
   		resultMap.put("resultList", algorithmRegList);
   		ModelAndView mav = new ModelAndView("jsonView", resultMap);
   		return mav;   
           
   	}
    
    @RequestMapping(value = "/cisc001/insertAlgorithm.do")
   	public ModelAndView insertAlgorithm(@ModelAttribute("paramVO") CISC001AlgorithmVO cisc001algorithmVO) throws Exception {
       	
       	int result = cisc001Service.insertAlgorithm(cisc001algorithmVO);    	
       	
       	Map<String, Object> resData = new HashMap<String, Object>();
       	resData.put("result", result);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
           return mav;
   	}
    
    @RequestMapping(value = "/cisc001/updateAlgorithm.do")
   	public ModelAndView updateAlgorithm(@ModelAttribute("paramVO") CISC001AlgorithmVO cisc001algorithmVO) throws Exception {
       	
       	int result = cisc001Service.updateAlgorithm(cisc001algorithmVO);    	
       	
       	Map<String, Object> resData = new HashMap<String, Object>();
       	resData.put("result", result);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
           return mav;
   	}
    
    @RequestMapping(value = "/cisc001/searchAlgorithmList.do")
   	public ModelAndView searchAlgorithmList(@ModelAttribute("paramVO") CISC001AlgorithmVO cisc001algorithmVO) throws Exception {
       	
        Map<String, Object> resultMap = new HashMap<String, Object>();

   		List<CISC001AlgorithmVO> algorithmList =  cisc001Service.selectAlgorithmList(cisc001algorithmVO);
   		resultMap.put("resultList", algorithmList);
   		ModelAndView mav = new ModelAndView("jsonView", resultMap);
   		return mav;   
           
   	}
   
    @RequestMapping(value = "/cisc001/createUpdateScript.do")
   	public ModelAndView createScript(@ModelAttribute("paramVO") CISC001ScriptVO scriptVO, HttpServletRequest request) throws Exception {
    	String scriptId = scriptVO.getScriptId();
    	String scriptName = scriptVO.getScriptNm();
    	String workKind = scriptVO.getWorkKind();
    	String satKind = scriptVO.getSatKind();
    	String metaDataNm = scriptVO.getMetaDataNm();
    	String algorithmNm = scriptVO.getAlgorithmNm();
    	String inputFileNm = scriptVO.getInputFileNm();
    	String inputFileNm2 = scriptVO.getInputFileNm2();
    	String inputFileNm3 = scriptVO.getInputFileNm3();
    	String inputFileNm4 = scriptVO.getInputFileNm4();    	
    	String outputFileNm = scriptVO.getOutputFileNm();
    	String targetFileNm = scriptVO.getTargetFileNm();
    	String toaOutputFileNm = scriptVO.getToaOutputFileNm();
    	String gain = scriptVO.getGain();
    	String offset = scriptVO.getOffset();
    	String radianceMultiple = scriptVO.getGain();
    	String radianceAddtion = scriptVO.getOffset();
    	String reflectanceMultiple = scriptVO.getReflectGain();
    	String reflectanceAddtion = scriptVO.getReflectOffset();
    	String radiatingFormula = "Radiance = "+scriptVO.getGain()+" X DN + "+scriptVO.getOffset();
    	String redBand = scriptVO.getRedBand();
    	String greenBand = scriptVO.getGreenBand();
    	String blueBand = scriptVO.getBlueBand();
    	String controlType = scriptVO.getControlType();
    	String autoAreaControl = scriptVO.getAutoAreaControl();
    	String histogramArea = scriptVO.getHistogramArea();
    	
    	String url = fileProperties.getProperty("script.server.url");
    	
    	Map<String, Object> resData = new HashMap<String, Object>();    	
    	if (scriptId == null) {
    		scriptId = "";
    	}
    	
    	if (url == null || url.equals("")) {
    		url = "http://localhost:8080/";
    	}
    	
    	String body = "";
    	
    	if (System.getProperty("os.name").indexOf("Windows") > -1) {  //window는  테스트용으로 사용함...
    		url = fileProperties.getProperty("script.local.url");
    		File dir = new File("D:\\\\shell\\\\");
	        if(!dir.isDirectory()){
	        	dir.mkdirs();
	        }
	        scriptName = scriptName.replaceAll("(?i).bat", "")+".bat";
	        scriptVO.setExtrlFileCoursNm(dir.getPath()+"\\"+scriptName);
	        if (outputFileNm.lastIndexOf("\\") < 0) {  //outFile에 경로가 없으면
	        	scriptVO.setOutputFileNm(inputFileNm.substring(0,inputFileNm.lastIndexOf("\\")+1)+outputFileNm);
	        }	        
	        
	        
	        if (workKind.equals("2")) { //절대방사보정
	        	File file = new File("D:\\\\shell\\"+scriptName);
        		body = "curl -X POST ";
        		body += "--data \"satKind="+satKind;
        		body += "&inFilePath="+inputFileNm;
        		body += "&outRadianceFilePath="+outputFileNm;
        		body += "&metaData="+metaDataNm;
        		body += "&gain="+gain;
        		body += "&offset="+offset;
        		if (satKind.equals("1")) {  //landSat
        			body += "&RadianceMultiple="+radianceMultiple+"\\\n";
        			body += "&RadianceAddtion="+radianceAddtion+"\\\n";
        			body += "&ReflectanceMultiple="+reflectanceMultiple+"\\\n";
        			body += "&ReflectanceAddtion="+reflectanceAddtion+"\\\n";
        			body += "&TOAReflectanceFilePath="+toaOutputFileNm+"\\\n";
        		}        		
        		body += "&radiatingFormula="+radiatingFormula+"\" ";
        		body += url+"cisc005/absoluteRadiatingCorrection.do";
        		try {
        			FileWriter fw = new FileWriter(file);
        			fw.write(body);
        			fw.close();
        			
        			/*int result = cisc001Service.insertScript(scriptVO);    				
    				resData.put("result", result);*/
        			
        			
        		}catch(IOException ie) {
        			ie.printStackTrace();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
	        } else if (workKind.equals("3")) { //상대방사보정
	    		File file = new File("D:\\\\shell\\"+scriptName);
	    		body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"algorithm="+algorithmNm+"\\\n";
	    		body += "&histogramArea="+histogramArea+"\\\n";
	    		body += "&srcFilePath="+inputFileNm+"\\\n";
	    		body += "&trgFilePath="+targetFileNm+"\\\n";
	    		body += "&outFilePath="+outputFileNm+"\" \\\n";
	    		body += url+"cisc005/relativeRadiatingCorrection.do)\n";
        		try {
        			FileWriter fw = new FileWriter(file);
        			fw.write(body);
        			fw.close();
        		}catch(IOException ie) {
        			ie.printStackTrace();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
	        }
	        if (scriptId.equals("")) {
	        	
	        	
	        	int result = cisc001Service.insertScript(scriptVO);
	        	resData.put("result", result);
	        }else {
	        	int result = cisc001Service.updateScript(scriptVO);
				resData.put("result", result);
	        }
	        
	        
    	}else {  //리눅스...
    		File dir = new File("/shellscript");
   		
	        if(!dir.isDirectory()){
	        	dir.mkdirs();
	        }
	        scriptName = scriptName.replaceAll("(?i).sh", "")+".sh";
	        File shellFile = new File("/shellscript/"+scriptName);
	        
	        if (outputFileNm.lastIndexOf("/") < 0) {  //outFile에 경로가 없으면
	        	scriptVO.setOutputFileNm(inputFileNm.substring(0,inputFileNm.lastIndexOf("/")+1)+outputFileNm);
	        	outputFileNm = inputFileNm.substring(0,inputFileNm.lastIndexOf("/")+1)+outputFileNm;
	        }
	        
	    	//삭제
	        if (shellFile.exists()) {
	    		if (scriptId.equals("")) {  //입력
	    			resData.put("result", -1);
	    			resData.put("msg", "["+scriptName+"]이 이미 존재 합니다. 다른이름을 입력하세요");
	    			ModelAndView mav = new ModelAndView("jsonView", resData);
	    	   		return mav;

	    		}else {  //수정
	    			File orgFile = new File(scriptVO.getExtrlFileCoursNm());
	    	        if (!shellFile.getName().equals(orgFile.getName())) {  //파일명칭이 다른 경우
	    	        	resData.put("result", -1);
		    			resData.put("msg", "["+scriptName+"]이 이미 존재 합니다. 다른이름을 입력하세요");
		    			ModelAndView mav = new ModelAndView("jsonView", resData);
		    	   		return mav;
	    	        }
	    			
	    			if(execute("hadoop fs -rm /user/oozie/shellscript/"+shellFile.getName())) {
		    			shellFile.delete();
		    		}
	    		}
	    	}else {  //파일이 없는경우 인데 수정인 경우는 기존 파일 삭제
	    		if (!scriptId.equals("")) {
	    			File orgFile = new File(scriptVO.getExtrlFileCoursNm());
	    			if(execute("hadoop fs -rm /user/oozie/shellscript/"+orgFile.getName())) {
	    				orgFile.delete();
		    		}
	    		}
	    	}
	        System.out.println("workKind : " +workKind);
	        
	        if(workKind.equals("1")) {  //상대대기보정
	        	body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"algorithm="+algorithmNm+"\\\n";
	    		body += "&innerFileCoursNm="+inputFileNm+"\\\n";
	    		body += "&outputpath="+outputFileNm+"\" \\\n";
	    		body += url+"cisc004/AtmosphereResult.do)\n";
	        }else if (workKind.equals("2")) {  //절대방사보정
		        body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"satKind="+satKind+"\\\n";
	    		body += "&inFilePath="+inputFileNm+"\\\n";
	    		body += "&outRadianceFilePath="+outputFileNm+"\\\n";
	    		body += "&metaData="+metaDataNm+"\\\n";
	    		body += "&gain="+gain+"\\\n";
	    		body += "&offset="+offset+"\\\n";
	    		if (satKind.equals("1")) {  //landSat
	    			body += "&RadianceMultiple="+radianceMultiple+"\\\n";
	    			body += "&RadianceAddtion="+radianceAddtion+"\\\n";
	    			body += "&ReflectanceMultiple="+reflectanceMultiple+"\\\n";
	    			body += "&ReflectanceAddtion="+reflectanceAddtion+"\\\n";
	    			body += "&TOAReflectanceFilePath="+toaOutputFileNm+"\\\n";
	    		}
	    		body += "&radiatingFormula="+radiatingFormula+"\" \\\n";
	    		body += url+"cisc005/absoluteRadiatingCorrection.do)\n";
	        }else if (workKind.equals("3")) {  //상대방사보정
	        	body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"algorithm="+algorithmNm+"\\\n";
	    		body += "&histogramArea="+histogramArea+"\\\n";
	    		body += "&srcFilePath="+inputFileNm+"\\\n";
	    		body += "&trgFilePath="+targetFileNm+"\\\n";
	    		body += "&outFilePath="+outputFileNm+"\" \\\n";
	    		body += url+"cisc005/relativeRadiatingCorrection.do)\n";
	        	
	        }else if (workKind.equals("4")) {  //컬러합성
	        	body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"red="+redBand+"\\\n";
	    		body += "&green="+greenBand+"\\\n";
	    		body += "&blue="+blueBand+"\\\n";
	    		body += "&name="+outputFileNm+"\" \\\n";
	    		body += url+"cisc007/colorResult.do)\n";
	        	
	        }else if (workKind.equals("5")) {  //히스토그램조정
	        	body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"input_vido="+inputFileNm+"\\\n";
	    		body += "&result_vido="+outputFileNm+"\\\n";
	    		body += "&auto="+"true"+"\\\n";
	    		body += "&histo[]=[]\\\n";
	    		//body += "&controlType="+controlType+"\\\n";
	    		//body += "&autoAreaControl="+autoAreaControl+"\\\n";	    		
	    		body += "&algorithm="+algorithmNm+"\" \\\n";	    		
	    		body += url+"cisc008/histcompl.do)\n";
	        }else {//if (workKind.equals("6")) {  //모자이크
	        	body = "#!/bin/bash\n\n\n";
	    		body += "RESPONSE=$(curl -X POST \\\n";
	    		body += "--data \"mosaic_vido="+targetFileNm+"\\\n";
	    		body += "--data \"mosaic_vido_input="+inputFileNm+"\\\n";
	    		body += "--data \"mosaic_vido_input2="+inputFileNm2+"\\\n";
	    		body += "--data \"mosaic_vido_input3="+inputFileNm3+"\\\n";
	    		body += "--data \"mosaic_vido_input4="+inputFileNm4+"\\\n";
	    		body += "&vido_resul="+outputFileNm+"\" \\\n";
	    		body += url+"cisc009/mosaicResult.do)\n";
	        }

	        //System.out.println("body : "+body);
    		try {
    			FileWriter fw = new FileWriter(shellFile);
    			fw.write(body);
    			fw.close();
    			
    			File f = new File("/shellscript/"+scriptName);
    			int wait = 0;
    			while(true) {
	    			if (f.exists() || wait > 9) {
	    				break;
	    			}else {
	    				Thread.sleep(100);
	    			}
	    			wait++;
    			}
    			boolean bResult = execute("chmod +x /shellscript/"+scriptName+" && hadoop fs -copyFromLocal /shellscript/"+scriptName+" /user/oozie/shellscript");
    			if (bResult) {  //쉘이 정상적으로 만들어 졌으면 디비 처리
    				scriptVO.setExtrlFileCoursNm("/shellscript/"+scriptName);
    				
    				if (scriptId.equals("")) {
    					int result = cisc001Service.insertScript(scriptVO);
        				resData.put("result", result);
    				}else {
    					int result = cisc001Service.updateScript(scriptVO);
        				resData.put("result", result);
    				}    				
    			}else {
    				resData.put("result", 0);
    				shellFile.delete();
    				resData.put("msg", "쉘 생성 실패");
    				System.out.println("쉘 생성 실패");
    			}
    			
    		}catch(IOException ie) {
    			ie.printStackTrace();
    		}catch(Exception e) {
    			e.printStackTrace();
    		}
	        
    	}
        
    	
    	ModelAndView mav = new ModelAndView("jsonView", resData);
   		return mav;   
   	}    
    
    @RequestMapping(value = "/cisc001/searchScriptList.do")
   	public ModelAndView searchScriptList(@ModelAttribute("paramVO") CISC001ScriptVO cisc001scriptVO) throws Exception {
       	
        Map<String, Object> resultMap = new HashMap<String, Object>();

   		List<CISC001ScriptVO> scriptList =  cisc001Service.selectScriptList(cisc001scriptVO);
   		resultMap.put("resultList", scriptList);
   		ModelAndView mav = new ModelAndView("jsonView", resultMap);
   		return mav;   
   	}
    
    public boolean execute(String cmd) {
        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer successOutput = new StringBuffer();
        StringBuffer errorOutput = new StringBuffer();
        BufferedReader successBufferReader = null; 
        BufferedReader errorBufferReader = null;
        String msg = null;
        boolean bSuccess = false;
 
        List<String> cmdList = new ArrayList<String>();
 
        
        cmdList.add("/bin/sh");
        cmdList.add("-c");
        
        // 명령어 셋팅
        cmdList.add(cmd);
        String[] array = cmdList.toArray(new String[cmdList.size()]);
 
        try {
 
            process = runtime.exec(array); 
            // shell 실행이 정상 동작했을 경우
            successBufferReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "EUC-KR"));
 
            while ((msg = successBufferReader.readLine()) != null) {
                successOutput.append(msg + System.getProperty("line.separator"));
            }
 
            // shell 실행시 에러가 발생했을 경우
            errorBufferReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "EUC-KR"));
            while ((msg = errorBufferReader.readLine()) != null) {
                errorOutput.append(msg + System.getProperty("line.separator"));
            }
 
            process.waitFor();
            
            if (process.exitValue() == 0) {
            	bSuccess = true;
            }
            
 
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                process.destroy();
                if (successBufferReader != null) successBufferReader.close();
                if (errorBufferReader != null) errorBufferReader.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return bSuccess;
    }
    
    
    @RequestMapping(value = "/cisc001/updateScript.do")
   	public ModelAndView updateScript(@ModelAttribute("paramVO") CISC001ScriptVO cisc001scriptVO) throws Exception {
       	
    	boolean bResult = true;
    	
    	File file = new File(cisc001scriptVO.getExtrlFileCoursNm());
    	//파일을 삭제 하고 입력받은 이름으로 다시 만든다.
    	if (file.exists()) {
    		bResult = execute("hadoop fs -rm /user/oozie/shellscript/"+file.getName());
    		if (bResult) {
    			file.delete();
    		}
    	}
    	   	
    	
       	int result = cisc001Service.updateScript(cisc001scriptVO);    	
       	
       	Map<String, Object> resData = new HashMap<String, Object>();
       	resData.put("result", result);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
        return mav;
   	}
    
    @RequestMapping(value = "/cisc001/deleteScript.do")
   	public ModelAndView deleteScript(@ModelAttribute("paramVO") CISC001ScriptVO cisc001scriptVO) throws Exception {
    	Map<String, Object> resData = new HashMap<String, Object>();
    	boolean bResult = true;
    	File file = new File(cisc001scriptVO.getExtrlFileCoursNm());
    	
    	if (file.exists()) {
    		bResult = execute("hadoop fs -rm /user/oozie/shellscript/"+file.getName());
    		if (bResult) {
    			file.delete();
    		}
    	}
    	int result = cisc001Service.deleteScript(cisc001scriptVO);    	
		resData.put("result", result);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
        return mav;
   	}
    
}
