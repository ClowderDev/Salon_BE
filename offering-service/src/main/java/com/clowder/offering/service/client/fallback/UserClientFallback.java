package com.clowder.offering.service.client.fallback;

import com.clowder.offering.service.client.UserClient;
import com.clowder.common.dto.shared.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserClientFallback implements UserClient {

    @Override
    public ResponseEntity<UserDTO> getUserById(Long userId) {
        log.warn("UserClient.getUserById circuit open in offering-service for userId={}", userId);
        return ResponseEntity.ok(null);
    }

    @Override
    public ResponseEntity<UserDTO> getUserProfile(String jwt) {
        log.warn("UserClient.getUserProfile circuit open in offering-service");
        return ResponseEntity.ok(null);
    }
}
