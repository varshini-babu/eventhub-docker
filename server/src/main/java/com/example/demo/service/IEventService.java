package com.example.demo.service;

import com.example.demo.exception.InternalServerException;
import com.example.demo.model.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface IEventService {
    Event addNewEvent(MultipartFile eventPhoto, String eventName, LocalDate eventDate, String eventDescription, Time eventTime, String eventVenue, int eventTicketPrice, int eventMaxOfTickets) throws SQLException, IOException;

    List<String> getAllEventName();

    List<Event> getAllEvents();

    byte[] getEventPhotoByEventId(Long eventId) throws SQLException;

    void deleteEvent(Long eventId);

    Event updateEvent(Long eventId, String eventName, LocalDate eventDate, String eventDescription, LocalTime eventTime, String eventVenue, Integer eventTicketPrice, Integer eventMaxOfTickets, MultipartFile eventPhoto) throws IOException, InternalServerException;


    Optional<Event> getEventById(Long eventId);

    LocalDate getEventDateByEventId(Long eventId);

    // getEventMaxOfTicketById(Long eventId);
}
