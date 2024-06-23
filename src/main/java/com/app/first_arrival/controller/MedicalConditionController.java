package com.app.first_arrival.controller;

import com.app.first_arrival.service.MedicalConditionService;
import com.app.first_arrival.entities.MedicalCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-conditions")
public class MedicalConditionController {

    private final MedicalConditionService medicalConditionService;

    @Autowired
    public MedicalConditionController(MedicalConditionService medicalConditionService) {
        this.medicalConditionService = medicalConditionService;
    }

    @GetMapping
    public List<MedicalCondition> getAllMedicalConditions() {
        return medicalConditionService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalCondition> getMedicalConditionById(@PathVariable Integer id) {
        return medicalConditionService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicalCondition createMedicalCondition(@RequestBody MedicalCondition medicalCondition) {
        return medicalConditionService.save(medicalCondition);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalCondition(@PathVariable Integer id) {
        medicalConditionService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
