package com.bookingApp.booking.service;

import com.bookingApp.booking.dto.BookingRequest;
import com.bookingApp.booking.model.Booking;
import com.bookingApp.booking.model.Event;
import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.BookingRepository;
import com.bookingApp.booking.repository.EventRepository;
import com.bookingApp.booking.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;


    public Booking createBooking(BookingRequest booking) throws IllegalArgumentException {
        try{
            Event event = eventRepository.findById(booking.getEventId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid event ID: "));

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            User user = userRepository.findByUsername(authentication.getName());

            double requiredSeats = booking.getNumberOfSeats();
            double availableSeats = event.getTotalSeats();

            if(requiredSeats>availableSeats)  throw new IllegalArgumentException("Some requested seats are not available: ");

            event.setTotalSeats(availableSeats-requiredSeats);
            eventRepository.save(event);

            Booking book = new Booking();
            book.setUser(user);
            book.setEvent(event);
            book.setSeats(booking.getNumberOfSeats());
            book.setPaymentStatus(false);
            Booking dbBook = bookingRepository.save(book);
            String subject = "Booking Confirmation: Your Event Details [Booking ID: " + dbBook.getId() + "]";
            emailService.sendEmail(user.getEmail(), subject, createEmailBody(dbBook));
            return dbBook;

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }


    public boolean deleteBookingById(ObjectId id) throws IllegalArgumentException {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));

        Event event = booking.getEvent();
        double bookingSeats = booking.getSeats();
        event.setTotalSeats(event.getTotalSeats()+bookingSeats);

        eventRepository.save(event);
        bookingRepository.deleteById(id);

        return true;

    }

    public List<Booking> getUserBooking() throws IllegalArgumentException{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByUsername(authentication.getName());
        if(user== null) throw new IllegalArgumentException("User is not found");

        List<Booking> userBooking = bookingRepository.findByUser(user);
        return userBooking;
    }

    private String createEmailBody(Booking booking) {
        return
                "Booking Confirmation"+
                "Dear " + booking.getUser().getUsername() +
                "Your booking has been confirmed:</p>" +

                "Booking ID: " + booking.getId() +
                "Event: " + booking.getEvent().getName() +
                "Date: " + booking.getEvent().getDate() +
                "Time: " + booking.getEvent().getDate()+
                "Seats: " + booking.getSeats() +
                "Venue: " + booking.getEvent().getLocation() +

                "Looking forward to seeing you at the event!" ;
    }
}
