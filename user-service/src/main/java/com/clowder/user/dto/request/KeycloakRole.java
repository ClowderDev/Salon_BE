package com.clowder.booking.dto.request;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakRole {
  private String id;
  private String name;
  private String description;
  private boolean composite;
  private boolean clientRole;
  private String containerId;
  private Map<String, Object> attributes;
}
