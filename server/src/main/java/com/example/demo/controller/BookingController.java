package com.example.demo.controller;

import com.example.demo.exception.InvalidBookingRequestException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.BookedEvent;
import com.example.demo.model.Event;
import com.example.demo.response.BookingResponse;
import com.example.demo.response.EventResponse;
import com.example.demo.service.IBookingService;
import com.example.demo.service.IEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final IBookingService bookingService;
    private final IEventService eventService;



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("all-bookings")

    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedEvent> bookedEvents = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedEvent bookedEvent : bookedEvents) {
            BookingResponse bookingResponse = getBookingResponse(bookedEvent);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }



    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingByConfirmationCode (@PathVariable String confirmationCode){
        try{
            BookedEvent bookedEvent = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(bookedEvent);
            return ResponseEntity.ok(bookingResponse);
        }catch(ResourceNotFoundException ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());

        }
    }

    @PostMapping("/event/{event_id}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable Long event_id,
                                         @RequestBody BookedEvent bookingRequest){
        try{
            String confirmationCode = bookingService.saveBooking(event_id,bookingRequest);
            return ResponseEntity.ok("Event booked successfully, Your booking confirmation code is :"+confirmationCode);
        }catch(InvalidBookingRequestException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long bookingId){

        bookingService.cancelBooking(bookingId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

   /* private BookingResponse getBookingResponse(BookedEvent bookedEvent) {

        Event theEvent = eventService.getEventById(bookedEvent.getEvent().getEvent_id()).get();
        EventResponse event = new EventResponse(
                theEvent.getEvent_id(),
                theEvent.getEvent_name(),
                theEvent.getEvent_description(),
                theEvent.getEvent_date(),
                theEvent.getEvent_time(),
                theEvent.getEvent_venue(),
                theEvent.getEvent_ticket_price(),
                theEvent.getEvent_max_of_tickets()
        );
                return new BookingResponse(bookedEvent.getBooking_id(), bookedEvent.getBooking_user_name(),
                                            bookedEvent.getBooking_user_email(), bookedEvent.getBooking_no_of_tickets(), bookedEvent.getBooking_confirmation_code(),event);
    }*/
   private BookingResponse getBookingResponse(BookedEvent bookedEvent) {
       Optional<Event> optionalEvent = eventService.getEventById(bookedEvent.getEvent().getEvent_id());

       if (optionalEvent.isPresent()) {
           Event theEvent = optionalEvent.get();
           EventResponse eventResponse = new EventResponse(
                   theEvent.getEvent_id(),
                   theEvent.getEvent_name(),
                   theEvent.getEvent_description(),
                   theEvent.getEvent_date(),
                   theEvent.getEvent_time(),
                   theEvent.getEvent_venue(),
                   theEvent.getEvent_ticket_price(),
                   theEvent.getEvent_max_of_tickets()
           );
           System.out.println("Event details: " + eventResponse);

           return new BookingResponse(
                   bookedEvent.getBooking_id(),
                   bookedEvent.getBooking_user_name(),
                   bookedEvent.getBooking_user_email(),
                   bookedEvent.getBooking_no_of_tickets(),
                   bookedEvent.getBooking_confirmation_code(),
                   eventResponse
           );
       } else {
           // Handle the case where the event is not found, e.g., throw an exception or return a response with an error
           throw new ResourceNotFoundException("Event not found for id: " + bookedEvent.getEvent().getEvent_id());
       }
   }



}
