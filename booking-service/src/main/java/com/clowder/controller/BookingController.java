package com.clowder.controller;

import com.clowder.dto.request.BookingDTO;
import com.clowder.dto.request.BookingRequest;
import com.clowder.dto.request.BookingSlotDTO;
import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.ServiceDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.enums.BookingStatus;
import com.clowder.mapper.BookingMapper;
import com.clowder.model.Booking;
import com.clowder.model.SalonReport;
import com.clowder.service.BookingService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService bookingService;

  @PostMapping
  public ResponseEntity<Booking> createBooking(
      @RequestParam Long salonId, @RequestBody BookingRequest bookingRequest) throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1L);

    SalonDTO salon = new SalonDTO();
    salon.setId(salonId);
    salon.setOpeningTime(LocalTime.of(8, 0));      // 8:00 AM
    salon.setClosingTime(LocalTime.of(22, 0));     // 10:00 PM

    Set<ServiceDTO> serviceDTOSet = new HashSet<>();

    ServiceDTO serviceDTO = new ServiceDTO();
    serviceDTO.setId(1L);
    serviceDTO.setPrice(399);
    serviceDTO.setDuration(60);
    serviceDTO.setName("Haircut");

    serviceDTOSet.add(new ServiceDTO());

    Booking booking = bookingService.createBooking(bookingRequest, userDTO, salon, serviceDTOSet);

    return ResponseEntity.ok(booking);
  }

  @GetMapping("/customer")
  public ResponseEntity<Set<BookingDTO>> getBookingsByCustomer() {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1L);

    List<Booking> bookings = bookingService.getBookingsByCustomer(1L);

    return ResponseEntity.ok(getBookingDTOs(bookings));
  }

  private Set<BookingDTO> getBookingDTOs(List<Booking> bookings) {
    return bookings.stream().map(BookingMapper::toDTO).collect(Collectors.toSet());
  }

  @GetMapping("/salon")
  public ResponseEntity<Set<BookingDTO>> getBookingsBySalon() {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1L);

    List<Booking> bookings = bookingService.getBookingsBySalon(1L);

    return ResponseEntity.ok(getBookingDTOs(bookings));
  }

  @GetMapping("/{bookingId}")
  public ResponseEntity<BookingDTO> getBookingsById(@PathVariable Long bookingId) {

    Booking booking = bookingService.getBookingById(bookingId);

    return ResponseEntity.ok(BookingMapper.toDTO(booking));
  }

  @PutMapping("/{bookingId}/status")
  public ResponseEntity<BookingDTO> updateBookingStatus(
      @PathVariable Long bookingId, @RequestParam BookingStatus bookingStatus) {

    Booking booking = bookingService.updateBooking(bookingId, bookingStatus);

    return ResponseEntity.ok(BookingMapper.toDTO(booking));
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
  public ResponseEntity<SalonReport> getSalonReport() {

    SalonReport report = bookingService.getSalonReport(1L);

    return ResponseEntity.ok(report);
  }
}
