package com.clowder.booking.service.client.fallback;

import com.clowder.booking.service.client.ServiceOfferingClient;
import com.clowder.common.dto.shared.ServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ServiceOfferingClientFallback implements ServiceOfferingClient {

    @Override
    public ResponseEntity<ServiceDTO> getServicesById(java.util.Set<Long> id) {
        log.warn("ServiceOfferingClient.getServicesById circuit open for ids={}", id);
        return ResponseEntity.ok(null);
    }
}
