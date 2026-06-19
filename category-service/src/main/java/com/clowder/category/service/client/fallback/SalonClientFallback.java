package com.clowder.category.service.client.fallback;

import com.clowder.category.service.client.SalonClient;
import com.clowder.common.dto.shared.SalonDTO;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SalonClientFallback implements SalonClient {

    @Override
    public ResponseEntity<List<SalonDTO>> getSalonsByOwnerId(String jwt) {
        log.warn("SalonClient.getSalonsByOwnerId circuit open in category-service — returning empty list");
        return ResponseEntity.ok(Collections.emptyList());
    }
}
