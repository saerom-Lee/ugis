package ugis.controller;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import ugis.service.CISC004Service;


@Controller
public class CISC004Controller {



    @Resource(name = "fileProperties")
    private Properties fileProperties;



    @Resource(name = "cisc004Service")
    private CISC004Service cisc004Service;


    @RequestMapping(value = "/cisc004.do")
    public String cmsc001(ModelMap model) throws Exception {

        return "ci/cisc004";
    }

}
