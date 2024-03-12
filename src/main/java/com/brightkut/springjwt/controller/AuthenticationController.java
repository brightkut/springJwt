package com.brightkut.springjwt.controller;

import com.brightkut.springjwt.entity.AuthenticationResponse;
import com.brightkut.springjwt.entity.User;
import com.brightkut.springjwt.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request){
        authenticationService.register(request);
        return ResponseEntity.ok("Create user success");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
