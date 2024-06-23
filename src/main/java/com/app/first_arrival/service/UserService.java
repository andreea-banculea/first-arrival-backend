package com.app.first_arrival.service;

import com.app.first_arrival.entities.users.User;
import com.app.first_arrival.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    public User createUser(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder().encode(user.getPassword()));
        newUser.setPhoneNumber(user.getPhoneNumber());
        newUser.setLocation(user.getLocation());
        newUser.setRole(user.getRole());
        newUser.setCertification(user.getCertification());
        newUser.setVolunteerLevel(user.getVolunteerLevel());

        return userRepository.save(newUser);
    }

    public User getLoggedInUser(String principal) {
        return userRepository.findByEmail(principal).orElse(null);
    }

    @Transactional
    public Optional<User> updateUser( User user) {
        return userRepository.findById(user.getId()).map(existingUser -> {
            existingUser.setName(user.getName());
            existingUser.setEmail(user.getEmail());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder().encode(user.getPassword()));
            }
            existingUser.setLocation(user.getLocation());
            existingUser.setRole(user.getRole());
            existingUser.setCertification(user.getCertification());
            existingUser.setVolunteerLevel(user.getVolunteerLevel());
            return userRepository.save(existingUser);
        });
    }
}
