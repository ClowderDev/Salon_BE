package com.clowder.booking.service;

import com.clowder.booking.dto.request.BookingRequest;
import com.clowder.booking.dto.response.SalonReport;
import com.clowder.booking.model.Booking;
import com.clowder.common.dto.shared.PaymentOrderDTO;
import com.clowder.common.dto.shared.SalonDTO;
import com.clowder.common.dto.shared.ServiceDTO;
import com.clowder.common.dto.shared.UserDTO;
import com.clowder.common.enums.BookingStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface BookingService {

  Booking createBooking(
      BookingRequest booking, UserDTO user, SalonDTO salon, Set<ServiceDTO> serviceDTOSet)
      throws Exception;

  List<Booking> getBookingsByCustomer(Long customerId);

  List<Booking> getBookingsBySalon(Long salonId);

  Booking getBookingById(Long id);

  Booking updateBooking(Long bookingId, BookingStatus status);

  List<Booking> getBookingsByDate(LocalDate date, Long salonId);

  SalonReport getSalonReport(Long salonId);

  Booking bookingSuccess(PaymentOrderDTO paymentOrder);
}
