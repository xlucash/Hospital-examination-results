package me.lukaszpisarczyk.Hospital.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Doctor {
    @Id
    @GeneratedValue
    private Long id;
    private String licenseNumber;
    private String specialization;

    public Doctor(String licenseNumber, String specialization) {
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
    }
}
