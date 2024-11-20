package com.bookingApp.booking.controllers;

import com.bookingApp.booking.dto.BookingRequest;
import com.bookingApp.booking.model.Booking;
import com.bookingApp.booking.service.BookingService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/booking")
public class BookingControllers {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/create-booking")
    public ResponseEntity<Object> createBooking(@RequestBody BookingRequest request) {

        Booking isBooked =  bookingService.createBooking(request);
        if(isBooked!=null) return ResponseEntity.status(HttpStatus.CREATED).body(isBooked);
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Booking interuptted");
    }

    @GetMapping("/user-bookings")
    public ResponseEntity<List<Booking>> getUserBooking(){
       List<Booking> bookingList = bookingService.getUserBooking();
       return ResponseEntity.status(HttpStatus.OK).body(bookingList);
    }

    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<Boolean> deleteBookingById(@PathVariable ObjectId id){
        boolean isDeleted =bookingService.deleteBookingById(id);
        return ResponseEntity.ok(isDeleted);
    }
}
