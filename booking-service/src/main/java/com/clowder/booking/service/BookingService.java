package com.clowder.booking.service;

import com.clowder.booking.dto.request.BookingRequest;
import com.clowder.booking.dto.request.PaymentOrder;
import com.clowder.booking.dto.request.SalonDTO;
import com.clowder.booking.dto.request.ServiceDTO;
import com.clowder.booking.dto.request.UserDTO;
import com.clowder.booking.dto.response.SalonReport;
import com.clowder.booking.enums.BookingStatus;
import com.clowder.booking.model.Booking;
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

  Booking bookingSuccess(PaymentOrder paymentOrder);
}
