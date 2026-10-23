package com.shashi.service;

import com.shashi.entity.BookingHistory;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<BookingHistory> bookTicket(String mailId, Long trNo, int seatCount, LocalDate journeyDate, String fromStn, String toStn);

    List<BookingHistory> getAllBookingsByCustomer(String mailId);
}
