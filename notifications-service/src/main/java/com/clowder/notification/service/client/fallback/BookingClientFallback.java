package com.clowder.notification.service.client.fallback;

import com.clowder.notification.service.client.BookingClient;
import com.clowder.common.dto.shared.BookingDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingClientFallback implements BookingClient {

    @Override
    public ResponseEntity<BookingDTO> getBookingsById(Long bookingId) {
        log.warn("BookingClient.getBookingsById circuit open in notification-service for bookingId={}", bookingId);
        return ResponseEntity.ok(null);
    }
}
