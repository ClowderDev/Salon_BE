package com.clowder.review.service.client;

import com.clowder.review.dto.request.SalonDTO;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("SALON-SERVICE")
public interface SalonClient {

  @GetMapping("/api/salons/owner")
  public ResponseEntity<List<SalonDTO>> getSalonsByOwnerId(
      @RequestHeader("Authorization") String jwt);

  @GetMapping("/api/salons/{salonId}")
  public ResponseEntity<SalonDTO> getSalonById(@PathVariable Long salonId);
}
