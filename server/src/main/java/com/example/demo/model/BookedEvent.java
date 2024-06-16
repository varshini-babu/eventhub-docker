package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long booking_id;
    private int booking_no_of_tickets;
    private String booking_user_name;
    private String booking_user_email;
    private String booking_confirmation_code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;


    public BookedEvent(String booking_confirmation_code) {
        this.booking_confirmation_code = booking_confirmation_code;
    }

    public BookedEvent(int booking_no_of_tickets, String booking_user_name, String booking_user_email) {
        this.booking_no_of_tickets = booking_no_of_tickets;
        this.booking_user_name = booking_user_name;
        this.booking_user_email = booking_user_email;
    }

}
