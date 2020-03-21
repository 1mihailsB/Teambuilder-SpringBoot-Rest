package com.teamplanner.rest.controller;

import com.teamplanner.rest.security.jwtutils.JwtProperties;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class Controller {

    @GetMapping("/about")
    public String about (@CookieValue(value = JwtProperties.COOKIE_NAME, defaultValue="empty") String Authorization) {


        return Authorization;
    }
}
