package com.app.first_arrival.entities;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Medication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

}