package com.example.demo.repository;
import com.example.demo.model.BookedEvent;
import com.example.demo.model.Event;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;


import java.util.List;
@EnableJpaRepositories
public interface BookingRepository extends JpaRepository<BookedEvent,Long>{
    void deleteById(Long bookingId);

    List<BookedEvent> findAll();

    @Query("SELECT b FROM BookedEvent b WHERE b.event.id = :event_id")
    List<BookedEvent> findByEventId(@Param("event_id") Long event_id);

    @Query("SELECT b FROM BookedEvent b WHERE b.booking_confirmation_code= :confirmationCode")
    BookedEvent findByBookingConfirmationCode(@Param("confirmationCode") String confirmationCode);


    //BookedEvent findByBookingConfirmationCode(String confirmationCode);


}
