package com.clowder.booking.controller;

import com.clowder.booking.dto.request.BookingRequest;
import com.clowder.booking.dto.request.BookingSlotDTO;
import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.dto.request.ServiceDTO;
import com.clowder.booking.dto.request.UserDTO;
import com.clowder.booking.dto.response.BookingDTO;
import com.clowder.booking.dto.response.PaymentLinkResponse;
import com.clowder.booking.dto.response.SalonReport;
import com.clowder.booking.enums.BookingStatus;
import com.clowder.booking.enums.PaymentMethod;
import com.clowder.booking.mapper.BookingMapper;
import com.clowder.booking.model.Booking;
import com.clowder.booking.service.BookingService;
import com.clowder.booking.service.client.PaymentClient;
import com.clowder.booking.service.client.SalonClient;
import com.clowder.booking.service.client.ServiceOfferingClient;
import com.clowder.booking.service.client.UserClient;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService bookingService;
  private final SalonClient salonClient;
  private final UserClient userClient;
  private final ServiceOfferingClient serviceOfferingClient;
  private final PaymentClient paymentClient;

  @PostMapping
  public ResponseEntity<PaymentLinkResponse> createBooking(
      @RequestParam Long salonId,
      @RequestParam PaymentMethod paymentMethod,
      @RequestBody @Valid BookingRequest bookingRequest,
      @RequestHeader("Authorization") String jwt)
      throws Exception {

    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();

    SalonDTO salon = salonClient.getSalonById(salonId).getBody();

    Set<ServiceDTO> serviceDTOSet =
        Collections.singleton(
            serviceOfferingClient.getServicesById(bookingRequest.getServicesIds()).getBody());

    Booking booking = bookingService.createBooking(bookingRequest, userDTO, salon, serviceDTOSet);

    BookingDTO bookingDTO = BookingMapper.toDto(booking);
    PaymentLinkResponse res =
        paymentClient.createPaymentLink(bookingDTO, paymentMethod, jwt).getBody();

    return ResponseEntity.ok(res);
  }

  @GetMapping("/customer")
  public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer(
      @RequestHeader("Authorization") String jwt) {
    UserDTO userDTO = userClient.getUserProfile(jwt).getBody();

    if (userDTO == null) {
      return ResponseEntity.notFound().build();
    }

    List<Booking> bookings = bookingService.getBookingsByCustomer(userDTO.getId());

    return ResponseEntity.ok(getBookingDTOs(bookings));
  }

  private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
    return bookings.stream().map(BookingMapper::toDto).collect(Collectors.toSet());
  }

  @GetMapping("/salon")
  public ResponseEntity<Set<BookingDTO>> getBookingsBySalon(
      @RequestHeader("Authorization") String jwt) {
    List<SalonDTO> salons = salonClient.getSalonsByOwnerId(jwt).getBody();
    if (salons == null || salons.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    SalonDTO salonDTO = salons.get(0);

    List<Booking> bookings = bookingService.getBookingsBySalon(salonDTO.getId());

    return ResponseEntity.ok(getBookingDTOs(bookings));
  }

  @GetMapping("/{bookingId}")
  public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long bookingId) {

    Booking booking = bookingService.getBookingById(bookingId);

    return ResponseEntity.ok(BookingMapper.toDto(booking));
  }

  @PutMapping("/{bookingId}/status")
  public ResponseEntity<BookingDTO> updateBookingStatus(
      @PathVariable Long bookingId, @RequestParam BookingStatus bookingStatus) {

    Booking booking = bookingService.updateBooking(bookingId, bookingStatus);

    return ResponseEntity.ok(BookingMapper.toDto(booking));
  }

  @GetMapping("/slots/salon/{salonId}/date/{date}")
  public ResponseEntity<List<BookingSlotDTO>> getBookedSlot(
      @PathVariable Long salonId, @PathVariable(required = false) LocalDate date) {

    List<Booking> bookings = bookingService.getBookingsByDate(date, salonId);

    List<BookingSlotDTO> slotsDTO =
        bookings.stream()
            .map(
                booking -> {
                  BookingSlotDTO bookingSlotDTO = new BookingSlotDTO();
                  bookingSlotDTO.setStartTime(booking.getStartTime());
                  bookingSlotDTO.setEndTime(booking.getEndTime());
                  return bookingSlotDTO;
                })
            .toList();
    return ResponseEntity.ok(slotsDTO);
  }

  @GetMapping("/report")
  public ResponseEntity<SalonReport> getSalonReport(@RequestHeader("Authorization") String jwt) {

    List<SalonDTO> salons = salonClient.getSalonsByOwnerId(jwt).getBody();
    if (salons == null || salons.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    SalonDTO salonDTO = salons.get(0);

    SalonReport report = bookingService.getSalonReport(salonDTO.getId());

    return ResponseEntity.ok(report);
  }
}
