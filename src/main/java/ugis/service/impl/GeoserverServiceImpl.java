package ugis.service.impl;

import java.io.File;

import javax.servlet.ServletContext;

import org.parboiled.common.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import ugis.service.GeoserverService;
import ugis.util.map.BaseMap;

@Service("geoserverService")
@Slf4j
public class GeoserverServiceImpl implements GeoserverService {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoserverServiceImpl.class);


	 @Value("${geoserver2.url}")
	 private String url;


	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ServletContext context;

	@Override
	public byte[] getByteFromMapImageResource(String paramStr) {
		try {

			String requestUrl = url+ "wms" + "?" + paramStr;
			
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

			ResponseEntity<byte[]> response = restTemplate.exchange(requestUrl, HttpMethod.GET, entity,
					byte[].class);
			return response.getBody();
		} catch (RestClientException e) {
			log.error("basemap image get fail.", e);
			String path = context.getRealPath("/resources") + File.separator + "images";
			File noTileFile = new File(path, "no_tile.png");
			return FileUtils.readAllBytes(noTileFile);
		}
	}
}
