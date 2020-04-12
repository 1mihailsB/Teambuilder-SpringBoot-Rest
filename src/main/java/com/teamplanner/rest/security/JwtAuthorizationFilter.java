package com.teamplanner.rest.security;


import com.teamplanner.rest.model.entity.User;
import com.teamplanner.rest.security.jwtutils.JwtGeneratorVerifier;
import com.teamplanner.rest.security.jwtutils.JwtProperties;
import com.teamplanner.rest.service.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    UserService userService;
    JwtGeneratorVerifier jwtgv;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService, JwtGeneratorVerifier jwtgv) {
        super(authenticationManager);
        this.userService = userService;
        this.jwtgv = jwtgv;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie jwtCookie = WebUtils.getCookie(request, JwtProperties.COOKIE_NAME);

        if(jwtCookie == null || !jwtCookie.getValue().startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getGoogleSubJwtAuthentication(jwtCookie.getValue());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        chain.doFilter(request, response);
    }

    private Authentication getGoogleSubJwtAuthentication(String cookie) {
        String token = cookie.replace(JwtProperties.TOKEN_PREFIX, "");

        if(token != null){
            String googleSub = jwtgv.verifySignedJwt(token);

            if(googleSub != null){
                User user = userService.findById(googleSub);

                if(user!=null) {
                    MyUserDetails userDetails = new MyUserDetails(user);
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(googleSub, null, userDetails.getAuthorities());
                    return auth;
                }
                return null;
            }
            return null;
        }
        return null;
    }
}
