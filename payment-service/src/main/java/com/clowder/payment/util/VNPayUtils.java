package com.clowder.payment.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class VNPayUtils {

  private static final DateTimeFormatter VN_DATE_FMT =
      DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

  /**
   * Tạo HMAC-SHA512 signature theo yêu cầu VNPay.
   * Params phải được sort theo key alphabet, encode URL rồi join bằng '&'.
   */
  public static String buildSecureHash(String secretKey, Map<String, String> params) {
    // Sort theo key (TreeMap tự sort)
    TreeMap<String, String> sorted = new TreeMap<>(params);

    StringBuilder hashData = new StringBuilder();
    for (Map.Entry<String, String> entry : sorted.entrySet()) {
      if (entry.getValue() != null && !entry.getValue().isEmpty()) {
        hashData.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
                .append('=')
                .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                .append('&');
      }
    }
    // Remove trailing '&'
    if (hashData.length() > 0) {
      hashData.deleteCharAt(hashData.length() - 1);
    }

    return hmacSha512(secretKey, hashData.toString());
  }

  /**
   * Build query string (URL-encoded) từ params map, đã append secure hash.
   */
  public static String buildQueryString(String secretKey, Map<String, String> params) {
    String secureHash = buildSecureHash(secretKey, params);

    TreeMap<String, String> sorted = new TreeMap<>(params);
    StringBuilder query = new StringBuilder();
    for (Map.Entry<String, String> entry : sorted.entrySet()) {
      if (entry.getValue() != null && !entry.getValue().isEmpty()) {
        query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII))
             .append('=')
             .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
             .append('&');
      }
    }
    query.append("vnp_SecureHash=").append(secureHash);
    return query.toString();
  }

  /**
   * HMAC-SHA512 implementation.
   */
  public static String hmacSha512(String key, String data) {
    try {
      Mac mac = Mac.getInstance("HmacSHA512");
      SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
      mac.init(secretKey);
      byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException("Lỗi tạo HMAC-SHA512 cho VNPay", e);
    }
  }

  /**
   * Lấy thời gian hiện tại theo format VNPay: yyyyMMddHHmmss (GMT+7).
   */
  public static String getCurrentDateTime() {
    return LocalDateTime.now().format(VN_DATE_FMT);
  }

  /**
   * Lấy thời gian hết hạn (mặc định +15 phút) theo format VNPay.
   */
  public static String getExpireDateTime(int minutesFromNow) {
    return LocalDateTime.now().plusMinutes(minutesFromNow).format(VN_DATE_FMT);
  }

  /**
   * Xác minh secure hash từ VNPay callback.
   * Loại bỏ key vnp_SecureHash và vnp_SecureHashType trước khi hash.
   */
  public static boolean verifySignature(String secretKey, Map<String, String> params) {
    String receivedHash = params.get("vnp_SecureHash");
    if (receivedHash == null) return false;

    // Clone và loại bỏ hash fields
    Map<String, String> verifyParams = new TreeMap<>(params);
    verifyParams.remove("vnp_SecureHash");
    verifyParams.remove("vnp_SecureHashType");

    String calculatedHash = buildSecureHash(secretKey, verifyParams);
    return calculatedHash.equalsIgnoreCase(receivedHash);
  }
}
