package com.bookingApp.booking.service;

import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIml implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repo.findByUsername(username);

        if(user !=null){
            return new UserPrincipal(user);
        }
        System.out.println("user Not Found");
        throw  new UsernameNotFoundException("User is not found");
    }
}
