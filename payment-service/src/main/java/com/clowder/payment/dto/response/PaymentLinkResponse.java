package com.clowder.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentLinkResponse {

  @JsonProperty("payment_link_url")
  private String paymentLinkUrl;

  @JsonProperty("payment_link_id")
  private String paymentLinkId;
}
