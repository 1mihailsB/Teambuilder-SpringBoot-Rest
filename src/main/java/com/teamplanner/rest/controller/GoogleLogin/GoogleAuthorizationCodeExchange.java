package com.teamplanner.rest.controller.GoogleLogin;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class GoogleAuthorizationCodeExchange {

	@Value("${google.client.id}")
	private String CLIENT_ID;
	@Value("${google.client.secret}")
	private String CLIENT_SECRET;
	@Value("${google.redirect.uri}")
	private String REDIRECT_URI;
	@Autowired
	RestTemplate restTemplate;
	
	public ResponseEntity<Map> exchangeAuthCode(Map<String, Object> authorizationCode) throws HttpClientErrorException{
		
		ResponseEntity<Map> googleResponse = null;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		HttpEntity<String> entity = new HttpEntity<String>("code="+authorizationCode.get("authCode")
															+ "&client_id="+CLIENT_ID
															+ "&client_secret="+CLIENT_SECRET
															+ "&redirect_uri="+REDIRECT_URI
															+ "&grant_type=authorization_code",headers);
		
		try {
			googleResponse = restTemplate.exchange("https://oauth2.googleapis.com/token", HttpMethod.POST, entity, Map.class);
	
		} catch (HttpClientErrorException e) {
			
			throw e;
		}
		
		return googleResponse;
		
	}
	
}
