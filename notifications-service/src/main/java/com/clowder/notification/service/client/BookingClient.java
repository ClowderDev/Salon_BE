package com.clowder.notification.service.client;

import com.clowder.notification.dto.request.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("booking-service")
public interface BookingClient {

  @GetMapping("/api/bookings/{bookingId}")
  public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long bookingId);
}
