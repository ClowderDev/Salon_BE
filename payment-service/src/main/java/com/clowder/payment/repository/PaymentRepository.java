package com.clowder.payment.repository;

import com.clowder.payment.model.PaymentOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentOrder, Long> {

  PaymentOrder findByPaymentLinkId(String paymentLinkId);
}
