package com.example.demo.service;

import com.example.demo.exception.InvalidBookingRequestException;
import com.example.demo.model.BookedEvent;

import com.example.demo.model.Event;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService{


    private final BookingRepository bookingRepository;
    private final IEventService eventService;
    @Autowired
    private EventRepository eventRepository;



    public List<BookedEvent> getAllBookingByEventId(Long event_id) {
        return bookingRepository.findByEventId(event_id);
    }


    @Override
    public List<BookedEvent> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public BookedEvent findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode);
    }
     @Override
    public void cancelBooking(Long bookingId) {
        Optional<BookedEvent> booking = bookingRepository.findById(bookingId);
        Event event = booking.get().getEvent();
         event.setEvent_max_of_tickets(event.getEvent_max_of_tickets() + booking.get().getBooking_no_of_tickets());
         eventRepository.save(event);
        bookingRepository.deleteById(bookingId);
    }


    @Override
    public String saveBooking(Long eventId, BookedEvent bookingRequest) {
        Event event = eventService.getEventById(eventId).get();
        int available_tickets = event.getEvent_max_of_tickets();
        if (available_tickets >= bookingRequest.getBooking_no_of_tickets()) {
            available_tickets -= bookingRequest.getBooking_no_of_tickets();
            event.setEvent_max_of_tickets(available_tickets);
            event.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        } else {
            throw new InvalidBookingRequestException("Sorry, Not enough tickets available. Only" + available_tickets +" tickets are available");
        }
        return bookingRequest.getBooking_confirmation_code();
    }


}


