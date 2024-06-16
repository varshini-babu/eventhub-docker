package com.example.demo.response;

import com.example.demo.model.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class BookingResponse {

    private Long booking_id;
    private int booking_no_of_tickets;
    private String booking_user_name;
    private String booking_user_email;
    private String booking_confirmation_code;
    private EventResponse event;

    public BookingResponse(Long booking_id, int booking_no_of_tickets, String booking_confirmation_code) {
        this.booking_id = booking_id;
        this.booking_no_of_tickets = booking_no_of_tickets;
        this.booking_confirmation_code = booking_confirmation_code;
    }


    public BookingResponse(Long bookingId, String bookingUserName, String bookingUserEmail, int bookingNoOfTickets, String bookingConfirmationCode, EventResponse event) {
        booking_id = bookingId;
        booking_confirmation_code=bookingConfirmationCode;
        booking_no_of_tickets = bookingNoOfTickets;
        booking_user_name=bookingUserName;
        booking_user_email=bookingUserEmail;
        this.event = event;
    }


}
