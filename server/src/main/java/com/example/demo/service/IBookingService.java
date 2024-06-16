package com.example.demo.service;

import com.example.demo.model.BookedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface IBookingService {

    List<BookedEvent> getAllBookings();

    BookedEvent findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long eventId, BookedEvent bookingRequest);

    void cancelBooking(Long bookingId);
}
