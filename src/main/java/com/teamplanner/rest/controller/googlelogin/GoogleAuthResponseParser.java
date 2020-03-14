package com.teamplanner.rest.controller.googlelogin;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.teamplanner.rest.dao.UserRepository;
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
    
    protected String exchange(Map<String, Object> authorizationCode) {
        ResponseEntity<Map> googleResponse = null;

        googleResponse = exchange.exchangeAuthCode(authorizationCode);

        if (googleResponse.getStatusCode() == HttpStatus.OK) {
            return parseGoogleReseponse(googleResponse);
        }
        throw new RuntimeException("an error occured while exchanging auth code");
    }

    
    private String parseGoogleReseponse(ResponseEntity<Map> googleResponse) {
    	JSONObject json = new JSONObject(googleResponse);

        //To avoid evaluating the to string method even when the loglevel is higher than debug we need to check if debug loging is enable for performance reasons
        if (LOG.isDebugEnabled()) {
            LOG.debug("googleresponse:  {}", json.toString(4));
        }
        LOG.debug("BODY--------- \n {}", json.get("body"));


        String jwtToken = (String) googleResponse.getBody().get("id_token");
        String[] splitString = jwtToken.split("\\.");
        String base64EncodedBody = splitString[1];
        Base64 base64Url = new Base64(true);

        String body = new String(base64Url.decode(base64EncodedBody));
        LOG.debug("JWT Body : {}", body);
        String newBody = body.replaceAll("\"", "");
        String[] bodyEntries = newBody.split(",");
        List<String> userProperties = Arrays.asList("sub","given_name", "email");
        
        Map<String, String> userprops =
                Arrays.stream(bodyEntries)
                        .map(elem -> elem.split(":"))
                        .filter(elem -> userProperties.contains(elem[0]))
                        .collect(Collectors.toMap(e -> e[0], e -> e[1]));
        LOG.debug("user props: {}", userprops);
        
        User user = userService.findById(userprops.get("sub"));
        LOG.debug("google user: "+user);
        if(user == null) {
        	User newUser = new User(userprops.get("sub"), userprops.get("given_name"), userprops.get("email"));
        	LOG.debug("new user: "+newUser);
        	userService.save(newUser);
        }else {
        	LOG.debug("----- User already exists in our database");
        }
        
        
        Map<String, String> userDetails =
                Arrays.stream(bodyEntries)
                        .map(elem -> elem.split(":"))
                        .filter(elem -> elem[0].equals("given_name"))
                        .collect(Collectors.toMap(e -> e[0], e -> e[1]));
        LOG.debug("user details: {}", userDetails);

        JSONObject responseToFrontend = new JSONObject(userDetails);
        LOG.debug("response:  {}", responseToFrontend);
        return responseToFrontend.toString();
    }
    
}
