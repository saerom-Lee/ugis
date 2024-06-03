package ugis.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.fdl.cmmn.exception.EgovBizException;
import ugis.service.TileService;
import ugis.util.map.BaseMap;
import ugis.util.map.TileRowCol;

@Controller
@RequestMapping(value = "/tile")
public class TileController {
	private static final Logger LOGGER = LoggerFactory.getLogger(TileController.class);

	@Autowired
	private TileService tileService;

	/**
	 * 배경맵 프록시 처리
	 *
	 * @param map
	 *            브이월드/구글
	 * @param type
	 *            일반/항공
	 * @param x
	 *            X
	 * @param y
	 *            Y
	 * @param z
	 *            Z
	 * @return
	 */

	// http://map.ngii.go.kr/openapi/Gettile.do?apikey=E57BF781E900D680853EF984632955F9
	// &layer=korean_map&style=korean&tilematrixset=korean&Service=WMTS&Request=GetTile&Version=1.0.0&Format=image%2Fpng&tilematrix=L15&tilecol=2219&tilerow=3982
	@RequestMapping(value = "/proxy/baseMap.do", method = { RequestMethod.GET })
	@ResponseBody
	public ResponseEntity<byte[]> baseMap(@RequestParam String layer, @RequestParam String style,
			@RequestParam String tilematrixset, @RequestParam String Service, @RequestParam String Request,
			@RequestParam String Version, @RequestParam String Format, @RequestParam String tilematrix,
			@RequestParam String tilecol, @RequestParam String tilerow) throws EgovBizException {

		BaseMap baseMap = new BaseMap(layer, style, tilematrixset, Service, Request, Version, Format, tilematrix,
				new TileRowCol(tilerow, tilecol));
		byte[] resourceBytes = tileService.getByteFromMapImageResource(baseMap);

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);

		return new ResponseEntity<>(resourceBytes, headers, HttpStatus.CREATED);
	}
}
