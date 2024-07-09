package com.app.first_arrival.controller;

import com.app.first_arrival.entities.dto.VolunteerRequest;
import com.app.first_arrival.entities.dto.VolunteerRequestDTO;
import com.app.first_arrival.entities.users.User;
import com.app.first_arrival.service.UserService;
import com.app.first_arrival.util.helpers.TokenRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        logger.info("Received user: " + user);
        User createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return userService.updateUser(user)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/volunteer-request")
    public void volunteerRequest(@RequestBody VolunteerRequest volunteerRequest) {
        userService.volunteerRequest(volunteerRequest);
    }

    @PutMapping("/accept/{id}")
    public void acceptVolunteerRequest(@PathVariable Long id) {
        userService.acceptVolunteerRequest(id);
    }

    @PutMapping("/decline/{id}")
    public void declineVolunteerRequest(@PathVariable Long id) {
        userService.declineVolunteerRequest(id);
    }

    @GetMapping("/volunteer-requests")
    public List<VolunteerRequestDTO> getAllVolunteerRequests() {
        return userService.getAllVolunteerRequests();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/loggedInUser")
    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userService.getLoggedInUser(authentication.getPrincipal().toString());
    }

    @PostMapping("/save-token")
    public ResponseEntity<String> saveToken(@RequestBody TokenRequest tokenRequest) {
        userService.saveUserToken(tokenRequest.getUserId(), tokenRequest.getToken());
        return ResponseEntity.ok("Token saved");
    }
}
