package ugis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CISC006Controller {
	
	  /**
     * 초기 화면 표시
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/cisc006.do")
    public String cmsc001(ModelMap model) throws Exception {

        return "ci/cisc005_6";
    }
    
    
    
	

}
