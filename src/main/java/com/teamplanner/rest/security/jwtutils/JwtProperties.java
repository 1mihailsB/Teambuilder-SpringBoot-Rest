package com.teamplanner.rest.security.jwtutils;

public class JwtProperties {
	public static final String SECRET ="development";
	public static final int EXPIRATION_TIME_MILLISECONDS = 60*60*1000; //60 minutes in milliseconds
	public static final String TOKEN_PREFIX = "Bearer:";
	public static final String COOKIE_NAME = "Authorization";
	
}
