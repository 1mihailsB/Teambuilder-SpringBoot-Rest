package com.teamplanner.rest.security.jwtutils;

import com.auth0.jwt.JWT;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class JwtGeneratorVerifier {

	public String createSignedJwt (String subject) {
		
		String token = JWT.create()
				.withSubject(subject)
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(HMAC512(JwtProperties.SECRET.getBytes()));
		
		return token;
	}

	public String verifySignedJwt (String token) {
		
		String googleSub = JWT.require(HMAC512(JwtProperties.SECRET.getBytes()))
				.build()
				.verify(token)
				.getSubject();
		
		return googleSub;
	}
	
}
