package com.clowder.booking.service.client.fallback;

import com.clowder.booking.service.client.PaymentClient;
import com.clowder.common.dto.shared.BookingDTO;
import com.clowder.common.dto.shared.PaymentLinkResponse;
import com.clowder.common.enums.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentClientFallback implements PaymentClient {

    @Override
    public ResponseEntity<PaymentLinkResponse> createPaymentLink(
            BookingDTO booking, PaymentMethod paymentMethod, String jwt) {
        log.error("PaymentClient.createPaymentLink circuit open — payment-service unavailable");
        return ResponseEntity.ok(null);
    }
}
