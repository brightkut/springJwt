package com.brightkut.springjwt.controller;

import com.brightkut.springjwt.entity.AuthenticationResponse;
import com.brightkut.springjwt.entity.User;
import com.brightkut.springjwt.service.AuthenticationService;
import com.brightkut.springjwt.service.TokenBlacklist;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.brightkut.springjwt.service.JwtService.cookieExpiry;

@RestController
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final TokenBlacklist tokenBlacklist;

    public AuthenticationController(AuthenticationService authenticationService, TokenBlacklist tokenBlacklist) {
        this.authenticationService = authenticationService;
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User request){
        authenticationService.register(request);
        return ResponseEntity.ok("Create user success");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody User request, HttpServletResponse response){
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);

        ResponseCookie cookie = ResponseCookie.from("accessToken", authenticationResponse.token())
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(cookieExpiry)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok()
                .body(authenticationResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        tokenBlacklist.addToBlacklist(token);

        // Clear any session-related data if necessary

        return ResponseEntity.ok("Logged out successfully");
    }

    public static String extractTokenFromRequest(HttpServletRequest request) {
        // Get the Authorization header from the request
        String authorizationHeader = request.getHeader("Authorization");

        // Check if the Authorization header is not null and starts with "Bearer "
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            // Extract the JWT token (remove "Bearer " prefix)
            return authorizationHeader.substring(7);
        }

        // If the Authorization header is not valid, return null
        return null;
    }
}
