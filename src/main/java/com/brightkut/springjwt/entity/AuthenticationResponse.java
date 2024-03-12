package com.brightkut.springjwt.entity;

public record AuthenticationResponse(String token, String refreshToken) {
}
