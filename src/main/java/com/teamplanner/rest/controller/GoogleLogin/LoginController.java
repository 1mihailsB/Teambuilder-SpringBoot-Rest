package com.teamplanner.rest.controller.GoogleLogin;

import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin
@RestController
@RequestMapping("/oauth")
public class LoginController {
	
	GoogleAuthResponseParser gace;
	
	@Autowired
	public LoginController(GoogleAuthResponseParser garp) {
		this.gace = garp;
	}
	
	@PostMapping("/authCode") //authorization code in request body in JSON object {'authCode':'the Code'}
	public String googleAuthentication(@RequestBody Map<String, Object> authorizationCode) {
		try {
			return gace.exchange(authorizationCode);
		}catch (HttpClientErrorException e) {
			throw new ResponseStatusException(e.getStatusCode(), e.getStatusText());
		}
	}
	
}
