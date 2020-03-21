package com.teamplanner.rest.controller.googlelogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.teamplanner.rest.security.jwtutils.JwtProperties;

import java.lang.invoke.MethodHandles;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
@RequestMapping("/oauth")
public class LoginController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
    GoogleAuthResponseParser garp;

    @Autowired
    public LoginController(GoogleAuthResponseParser garp) {
        this.garp = garp;
    }

    @PostMapping("/authCode")
    @SuppressWarnings("rawtypes")
    public ResponseEntity<Map> googleAuthentication(@CookieValue (value = JwtProperties.COOKIE_NAME, defaultValue="empty") String AuthorizationJWT,
    @RequestBody Map<String, Object> authorizationCode, HttpServletResponse response) {
        if (LOG.isDebugEnabled()) LOG.debug("COOKIE--- " +AuthorizationJWT);
    	try {
            return garp.exchange(authorizationCode, response);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getStatusText());
        }
    }

    @PostMapping("/logout")
    public  String logout(HttpServletResponse response){
        Cookie cookie = new Cookie(JwtProperties.COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);

        return "LoggedOut";
    }
}
