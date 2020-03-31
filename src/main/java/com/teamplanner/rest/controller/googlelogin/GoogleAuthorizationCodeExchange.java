package com.teamplanner.rest.controller.googlelogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.invoke.MethodHandles;
import java.util.Map;

@Component
public class GoogleAuthorizationCodeExchange {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${google.client.id}")
    private String clientId;
    @Value("${google.client.secret}")
    private String clientSecret;
    @Value("${google.redirect.uri}")
    private String redirectUri;
    @Value("${google.token.endpoint}")
    private String tokenEndpoint;

    private final RestTemplate restTemplate;

    @Autowired
    public GoogleAuthorizationCodeExchange(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("rawtypes")
	public ResponseEntity<Map> exchangeAuthCode(Map<String, Object> authorizationCode) {

        ResponseEntity<Map> googleResponse = null;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        /**
        example of authorization code exchange request is given in Google documentation, we do it after we receive
        authorization code by front end by redirecting user to Google authentication page, then we send the code here.
        google example: https://developers.google.com/identity/protocols/oauth2/web-server#exchange-authorization-code
         **/
        HttpEntity<String> entity = new HttpEntity<>("code=" + authorizationCode.get("authCode")
                + "&client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&redirect_uri=" + redirectUri
                + "&grant_type=authorization_code", headers);

        try {
            googleResponse = restTemplate.exchange(tokenEndpoint, HttpMethod.POST, entity, Map.class);
        } catch (HttpClientErrorException e) {
            LOG.error("an error occured while exchanging auth code", e);
        }

        return googleResponse;

    }

}
