package com.app.first_arrival.controller;

import com.app.first_arrival.entities.Emergency;
import com.app.first_arrival.entities.dto.EmergencyDTO;
import com.app.first_arrival.service.EmergencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/emergencies")
public class EmergencyController {
    private final EmergencyService emergencyService;

    private static final Logger logger = Logger.getLogger(EmergencyController.class.getName());

    @Autowired
    public EmergencyController(EmergencyService emergencyService) {
        this.emergencyService = emergencyService;
    }

    @GetMapping
    public List<Emergency> getAllEmergencies() {
        return emergencyService.findAll();
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<Void> cancelEmergency(@PathVariable Long id) {
        emergencyService.cancelEmergencyById(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmergency(@PathVariable Long id) {
        emergencyService.deleteEmergencyById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/resolve/{id}")
    public ResponseEntity<Void> resolveEmergency(@PathVariable Long id) {
        emergencyService.resolveEmergencyById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Emergency> getEmergencyById(@PathVariable Long id) {
        return emergencyService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public List<Emergency> getEmergenciesWhereStatusIsActive() {
        return emergencyService.findAllEmergenciesWithStatusActive();
    }

    @PostMapping
    public Emergency createEmergency(@RequestBody Emergency emergency) {
        logger.info("Received Emergency: " + emergency);
        return emergencyService.save(emergency);
    }

    @PutMapping()
    public ResponseEntity<EmergencyDTO> updateEmergency(@RequestBody EmergencyDTO emergencyDTO) {
        try {
            EmergencyDTO updatedEmergency = emergencyService.update(emergencyDTO);
            return ResponseEntity.ok(updatedEmergency);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/accept/{emergencyId}")
    public ResponseEntity<Emergency> acceptEmergency(@PathVariable Long emergencyId) {
        try {
            Emergency updatedEmergency = emergencyService.acceptEmergency(emergencyId);
            return ResponseEntity.ok(updatedEmergency);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/abort/{emergencyId}")
    public ResponseEntity<Emergency> abort(@PathVariable Long emergencyId) {
        try {
            Emergency updatedEmergency = emergencyService.abortEmergency(emergencyId);
            return ResponseEntity.ok(updatedEmergency);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/hasActiveEmergency")
    public Emergency hasActiveEmergency() {
        return emergencyService.hasAnActiveEmergencyReported();
    }

    @PostMapping("/notify")
    public ResponseEntity<String> notifyNearbyUsers(@RequestBody Emergency emergency) {
        emergencyService.notifyNearbyUsers(emergency);
        return ResponseEntity.ok("Notifications sent");
    }
}
