package com.teamplanner.rest.controller.googlelogin;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.teamplanner.rest.model.User;
import com.teamplanner.rest.service.UserService;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleAuthResponseParser {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  
    GoogleAuthorizationCodeExchange exchange;
    UserService userService;

    @Autowired
    public GoogleAuthResponseParser(GoogleAuthorizationCodeExchange gace, UserService userService) {
    	this.exchange = gace;
    	this.userService = userService;
	}
    @SuppressWarnings("rawtypes")
    protected ResponseEntity<Map> exchange(Map<String, Object> authorizationCode) {
        
		ResponseEntity<Map> googleResponse = null;

        googleResponse = exchange.exchangeAuthCode(authorizationCode);

        if (googleResponse.getStatusCode() == HttpStatus.OK) {
            return parseGoogleReseponse(googleResponse);
        }
        throw new RuntimeException("an error occured while exchanging auth code");
    }

    
    @SuppressWarnings("rawtypes")
	private ResponseEntity<Map> parseGoogleReseponse(ResponseEntity<Map> googleResponse) {
    	
    	JSONObject json = new JSONObject(googleResponse);
        //To avoid evaluating the to string method even when the loglevel is higher than debug we need to check if debug loging is enable for performance reasons
        if (LOG.isDebugEnabled()) LOG.debug("googleresponse:  {}", json.toString(4));

        String jwtIdToken = (String) googleResponse.getBody().get("id_token");
        String[] splitToken = jwtIdToken.split("\\.");
        String base64EncodedBody = splitToken[1];
        
        Base64 base64UrlSafe = new Base64(true);
        String idTokenDecoded = new String(base64UrlSafe.decode(base64EncodedBody));
        if (LOG.isDebugEnabled()) LOG.debug("JWT Body : {}", new JSONObject(idTokenDecoded).toString(4));
        
        String bodyQuotesRemoved = idTokenDecoded.replaceAll("\"", "");
        String[] bodyEntries = bodyQuotesRemoved.split(",");
        
        List<String> selectedUserProperties = Arrays.asList("sub","given_name", "email");
        Map<String, String> userprops =
                Arrays.stream(bodyEntries)
                        .map(elem -> elem.split(":"))
                        .filter(elem -> selectedUserProperties.contains(elem[0]))
                        .collect(Collectors.toMap(e -> e[0], e -> e[1]));

        User user = userService.findById(userprops.get("sub"));
        if(user == null) {
        	User newUser = new User(userprops.get("sub"), userprops.get("given_name"), userprops.get("email"));
        	userService.save(newUser);
        }else {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("----- User already exists in our database: "+user);
        		}
        }

        JSONObject responseToFrontend = new JSONObject(Map.of("given_name", userprops.get("given_name")));
        
        ResponseEntity<Map> response = new ResponseEntity<Map>(responseToFrontend.toMap(), HttpStatus.OK);
        
        if (LOG.isDebugEnabled()) LOG.debug("response to frontned: {}", new JSONObject(response).toString(4));
        
        return response;
    }
    
}
