package ugis.handler;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import ugis.controller.CISC010Controller;
import ugis.controller.CISC015Controller;
import ugis.controller.CISC017Controller;
import ugis.exception.RestApiException;

/**
 * @Class Name : RestApiExceptionHandler.java
 * @Description : AI 학습, 객체추출, 변화탐지 RestApiExceptionHandler
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
@RestControllerAdvice(assignableTypes = {CISC010Controller.class, CISC015Controller.class, CISC017Controller.class})
public class RestApiExceptionHandler {

    @ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView handleException(Exception exception, HttpServletRequest request) {
    	String code = "9999";
    	String message = null;
    	if(exception instanceof RestApiException && exception.getCause() != null) {
    		message = exception.getMessage() + " [에러 메시지 = " + exception.getCause().getMessage() + "]";
    	} else if(exception instanceof RestApiException) {
    		message = exception.getMessage();
    	} else {
    		message = "오류가 발생하였습니다." + " [exception = " + exception.getMessage() + "]";
    	}

       	Map<String, Object> resData = new HashMap<String, Object>();
       	resData.put("code", code);
       	resData.put("message", message);
       	ModelAndView mav = new ModelAndView("jsonView", resData);
       	return mav;
    }
}
