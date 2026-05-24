package com.clowder.booking.service;

import com.clowder.booking.dto.request.SignUpDTO;
import com.clowder.booking.dto.response.AuthResponse;

public interface AuthService {
  AuthResponse login(String username, String password);

  AuthResponse signUp(SignUpDTO request);

  AuthResponse getAccessTokenFromRefreshToken(String refreshToken);
}
