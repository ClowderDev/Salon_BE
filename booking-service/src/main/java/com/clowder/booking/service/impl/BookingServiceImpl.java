package com.clowder.booking.service.impl;

import com.clowder.booking.dto.request.BookingRequest;
import com.clowder.booking.dto.response.SalonReport;
import com.clowder.booking.model.Booking;
import com.clowder.booking.repository.BookingRepository;
import com.clowder.booking.service.BookingService;
import com.clowder.common.dto.shared.PaymentOrderDTO;
import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.ServiceDTO;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.BookingStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

  private final BookingRepository bookingRepository;

  @Override
  public Booking createBooking(
      BookingRequest booking, UserDTO user, SalonDTO salon, Set<ServiceDTO> serviceDTOSet)
      throws Exception {

    int totalDuration = serviceDTOSet.stream().mapToInt(ServiceDTO::getDuration).sum();

    LocalDateTime bookingStartTime = booking.getStartTime();
    LocalDateTime bookingEndTime = bookingStartTime.plusMinutes(totalDuration);

    Boolean isSlotAvailable = isTimeSlotAvailable(salon, bookingStartTime, bookingEndTime);

    int totalPrice = serviceDTOSet.stream().mapToInt(ServiceDTO::getPrice).sum();

    Set<Long> idList = serviceDTOSet.stream().map(ServiceDTO::getId).collect(Collectors.toSet());

    if (!isSlotAvailable) {
      throw new Exception("Slot not available, please choose another slot");
    }

    Booking newBooking = new Booking();
    newBooking.setCustomerId(user.getId());
    newBooking.setSalonId(salon.getId());
    newBooking.setServiceIds(idList);
    newBooking.setStatus(BookingStatus.PENDING);
    newBooking.setStartTime(booking.getStartTime());
    newBooking.setEndTime(bookingEndTime);
    newBooking.setTotalPrice(totalPrice);

    return bookingRepository.save(newBooking);
  }

  public Boolean isTimeSlotAvailable(
      SalonDTO salonDTO, LocalDateTime bookingStartTime, LocalDateTime bookingEndTime)
      throws Exception {

    List<Booking> existingBookings = getBookingsBySalon(salonDTO.getId());

    if (salonDTO.getOpeningTime() != null && salonDTO.getClosingTime() != null) {
      LocalDateTime salonOpenTime =
          salonDTO.getOpeningTime().atDate(bookingStartTime.toLocalDate());
      LocalDateTime salonCloseTime = salonDTO.getClosingTime().atDate(bookingEndTime.toLocalDate());

      if (bookingStartTime.isBefore(salonOpenTime) || bookingEndTime.isAfter(salonCloseTime)) {
        throw new Exception("Booking time is outside salon operating hours");
      }
    }

    for (Booking booking : existingBookings) {
      LocalDateTime existingBookingStartTime = booking.getStartTime();
      LocalDateTime existingBookingEndTime = booking.getEndTime();

      if (bookingStartTime.isBefore(existingBookingEndTime)
          && bookingEndTime.isAfter(existingBookingStartTime)) {
        throw new Exception("Slot not available, please choose another slot");
      }

      if (bookingStartTime.isEqual(existingBookingStartTime)
          || bookingEndTime.isEqual(existingBookingEndTime)) {
        throw new Exception("Slot not available, please choose another slot");
      }
    }

    return true;
  }

  @Override
  public List<Booking> getBookingsByCustomer(Long customerId) {
    return bookingRepository.findByCustomerId(customerId);
  }

  @Override
  public List<Booking> getBookingsBySalon(Long salonId) {
    return bookingRepository.findBySalonId(salonId);
  }

  @Override
  public Booking getBookingById(Long id) {
    return bookingRepository
        .findById(id)
        .orElseThrow(() -> new RuntimeException("Booking not found with id: " + id));
  }

  @Override
  public Booking updateBooking(Long bookingId, BookingStatus status) {
    Booking booking =
        bookingRepository
            .findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));

    booking.setStatus(status);
    return bookingRepository.save(booking);
  }

  @Override
  public List<Booking> getBookingsByDate(LocalDate date, Long salonId) {
    List<Booking> allBookings = getBookingsBySalon(salonId);

    if (date == null) {
      return allBookings;
    }

    return allBookings.stream()
        .filter(
            booking ->
                isSameDate(booking.getStartTime(), date) || isSameDate(booking.getEndTime(), date))
        .collect(Collectors.toList());
  }

  private boolean isSameDate(LocalDateTime dateTime, LocalDate date) {
    return dateTime.toLocalDate().isEqual(date);
  }

  @Override
  public SalonReport getSalonReport(Long salonId) {
    List<Booking> bookings = getBookingsBySalon(salonId);

    Double totalEarnings = bookings.stream().mapToDouble(Booking::getTotalPrice).sum();

    Integer totalBookings = bookings.size();

    List<Booking> cancelledBookings =
        bookings.stream()
            .filter(booking -> booking.getStatus().equals(BookingStatus.CANCELLED))
            .toList();

    Double totalRefund = cancelledBookings.stream().mapToDouble(Booking::getTotalPrice).sum();

    SalonReport salonReport = new SalonReport();
    salonReport.setSalonId(salonId);
    salonReport.setCancelledBookings(cancelledBookings.size());
    salonReport.setTotalBookings(totalBookings);
    salonReport.setTotalEarnings(totalEarnings);
    salonReport.setTotalRefund(totalRefund);
    return salonReport;
  }

  @Override
  public Booking bookingSuccess(PaymentOrderDTO paymentOrder) {

    Booking existedBooking = getBookingById(paymentOrder.getBookingId());
    existedBooking.setStatus(BookingStatus.CONFIRMED);
    return bookingRepository.save(existedBooking);
  }
}
