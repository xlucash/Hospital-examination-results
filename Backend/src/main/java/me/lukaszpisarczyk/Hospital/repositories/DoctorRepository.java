package me.lukaszpisarczyk.Hospital.repositories;

import me.lukaszpisarczyk.Hospital.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    boolean existsByLicenseNumber(String licenseNumber);
}
