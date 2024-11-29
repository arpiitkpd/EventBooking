package com.bookingApp.booking.repository;

import com.bookingApp.booking.model.Booking;
import com.bookingApp.booking.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends MongoRepository<Booking, ObjectId> {
    List<Booking> findByUser(User user);
    List<Booking> findByEventId(String eventId);

}
