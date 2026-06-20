package com.clowder.payment.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class MoMoUtils {

  /**
   * Tạo HMAC-SHA256 signature theo yêu cầu MoMo v2.
   *
   * Raw hash format:
   * "accessKey=<>&amount=<>&extraData=<>&ipnUrl=<>&orderId=<>&orderInfo=<>&partnerCode=<>&redirectUrl=<>&requestId=<>&requestType=<>"
   */
  public static String buildRawHash(
      String accessKey,
      String amount,
      String extraData,
      String ipnUrl,
      String orderId,
      String orderInfo,
      String partnerCode,
      String redirectUrl,
      String requestId,
      String requestType) {

    return "accessKey=" + accessKey
        + "&amount=" + amount
        + "&extraData=" + extraData
        + "&ipnUrl=" + ipnUrl
        + "&orderId=" + orderId
        + "&orderInfo=" + orderInfo
        + "&partnerCode=" + partnerCode
        + "&redirectUrl=" + redirectUrl
        + "&requestId=" + requestId
        + "&requestType=" + requestType;
  }

  /**
   * HMAC-SHA256 implementation.
   */
  public static String hmacSha256(String secretKey, String data) {
    try {
      Mac mac = Mac.getInstance("HmacSHA256");
      SecretKeySpec keySpec = new SecretKeySpec(
          secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      mac.init(keySpec);
      byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException("Lỗi tạo HMAC-SHA256 cho MoMo", e);
    }
  }

  /**
   * Xác minh signature từ MoMo IPN callback.
   *
   * MoMo gửi về signature dựa trên raw hash của các fields nhất định.
   */
  public static boolean verifySignature(
      String secretKey,
      String accessKey,
      String amount,
      String extraData,
      String ipnUrl,
      String orderId,
      String orderInfo,
      String partnerCode,
      String redirectUrl,
      String requestId,
      String requestType,
      String receivedSignature) {

    String rawHash = buildRawHash(accessKey, amount, extraData, ipnUrl,
        orderId, orderInfo, partnerCode, redirectUrl, requestId, requestType);
    String calculatedSig = hmacSha256(secretKey, rawHash);
    return calculatedSig.equals(receivedSignature);
  }
}
