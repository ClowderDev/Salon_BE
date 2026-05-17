package com.clowder.dto.response;

import com.clowder.dto.request.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AuthResponse {
  private String jwt;

  @JsonProperty("refresh_token")
  private String refreshToken;

  private String message;
  private String title;
  private UserRole role;
}
