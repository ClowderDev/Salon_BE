package com.clowder.service;

import com.clowder.dto.request.BookingRequest;
import com.clowder.dto.request.SalonDTO;
import com.clowder.dto.request.ServiceDTO;
import com.clowder.dto.request.UserDTO;
import com.clowder.enums.BookingStatus;
import com.clowder.model.Booking;
import com.clowder.model.PaymentOrder;
import com.clowder.model.SalonReport;
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
