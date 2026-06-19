package com.clowder.user.controller;

import com.clowder.user.dto.request.LoginDTO;
import com.clowder.user.dto.request.SignUpDTO;
import com.clowder.user.dto.response.AuthResponse;
import com.clowder.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication", description = "Registration and token endpoints")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @Operation(summary = "Sign up a new user")
  @PostMapping("/signup")
  public ResponseEntity<AuthResponse> signUp(@RequestBody @Valid SignUpDTO request) {
    AuthResponse authResponse = authService.signUp(request);
    return ResponseEntity.ok(authResponse);
  }

  @Operation(summary = "Log in a user")
  @PostMapping("/login")
  public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginDTO request) {
    AuthResponse authResponse = authService.login(request.getEmail(), request.getPassword());
    return ResponseEntity.ok(authResponse);
  }

  @Operation(summary = "Refresh access token using refresh token")
  @PostMapping("/access-token/refresh-token/{refreshToken}")
  public ResponseEntity<AuthResponse> getAccessToken(@PathVariable String refreshToken) {
    AuthResponse authResponse = authService.getAccessTokenFromRefreshToken(refreshToken);
    return ResponseEntity.ok(authResponse);
  }
}
