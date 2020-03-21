package com.teamplanner.rest.security;


import com.teamplanner.rest.dao.UserRepository;
import com.teamplanner.rest.model.User;
import com.teamplanner.rest.security.jwtutils.JwtGeneratorVerifier;
import com.teamplanner.rest.security.jwtutils.JwtProperties;
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

import java.util.List;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    UserRepository userRepository;
    JwtGeneratorVerifier jwtgv;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository, JwtGeneratorVerifier jwtgv) {
        super(authenticationManager);
        this.userRepository = userRepository;
        this.jwtgv = jwtgv;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie cookie = WebUtils.getCookie(request, JwtProperties.COOKIE_NAME);
        if(cookie != null) System.out.println("Cookie in filter: "+cookie.getName()+":"+cookie.getValue()+"SW: "+cookie.getValue().startsWith(JwtProperties.TOKEN_PREFIX));

        if(cookie == null || !cookie.getValue().startsWith(JwtProperties.TOKEN_PREFIX)){
            chain.doFilter(request, response);
            return;
        }

        Authentication authentication = getGoogleSubJwtAuthentication(cookie.getValue());
        SecurityContextHolder.getContext().setAuthentication(authentication);


        chain.doFilter(request, response);
    }

    private Authentication getGoogleSubJwtAuthentication(String cookie) {
        String token = cookie.replace(JwtProperties.TOKEN_PREFIX, "");

        if(token != null){
            String googleSub = jwtgv.verifySignedJwt(token);

            if(googleSub != null){
                User user = userRepository.findByGooglesub(googleSub);
                UserPrincipal userPrincipal = new UserPrincipal(user);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(googleSub, null, userPrincipal.getAuthorities());
                System.out.println(auth.getPrincipal());

                return auth;
            }
            return null;
        }
        return null;
    }
}
