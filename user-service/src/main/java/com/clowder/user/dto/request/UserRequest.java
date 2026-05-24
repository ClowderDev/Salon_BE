package com.clowder.user.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
  private String username;
  private Boolean enabled;
  private String firstName;
  private String lastName;
  private String email;
  private List<Credential> credentials = new ArrayList<>();
}
