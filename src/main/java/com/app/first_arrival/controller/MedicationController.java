package com.app.first_arrival.controller;

import com.app.first_arrival.service.MedicationService;
import com.app.first_arrival.entities.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {

    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public List<Medication> getAllMedications() {
        return medicationService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable Integer id) {
        return medicationService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medication createMedication(@RequestBody Medication medication) {
        return medicationService.save(medication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Integer id) {
        medicationService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
