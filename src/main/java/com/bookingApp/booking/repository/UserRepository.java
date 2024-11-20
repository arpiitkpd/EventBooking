package com.bookingApp.booking.repository;

import com.bookingApp.booking.model.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    User findByUsername(String username);
    User findByEmail(String email);
    void deleteByUsername(String username);
}
