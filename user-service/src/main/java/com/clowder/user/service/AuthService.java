package com.clowder.user.service;

import com.clowder.user.dto.request.SignUpDTO;
import com.clowder.user.dto.response.AuthResponse;

public interface AuthService {
  AuthResponse login(String username, String password);

  AuthResponse signUp(SignUpDTO request);

  AuthResponse getAccessTokenFromRefreshToken(String refreshToken);
}
