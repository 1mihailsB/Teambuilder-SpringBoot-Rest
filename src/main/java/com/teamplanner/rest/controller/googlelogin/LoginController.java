package com.teamplanner.rest.controller.googlelogin;

import com.teamplanner.rest.model.User;
import com.teamplanner.rest.security.jwtutils.JwtProperties;
import com.teamplanner.rest.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/oauth")
public class LoginController {

	private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	UserService userService;
    GoogleAuthResponseParser garp;

    @Autowired
    public LoginController(GoogleAuthResponseParser garp, UserService userService) {
        this.garp = garp;
        this.userService = userService;
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
        final Cookie jwtCookie = new Cookie(JwtProperties.COOKIE_NAME, null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        jwtCookie.setPath("/");

        final Cookie userNicknameCookie = new Cookie("nickname", null);//user.getNickname());
        userNicknameCookie.setMaxAge(0);
        userNicknameCookie.setPath("/");

        response.addCookie(userNicknameCookie);
        response.addCookie(jwtCookie);

        return "LoggedOut";
    }

    @PutMapping("/chooseNickname")
    public String chooseNickname(@RequestBody String nickname, Authentication authentication,
                                              HttpServletRequest request, HttpServletResponse response){

        //if incoming nickname doesn't match it means user is doing something to avoid form validation on front end
        //we don't create a special response for that, we respond as if such username is taken.
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9\\[\\]!@_-]{1,16}$");
        Matcher matcher = pattern.matcher(nickname);
        if(!matcher.matches()){
            return "Username taken";
        }

        User user = userService.findById((String) authentication.getPrincipal());

        if(user != null){
            user.setNickname(nickname);
            user.setRoles("ROLE_USER"); //give user a default role after he has chosen a nickname
            try{
                //will throw exception if nickname isn't unique and following code won't run
                user = userService.save(user);

                //also refresh authorization and nickname cookies after updating user nickname
                Cookie userNickNameCookie = WebUtils.getCookie(request, "nickname");
                userNickNameCookie.setValue(nickname);
                userNickNameCookie.setMaxAge(JwtProperties.EXPIRATION_TIME_MILLISECONDS/1000);
                userNickNameCookie.setPath("/");
                response.addCookie(userNickNameCookie);

                Cookie userJwtCookie = WebUtils.getCookie(request, JwtProperties.COOKIE_NAME);
                userJwtCookie.setSecure(true);
                userJwtCookie.setPath("/");
                userJwtCookie.setMaxAge(JwtProperties.EXPIRATION_TIME_MILLISECONDS/1000);
                response.addCookie(userJwtCookie);
            }catch(DataIntegrityViolationException e){
                if (LOG.isDebugEnabled()) LOG.debug(e.getMessage());
                return "Username taken";
            }
        }

        return "Username changed";
    }
}
