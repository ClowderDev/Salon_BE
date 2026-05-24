package com.clowder.salon.service.client;

import com.clowder.salon.dto.request.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("user-service")
public interface UserClient {

  @GetMapping("/{userId}")
  public ResponseEntity<UserDTO> getUserById(@PathVariable Long userId);

  @GetMapping("/profiles")
  public ResponseEntity<UserDTO> getUserProfile(@RequestHeader("Authorization") String jwt);
}
