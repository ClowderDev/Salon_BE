package com.clowder.user.service.impl;

import com.clowder.user.dto.request.SignUpDTO;
import com.clowder.user.dto.response.AuthResponse;
import com.clowder.user.dto.response.TokenResponse;
import com.clowder.user.model.User;
import com.clowder.user.repository.UserRepository;
import com.clowder.user.service.AuthService;
import com.clowder.user.service.KeycloakService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final KeycloakService keycloakService;

  @Override
  public AuthResponse login(String username, String password) {

    TokenResponse tokenResponse =
        keycloakService.getAdminAccessToken(username, password, "password", null);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setRefreshToken(tokenResponse.getRefreshToken());
    authResponse.setJwt(tokenResponse.getAccessToken());
    authResponse.setMessage("Login successful");
    return authResponse;
  }

  @Override
  public AuthResponse signUp(SignUpDTO request) {
    keycloakService.createUser(request);

    User user = new User();
    user.setUsername(request.getUsername());
    user.setPassword(request.getPassword());
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    user.setFullName(request.getFirstName() + " " + request.getLastName());
    user.setCreatedAt(LocalDateTime.now());

    userRepository.save(user);

    TokenResponse tokenResponse =
        keycloakService.getAdminAccessToken(
            request.getUsername(), request.getPassword(), "password", null);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setRefreshToken(tokenResponse.getRefreshToken());
    authResponse.setJwt(tokenResponse.getAccessToken());
    authResponse.setRole(request.getRole());
    authResponse.setMessage("User registered successfully");
    return authResponse;
  }

  @Override
  public AuthResponse getAccessTokenFromRefreshToken(String refreshToken) {
    TokenResponse tokenResponse =
        keycloakService.getAdminAccessToken(null, null, "refresh_token", refreshToken);

    AuthResponse authResponse = new AuthResponse();
    authResponse.setRefreshToken(tokenResponse.getRefreshToken());
    authResponse.setJwt(tokenResponse.getAccessToken());
    authResponse.setMessage("Access token refreshed successfully");
    return authResponse;
  }
}
