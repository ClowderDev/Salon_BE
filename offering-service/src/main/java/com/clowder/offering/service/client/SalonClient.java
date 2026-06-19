package com.clowder.offering.service.client;

import com.clowder.offering.service.client.fallback.SalonClientFallback;
import com.clowder.common.dto.shared.SalonDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "salon-service", fallback = SalonClientFallback.class)
public interface SalonClient {

  @GetMapping("/api/salons/owner")
  public ResponseEntity<List<SalonDTO>> getSalonsByOwnerId(
      @RequestHeader("Authorization") String jwt);
}
