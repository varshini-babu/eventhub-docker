package com.example.demo.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class EventResponse {

    private Long event_id;
    private String event_name;
    private String event_venue;
    private LocalDate event_date;
    private Time event_time;
    private String event_description;
    private int event_ticket_price;
    private int event_max_of_tickets;
    private String event_photo;
    private List<BookingResponse> bookings;

    public EventResponse(Long event_id, String event_name, LocalDate event_date, String event_description, Time event_time, String event_venue, int event_ticket_price, int event_max_of_tickets, byte[] bookingInfo) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_date = event_date;
        this.event_description = event_description;
        this.event_time = event_time;
        this.event_venue = event_venue;
        this.event_ticket_price = event_ticket_price;
        this.event_max_of_tickets = event_max_of_tickets;
    }

    public EventResponse(Long event_id, String event_name, String event_venue, LocalDate event_date, Time event_time, String event_description, int event_ticket_price, int event_max_of_tickets, byte[] event_photo_bytes) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_venue = event_venue;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_description = event_description;
        this.event_ticket_price = event_ticket_price;
        this.event_max_of_tickets = event_max_of_tickets;
        this.event_photo = event_photo_bytes != null ? Base64.encodeBase64String(event_photo_bytes): null;
        //this.bookings = bookings;
    }


    public EventResponse(Long event_id, String event_name, String event_venue, LocalDate event_date, Time event_time, String event_description, int event_ticket_price,int event_max_of_tickets) {
        this.event_id = event_id;
        this.event_name = event_name;
        this.event_venue = event_venue;
        this.event_date = event_date;
        this.event_time = event_time;
        this.event_description = event_description;
        this.event_ticket_price = event_ticket_price;
        this.event_max_of_tickets = event_max_of_tickets;
        //this.event_photo = event_photo_bytes != null ? Base64.encodeBase64String(event_photo_bytes): null;
        //this.bookings = bookings;
    }
    public EventResponse(Long event_id) {
        this.event_id = event_id;
    }
}
