package me.lukaszpisarczyk.Hospital.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor {
    @Id
    @GeneratedValue
    private Long id;
    private String licenseNumber;
    private String specialization;

    public Doctor() {
    }

    public Doctor(Long id, String licenseNumber, String specialization) {
        this.id = id;
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
    }

    public Doctor(String licenseNumber, String specialization) {
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
