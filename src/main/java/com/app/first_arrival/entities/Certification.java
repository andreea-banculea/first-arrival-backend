package com.app.first_arrival.entities;

import com.app.first_arrival.entities.enums.CertificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
public class Certification implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificationCode;

    private CertificationStatus status;

    public Certification(String certificationCode, CertificationStatus status) {
        this.certificationCode = certificationCode;
        this.status = status;
    }
}
