package com.clowder.user.controller;

import com.clowder.user.dto.request.LoginDTO;
import com.clowder.user.dto.request.SignUpDTO;
import com.clowder.user.dto.response.AuthResponse;
import com.clowder.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signUp(@RequestBody SignUpDTO request) {
    AuthResponse authResponse = authService.signUp(request);
    return ResponseEntity.ok(authResponse);
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody LoginDTO request) {
    AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
    return ResponseEntity.ok(authResponse);
  }

  @GetMapping("/access-token/refresh-token/{refreshToken}")
  public ResponseEntity<AuthResponse> getAccessToken(@PathVariable String refreshToken) {
    AuthResponse authResponse = authService.getAccessTokenFromRefreshToken(refreshToken);
    return ResponseEntity.ok(authResponse);
  }
}
