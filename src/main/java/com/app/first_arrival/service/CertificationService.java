package com.app.first_arrival.service;

import com.app.first_arrival.entities.Certification;
import com.app.first_arrival.repository.CertificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CertificationService {

    private final CertificationRepository certificationRepository;

    @Autowired
    public CertificationService(CertificationRepository certificationRepository) {
        this.certificationRepository = certificationRepository;
    }

    public List<Certification> findAll() {
        return certificationRepository.findAll();
    }

    public Optional<Certification> findById(Long id) {
        return certificationRepository.findById(id);
    }

    public Certification save(Certification certification) {
        return certificationRepository.save(certification);
    }

    public void deleteById(Long id) {
        certificationRepository.deleteById(id);
    }
}
