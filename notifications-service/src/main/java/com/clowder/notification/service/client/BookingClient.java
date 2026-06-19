package com.clowder.notification.service.client;

import com.clowder.notification.service.client.fallback.BookingClientFallback;
import com.clowder.common.dto.shared.BookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", fallback = BookingClientFallback.class)
public interface BookingClient {

  @GetMapping("/api/bookings/{bookingId}")
  public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long bookingId);
}
