package com.example.demo.service;
import com.example.demo.response.EventResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.exception.InternalServerException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class EventService implements IEventService {

    private static final Logger logger = Logger.getLogger(EventService.class.getName());
    private final EventRepository eventRepository;

    @Override
    public Event addNewEvent(MultipartFile file, String eventName, LocalDate eventDate, String eventDescription, Time eventTime, String eventVenue, int eventTicketPrice, int eventMaxOfTickets) throws SQLException, IOException {
        logger.info("Adding new event with details: " +
                "eventName=" + eventName + ", " +
                "eventDate=" + eventDate + ", " +
                "eventDescription=" + eventDescription + ", " +
                "eventTime=" + eventTime + ", " +
                "eventVenue=" + eventVenue + ", " +
                "eventTicketPrice=" + eventTicketPrice + ", " +
                "eventMaxOfTickets=" + eventMaxOfTickets);

        Event event = new Event();
        event.setEvent_name(eventName);
        event.setEvent_date(eventDate);
        event.setEvent_description(eventDescription);
        event.setEvent_time(eventTime);
        event.setEvent_venue(eventVenue);
        event.setEvent_ticket_price(eventTicketPrice);
        event.setEvent_max_of_tickets(eventMaxOfTickets);

        if (!file.isEmpty()) {
            byte[] event_photo_bytes = file.getBytes();
            Blob photoBlob = new SerialBlob(event_photo_bytes);
            event.setEvent_photo(photoBlob);
        }

        return eventRepository.save(event);
    }

    @Override
    public List<String> getAllEventName() {
        return eventRepository.findDistinctEventName();
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public byte[] getEventPhotoByEventId(Long eventId) throws SQLException {
        Optional<Event> theEvent = eventRepository.findById(eventId);
        if (theEvent.isPresent()) {
            Blob photoBlob = theEvent.get().getEvent_photo();
            if (photoBlob != null) {
                return photoBlob.getBytes(1, (int) photoBlob.length());
            }
            return new byte[0];
        } else {
            throw new ResourceNotFoundException("Sorry, Event not found");
        }
    }
    @Override
    public LocalDate getEventDateByEventId(Long eventId) {
        Optional<Event> theEvent = eventRepository.findById(eventId);
        if (theEvent.isPresent()) {
            LocalDate date = theEvent.get().getEvent_date();
            return date;
        } else {
            throw new ResourceNotFoundException("Sorry, Event not found");
        }
    }


    @Override
    public void deleteEvent(Long eventId) {
        Optional<Event> theEvent = eventRepository.findById(eventId);
        if(theEvent.isPresent()){
            eventRepository.deleteById(eventId);
        }
    }



    @Transactional
    @Override
    public Event updateEvent(Long eventId, String eventName, LocalDate eventDate, String eventDescription, LocalTime eventTime, String eventVenue, Integer eventTicketPrice, Integer eventMaxOfTickets, @RequestParam("event_photo") MultipartFile eventPhoto) throws IOException, InternalServerException{
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        if (eventName != null) event.setEvent_name(eventName);
        if (eventDate != null) event.setEvent_date(eventDate);
        if (eventDescription != null) event.setEvent_description(eventDescription);
        if (eventTime != null) event.setEvent_time(Time.valueOf(eventTime));
        if (eventVenue != null) event.setEvent_venue(eventVenue);
        if (eventTicketPrice != null) {
            event.setEvent_ticket_price(eventTicketPrice.intValue());
        }
        if (eventMaxOfTickets != null) {
            event.setEvent_max_of_tickets(eventMaxOfTickets.intValue());
        }
        if (eventPhoto != null && !eventPhoto.isEmpty()) {
            byte[] event_photo_bytes = eventPhoto.getBytes();
            if (event_photo_bytes.length > 0) {
                try {
                    Blob photoBlob = new SerialBlob(event_photo_bytes);
                    event.setEvent_photo(photoBlob);
                } catch (SQLException ex) {
                    throw new InternalServerException("Error updating event");
                }
            }
        }

        return eventRepository.save(event);
    }



    @Override
    public Optional<Event> getEventById(Long eventId) {
        return Optional.of(eventRepository.findById(eventId).get());
    }





}
