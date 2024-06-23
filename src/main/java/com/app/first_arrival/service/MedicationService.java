package com.app.first_arrival.service;

import com.app.first_arrival.repository.MedicationRepository;
import com.app.first_arrival.entities.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {

    private final MedicationRepository medicationRepository;

    @Autowired
    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public List<Medication> findAll() {
        return medicationRepository.findAll();
    }

    public Optional<Medication> findById(Integer id) {
        return medicationRepository.findById(id);
    }

    public Medication save(Medication medication) {
        return medicationRepository.save(medication);
    }

    public void deleteById(Integer id) {
        medicationRepository.deleteById(id);
    }
}
