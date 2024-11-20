package com.bookingApp.booking.repository;

import com.bookingApp.booking.model.Event;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, ObjectId> {
    List<Event> findByName(String name);
    List<Event> findByLocation(String location);
    List<Event> findByDate(String date);

}
