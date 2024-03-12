package com.brightkut.springjwt.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NormalController {
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/normal")
    public ResponseEntity<String> getNormalUser(){
        return ResponseEntity.ok("Hi I'm user");
    }
}
