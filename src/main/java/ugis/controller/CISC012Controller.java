package ugis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Class Name : CISC012Controller.java
 * @Description : AI 학습
 * @Modification Information
 * @ @ 수정일 / 수정자 / 수정내용 @ -------------------------------------------------
 * @ 2021.09.xx / ngii / 최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2021.09.xx
 * @version 1.0
 * @see
 *
 */
@Controller
public class CISC012Controller {

	
	/**
	 * 초기 화면 표시
	 * 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cisc012.do")
	public String cisc012(ModelMap model) throws Exception {
		System.out.println("in cisc012");
		
		return "";
	}
}
