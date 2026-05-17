package com.clowder.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class SignUpDTO {

  private String firstName;
  private String lastName;
  private String email;
  private String password;
  private String username;
  private UserRole role;
}
