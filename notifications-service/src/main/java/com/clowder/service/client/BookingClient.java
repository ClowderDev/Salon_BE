package com.clowder.service.client;

import com.clowder.dto.request.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("BOOKING-SERVICE")
public interface BookingClient {

  @GetMapping("/api/bookings/{bookingId}")
  public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long bookingId);
}
