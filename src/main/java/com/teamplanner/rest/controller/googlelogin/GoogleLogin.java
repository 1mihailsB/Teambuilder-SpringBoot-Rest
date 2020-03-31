package com.teamplanner.rest.controller.googlelogin;

import com.teamplanner.rest.model.User;
import com.teamplanner.rest.security.jwtutils.JwtGeneratorVerifier;
import com.teamplanner.rest.security.jwtutils.JwtProperties;
import com.teamplanner.rest.service.UserService;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GoogleLogin {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final GoogleAuthorizationCodeExchange exchange;
    private final UserService userService;
    private final JwtGeneratorVerifier jwtGeneratorVerifier;

    @Autowired
    public GoogleLogin(GoogleAuthorizationCodeExchange exchange, UserService userService,
                       JwtGeneratorVerifier jwtGeneratorVerifier) {
    	this.exchange = exchange;
    	this.userService = userService;
    	this.jwtGeneratorVerifier = jwtGeneratorVerifier;
	}
    @SuppressWarnings("rawtypes")
    protected ResponseEntity<Map> login(Map<String, Object> authorizationCode, HttpServletResponse httpResponse) {
        
		ResponseEntity<Map> googleResponse;

        googleResponse = exchange.exchangeAuthCode(authorizationCode);

        if (googleResponse.getStatusCode() == HttpStatus.OK) {
            Map<String,String> googleUserInfo = extractGoogleUserInfo(googleResponse);
            return authenticateAndPrepareResponse(googleUserInfo, httpResponse);
        }
        throw new RuntimeException("an error occured while exchanging authorization code");
    }

    
    @SuppressWarnings("rawtypes")
	private ResponseEntity<Map> authenticateAndPrepareResponse(Map<String,String> googleUserInfo, HttpServletResponse httpResponse) {

        User user = userService.findById(googleUserInfo.get("sub"));
        if(user == null) {
        	user = new User(googleUserInfo.get("sub"), googleUserInfo.get("given_name"), googleUserInfo.get("email"));
        	userService.save(user);
        }else {
        	if (LOG.isDebugEnabled()) LOG.debug("----- User already exists in our database: {}", user);
        }

        String jwt = jwtGeneratorVerifier.createSignedJwt(googleUserInfo.get("sub"));

        final Cookie jwtCookie = new Cookie(JwtProperties.COOKIE_NAME, JwtProperties.TOKEN_PREFIX + jwt);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(JwtProperties.EXPIRATION_TIME_MILLISECONDS/1000);
        jwtCookie.setPath("/");

        final Cookie userNicknameCookie;
        if(user.getNickname()==null){
            userNicknameCookie = new Cookie("nickname", "*()unset");
        }else{//if user hasn't yet chosen a nickname, we set it to *()unset in the cookie. front-end expects this.
            userNicknameCookie = new Cookie("nickname", user.getNickname());
        }
        userNicknameCookie.setMaxAge(JwtProperties.EXPIRATION_TIME_MILLISECONDS/1000);
        userNicknameCookie.setPath("/");

        httpResponse.addCookie(jwtCookie);
        httpResponse.addCookie(userNicknameCookie);

        ResponseEntity<Map> response = new ResponseEntity<>(HttpStatus.OK);
        
        if (LOG.isDebugEnabled()) LOG.debug("response to frontend: {}", new JSONObject(response).toString(4));

        return response;
    }

    private Map<String, String> extractGoogleUserInfo(ResponseEntity<Map> googleResponse){

        if (LOG.isDebugEnabled()) LOG.debug("googleresponse:  {}",  new JSONObject(googleResponse).toString(4));

        String jwtIdToken = (String) googleResponse.getBody().get("id_token");
        String[] splitToken = jwtIdToken.split("\\.");
        String base64EncodedBody = splitToken[1];

        Base64 base64UrlSafe = new Base64(true);
        String idTokenDecoded = new String(base64UrlSafe.decode(base64EncodedBody));
        if (LOG.isDebugEnabled()) LOG.debug("JWT Body : {}", new JSONObject(idTokenDecoded).toString(4));

        String bodyQuotesRemoved = idTokenDecoded.replaceAll("\"", "");
        String[] bodyEntries = bodyQuotesRemoved.split(",");

        List<String> wantedUserInfo = Arrays.asList("sub","given_name", "email");
        Map<String, String> googleUserInfo =
                Arrays.stream(bodyEntries)
                        .map(elem -> elem.split(":"))
                        .filter(elem -> wantedUserInfo.contains(elem[0]))
                        .collect(Collectors.toMap(e -> e[0], e -> e[1]));

        return googleUserInfo;
    }
    
}
