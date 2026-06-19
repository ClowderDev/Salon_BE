package com.clowder.review.service.client.fallback;

import com.clowder.review.service.client.SalonClient;
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
        log.warn("SalonClient.getSalonsByOwnerId circuit open in review-service");
        return ResponseEntity.ok(Collections.emptyList());
    }

    @Override
    public ResponseEntity<SalonDTO> getSalonById(Long salonId) {
        log.warn("SalonClient.getSalonById circuit open in review-service for salonId={}", salonId);
        return ResponseEntity.ok(null);
    }
}
