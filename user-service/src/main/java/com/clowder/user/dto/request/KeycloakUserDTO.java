package com.clowder.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakUserDTO {
  private String id;
  private String firstName;
  private String lastName;
  private String email;
  private String username;
}
