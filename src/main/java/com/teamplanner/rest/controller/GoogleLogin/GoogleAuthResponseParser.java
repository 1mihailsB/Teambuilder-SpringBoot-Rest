package com.teamplanner.rest.controller.GoogleLogin;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
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
public class GoogleAuthResponseParser {

	@Autowired
	GoogleAuthorizationCodeExchange exchange;
	
				//authorization code in request body in JSON object {'authCode':'the Code'}
	protected String exchange(Map<String, Object> authorizationCode)throws HttpClientErrorException {
		ResponseEntity<Map> googleResponse = null;
		
		googleResponse = exchange.exchangeAuthCode(authorizationCode);
		
		JSONObject responseToFrontend = new JSONObject().put("empty", "value");
		
		if(googleResponse!=null) {
			JSONObject json = new JSONObject(googleResponse);
			
			System.out.println("gresponse"+googleResponse.getHeaders());
			System.out.println("id_token++   "+googleResponse.getBody().get("id_token"));
			System.out.println("gresponse++"+json.toString(4));
			System.out.println("JSON--------- \n"+json.get("body"));
			
			
			String jwtToken = (String)googleResponse.getBody().get("id_token");
			System.out.println("------------ Decode JWT ------------");
			String[] split_string = jwtToken.split("\\.");
//			String base64EncodedHeader = split_string[0];
			String base64EncodedBody = split_string[1];
//			String base64EncodedSignature = split_string[2];
//			
//			System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
			Base64 base64Url = new Base64(true);
//			String header = new String(base64Url.decode(base64EncodedHeader));
//			System.out.println("JWT Header : " + header);
			
			
			System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
			String body = new String(base64Url.decode(base64EncodedBody));
			System.out.println("JWT Body : "+body);      
			String newBody = body.replaceAll("\"","");
			String[] bodyEntries = newBody.split(",");
			Map<String, String> userDetails = 
					Arrays.stream(bodyEntries)
					.map(elem -> elem.split(":"))
					.filter(elem -> elem[0].equals("given_name"))
					.collect(Collectors.toMap(e -> e[0], e -> e[1]));
			System.out.println(userDetails);
			
			responseToFrontend = new JSONObject(userDetails);
			System.out.println("response:  "+responseToFrontend);
			return responseToFrontend.toString();
			
			
//			System.out.println("~~~~~ JWT Signature ~~~~~~");
//			String signature = new String(base64Url.decode(base64EncodedSignature));
//			System.out.println("JWT Signature : "+signature);
		}
		
		
		return responseToFrontend.toString();
	}
	
}
