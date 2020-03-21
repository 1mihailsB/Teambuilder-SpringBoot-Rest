package com.teamplanner.rest.security.jwtutils;

public class JwtProperties {
	public static final String SECRET ="development";
	public static final int EXPIRATION_TIME = 180_000; //3 minutes in milliseconds
	public static final String TOKEN_PREFIX = "Bearer:";
	public static final String COOKIE_NAME = "Authorization";
	
}
