package com.clowder.user.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Credential {
  private String type;
  private String value;
  private boolean temporary;
}
