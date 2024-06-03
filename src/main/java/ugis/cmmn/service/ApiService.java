package ugis.cmmn.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import ugis.exception.RestApiException;

@RequiredArgsConstructor
@Service
public class ApiService<T> {

    @Value("${rest.api.url}")
    private String restUrl;

    private final RestTemplate restTemplate;

    @SuppressWarnings("unchecked")
    public ResponseEntity<T> get(String url, HttpHeaders httpHeaders) {
        return callApiEndpoint(restUrl + url, HttpMethod.GET, httpHeaders, null, (Class<T>)Object.class);
    }

    public ResponseEntity<T> get(String url, HttpHeaders httpHeaders, Class<T> clazz) {
        return callApiEndpoint(restUrl + url, HttpMethod.GET, httpHeaders, null, clazz);
    }

    @SuppressWarnings("unchecked")
    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body) {
        return callApiEndpoint(restUrl + url, HttpMethod.POST, httpHeaders, body,(Class<T>)Object.class);
    }

    public ResponseEntity<T> post(String url, HttpHeaders httpHeaders, Object body, Class<T> clazz) {
        return callApiEndpoint(restUrl + url, HttpMethod.POST, httpHeaders, body, clazz);
    }

    private ResponseEntity<T> callApiEndpoint(String url, HttpMethod httpMethod, HttpHeaders httpHeaders, Object body, Class<T> clazz) {

        ResponseEntity<T> response = null;

        try {
            response = restTemplate.exchange(url, httpMethod, new HttpEntity<>(body, httpHeaders), clazz);
        } catch(RestClientException e) {
            throw new RestApiException("Api Server Connect Timed out.");
        }

        return response;
    }


}
