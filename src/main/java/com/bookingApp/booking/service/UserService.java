package com.bookingApp.booking.service;

import com.bookingApp.booking.model.User;
import com.bookingApp.booking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTService jwtService;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public boolean registerUser(User user) {
        if(userRepository.findByEmail(user.getEmail()) !=null){
            return false;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return true;
    }

    public void saveUser(User user){
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    public String verify(User user) throws Exception {
        try{

            Authentication authentication =
                    authenticationManager.authenticate((new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));

            if(authentication.isAuthenticated()){

                return jwtService.generateToken(user.getUsername());

            }
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return "";
    }


    public User findByUserName(String userName) {
        try {
            User user = userRepository.findByUsername(userName);
            if (user != null) return user;
            else return null;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void deleteUser(String username) {
        userRepository.deleteByUsername(username);
    }
}
