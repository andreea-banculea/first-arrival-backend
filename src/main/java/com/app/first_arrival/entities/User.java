package com.app.first_arrival.entities;

import com.app.first_arrival.entities.Certification;
import com.app.first_arrival.entities.Location;
import com.app.first_arrival.entities.enums.Role;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Email
    private String email;

    private String phoneNumber;

    private String password;

    @OneToOne
    private Location location;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @Nullable
    private Certification certification;

    @Nullable
    private Integer volunteerLevel;

    String pushToken;
}
