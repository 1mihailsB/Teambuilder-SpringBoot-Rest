package com.teamplanner.rest.controller.googlelogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/oauth")
public class LoginController {

    GoogleAuthResponseParser gace;

    @Autowired
    public LoginController(GoogleAuthResponseParser garp) {
        this.gace = garp;
    }

    @PostMapping("/authCode")
    public String googleAuthentication(@RequestBody Map<String, Object> authorizationCode) {
        try {
            return gace.exchange(authorizationCode);
        } catch (HttpClientErrorException e) {
            throw new ResponseStatusException(e.getStatusCode(), e.getStatusText());
        }
    }

}
