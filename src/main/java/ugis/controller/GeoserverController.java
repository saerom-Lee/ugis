package ugis.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import ugis.service.GeoserverService;

@Controller
@RequestMapping(value = "/geoserver")
public class GeoserverController {
	private static final Logger LOGGER = LoggerFactory.getLogger(GeoserverController.class);

	@Autowired
	private GeoserverService geoserverService;

	/**
	 * 프록시 처리
	 *
	 * @param url
	 * @return
	 */
	@RequestMapping(value = "/proxy.do", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<byte[]> baseMap(HttpServletRequest request) throws EgovBizException {

		Enumeration attNames = request.getParameterNames();

		StringBuilder strBuilder = new StringBuilder();

		while (attNames.hasMoreElements()) {
			String key = (String) attNames.nextElement();
			String value = request.getParameter(key);

			strBuilder.append(key);
			strBuilder.append("=");
			strBuilder.append(value);
			strBuilder.append("&");
		}

		String paramStr = strBuilder.toString();

		paramStr = strBuilder.replace(paramStr.length() - 1, paramStr.length() - 1, "").toString();

		byte[] resourceBytes = geoserverService.getByteFromMapImageResource(paramStr);
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		return new ResponseEntity<>(resourceBytes, headers, HttpStatus.CREATED);
	}
}
