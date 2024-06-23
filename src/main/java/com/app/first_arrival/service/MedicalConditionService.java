package com.app.first_arrival.service;

import com.app.first_arrival.repository.MedicalConditionRepository;
import com.app.first_arrival.entities.MedicalCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalConditionService {

    private final MedicalConditionRepository medicalConditionRepository;

    @Autowired
    public MedicalConditionService(MedicalConditionRepository medicalConditionRepository) {
        this.medicalConditionRepository = medicalConditionRepository;
    }

    public List<MedicalCondition> findAll() {
        return medicalConditionRepository.findAll();
    }

    public Optional<MedicalCondition> findById(Integer id) {
        return medicalConditionRepository.findById(id);
    }

    public MedicalCondition save(MedicalCondition medicalCondition) {
        return medicalConditionRepository.save(medicalCondition);
    }

    public void deleteById(Integer id) {
        medicalConditionRepository.deleteById(id);
    }
}
