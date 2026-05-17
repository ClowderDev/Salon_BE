package com.clowder.service;

import com.clowder.dto.request.SignUpDTO;
import com.clowder.dto.response.AuthResponse;

public interface AuthService {
  AuthResponse login(String username, String password);

  AuthResponse signUp(SignUpDTO request);

  AuthResponse getAccessTokenFromRefreshToken(String refreshToken);
}
