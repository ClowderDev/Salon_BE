package com.clowder.payment.service.client;

import com.clowder.payment.dto.request.SalonDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("salon-service")
public interface SalonClient {

  @GetMapping("/api/salons/owner")
  public ResponseEntity<List<SalonDTO>> getSalonsByOwnerId(
      @RequestHeader("Authorization") String jwt);

  @GetMapping("/api/salons/{salonId}")
  public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId);
}
