package ugis.cmmn;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component	
public class AgentRestComponent {
	private RestTemplate restTemplate = new RestTemplate();
	
	public HttpHeaders makeHeader(String apiKey) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("X-Auth-Token", apiKey);
		return headers;
	}
	

	
}