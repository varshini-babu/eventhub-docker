package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Blob;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long event_id;
    private String event_name;
    private String event_venue;
    private LocalDate event_date;
    private Time event_time;
    private String event_description;
    private int event_ticket_price;
    private int event_max_of_tickets;
    @Lob
    private Blob event_photo;
    @OneToMany(mappedBy ="event", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedEvent> bookedEvents;

    public Event(){
        this.bookedEvents = new ArrayList<>();
    }

    public void setEvent_max_of_tickets(int event_max_of_tickets) {
        this.event_max_of_tickets = event_max_of_tickets;
    }

    public int getEvent_max_of_tickets() {
        return event_max_of_tickets;
    }

    public void addBooking(BookedEvent bookedEvent){
        if(bookedEvents == null){
            bookedEvents = new ArrayList<>();
        }
        bookedEvents.add(bookedEvent);
        bookedEvent.setEvent(this);

        String bookingCode = RandomStringUtils.randomNumeric(10);
        bookedEvent.setBooking_confirmation_code(bookingCode);
    }


}
