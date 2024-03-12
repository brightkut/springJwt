package com.brightkut.springjwt.filter;

import com.brightkut.springjwt.service.TokenBlacklist;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.brightkut.springjwt.controller.AuthenticationController.extractTokenFromRequest;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final TokenBlacklist tokenBlacklist;

    public JwtTokenFilter(TokenBlacklist tokenBlacklist) {
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractTokenFromRequest(request);

        if (token != null && !tokenBlacklist.isBlacklisted(token)) {
            // Token is valid and not blacklisted
            // Proceed with request processing
            filterChain.doFilter(request, response);
        } else {
            // Token is blacklisted or expired, deny access
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}