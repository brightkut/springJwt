package com.brightkut.springjwt.controller;

import com.brightkut.springjwt.dto.RefreshTokenRequestDTO;
import com.brightkut.springjwt.entity.AuthenticationResponse;
import com.brightkut.springjwt.entity.RefreshToken;
import com.brightkut.springjwt.service.JwtService;
import com.brightkut.springjwt.service.RefreshTokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public RefreshTokenController(RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/refreshToken")
    public AuthenticationResponse refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
        return refreshTokenService.findByToken(refreshTokenRequestDTO.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String accessToken = jwtService.generateToken(user);
                    return new AuthenticationResponse(accessToken, refreshTokenRequestDTO.refreshToken());
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    }
}
