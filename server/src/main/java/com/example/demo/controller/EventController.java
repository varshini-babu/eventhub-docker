package com.example.demo.controller;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.BookedEvent;
import com.example.demo.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.apache.tomcat.util.codec.binary.Base64;
import com.example.demo.exception.PhotoRetrievalException;

import java.sql.Blob;
import java.sql.SQLOutput;
import java.sql.Time;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.io.IOException;

import com.example.demo.model.Event;
import com.example.demo.response.EventResponse;
import com.example.demo.service.IEventService;
import lombok.RequiredArgsConstructor;

import javax.sql.rowset.serial.SerialBlob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private static final Logger logger = Logger.getLogger(EventController.class.getName());
    private final IEventService eventService;
    private final BookingService bookingService;

    @PostMapping("/add/new-event")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EventResponse> addNewEvent(
            @RequestParam("event_photo") MultipartFile event_photo,
            @RequestParam("event_name") String event_name,
            @RequestParam("event_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate event_date,
            @RequestParam("event_description") String event_description,
            @RequestParam("event_time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime event_time,
            @RequestParam("event_venue") String event_venue,
            @RequestParam("event_ticket_price") int event_ticket_price,
            @RequestParam("event_max_of_tickets") int event_max_of_tickets) throws SQLException, IOException {

        logger.info("Received new event request with details: " +
                "event_name=" + event_name + ", " +
                "event_date=" + event_date + ", " +
                "event_description=" + event_description + ", " +
                "event_time=" + event_time + ", " +
                "event_venue=" + event_venue + ", " +
                "event_ticket_price=" + event_ticket_price + ", " +
                "event_max_of_tickets=" + event_max_of_tickets);

        Time sqlTime = Time.valueOf(event_time);

        Event savedEvent = eventService.addNewEvent(event_photo, event_name, event_date, event_description, sqlTime, event_venue, event_ticket_price, event_max_of_tickets);


        byte[] photoBytes = event_photo.getBytes();

        EventResponse response = new EventResponse(savedEvent.getEvent_id(), savedEvent.getEvent_name(),
                savedEvent.getEvent_date(), savedEvent.getEvent_description(),
                savedEvent.getEvent_time(), savedEvent.getEvent_venue(),
                savedEvent.getEvent_ticket_price(), savedEvent.getEvent_max_of_tickets(), photoBytes);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @GetMapping("/event/name")
    public List<String> getEventName() {
        return eventService.getAllEventName();
    }

    @GetMapping("/all-events")
    public ResponseEntity<List<EventResponse>> getAllEvents() throws SQLException {
        List<Event> events = eventService.getAllEvents();
        List<EventResponse> eventResponses = new ArrayList<>();
        for (Event event : events) {
            byte[] photoBytes = eventService.getEventPhotoByEventId(event.getEvent_id());
            System.out.print("PhotoByte: " + photoBytes);
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                EventResponse eventResponse = getEventResponse(event);
                eventResponse.setEvent_photo(base64Photo);
                eventResponses.add(eventResponse);
            }
        }
        return ResponseEntity.ok(eventResponses);
    }



    @DeleteMapping("/delete/event/{eventId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/update/{eventId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable("eventId") Long event_id,
                                                     @RequestParam("event_name") String event_name,
                                                     @RequestParam("event_date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate event_date,
                                                     @RequestParam("event_description") String event_description,
                                                     @RequestParam("event_time") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime event_time,
                                                     @RequestParam("event_venue") String event_venue,
                                                     @RequestParam("event_ticket_price") Integer event_ticket_price,
                                                     @RequestParam("event_max_of_tickets") Integer event_max_of_tickets,
                                                     @RequestParam(required = false) MultipartFile event_photo) throws IOException, SQLException {


        byte[] photoBytes = event_photo != null && !event_photo.isEmpty() ? event_photo.getBytes() : eventService.getEventPhotoByEventId(event_id);

        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Time sqlTime = Time.valueOf(event_time);
        Event theEvent = eventService.updateEvent(event_id, event_name, event_date, event_description, sqlTime.toLocalTime(), event_venue, event_ticket_price, event_max_of_tickets, event_photo);
        theEvent.setEvent_photo(photoBlob);
        EventResponse eventResponse = getEventResponse(theEvent);
        return ResponseEntity.ok(eventResponse);


    }


    @GetMapping("/event/{eventId}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable("eventId") Long eventId) throws SQLException {
        Optional<Event> optionalEvent = eventService.getEventById(eventId);
        if (optionalEvent.isPresent()) {
            Event event = optionalEvent.get();
            EventResponse eventResponse = getEventResponse(event);
            byte[] photoBytes = eventService.getEventPhotoByEventId(eventId);
            System.out.print("PhotoByte: " + photoBytes);
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                eventResponse.setEvent_photo(base64Photo);
            }
            return ResponseEntity.ok(eventResponse);
        }else

        {
            throw new ResourceNotFoundException("Event not found");
        }
    }


    private EventResponse getEventResponse(Event event) throws SQLException {
        List<BookedEvent> bookedEvents = getAllBookingsByEventId(event.getEvent_id());


        byte[] photoBytes=null;
        Blob photoBlob = event.getEvent_photo();
        if(photoBlob != null){
            try{
                photoBytes = photoBlob.getBytes(1,(int) photoBlob.length());
            }catch(SQLException e){
                throw new PhotoRetrievalException("Error retrieving photo");
            }
        }
        return new EventResponse(event.getEvent_id(),
                event.getEvent_name(),
                event.getEvent_date(),
                event.getEvent_description(),
                event.getEvent_time(),
                event.getEvent_venue(),
                event.getEvent_ticket_price(),
                event.getEvent_max_of_tickets(), photoBytes);
    }

    private List<BookedEvent> getAllBookingsByEventId(Long eventId){
        return bookingService.getAllBookingByEventId(eventId);
    }
}
