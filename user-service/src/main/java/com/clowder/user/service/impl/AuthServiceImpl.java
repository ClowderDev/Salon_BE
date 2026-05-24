package com.clowder.user.service.impl;

import com.clowder.common.exception.BusinessException;
import com.clowder.user.dto.request.SignUpDTO;
import com.clowder.user.dto.response.AuthResponse;
import com.clowder.user.dto.response.TokenResponse;
import com.clowder.user.model.User;
import com.clowder.user.repository.UserRepository;
import com.clowder.user.service.AuthService;
import com.clowder.user.service.KeycloakService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  @Transactional
  public AuthResponse signUp(SignUpDTO request) {
    if (userRepository.findByEmail(request.getEmail()) != null) {
      throw new BusinessException("Email already exists: " + request.getEmail());
    }

    keycloakService.createUser(request);

    User user = new User();
    user.setUsername(request.getUsername());
    user.setEmail(request.getEmail());
    user.setRole(request.getRole());
    user.setFullName(request.getFirstName() + " " + request.getLastName());
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
