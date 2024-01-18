package me.lukaszpisarczyk.Hospital.repositories;

import jakarta.validation.constraints.NotNull;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;
import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import me.lukaszpisarczyk.Hospital.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, Long> {
    List<ExaminationResult> findAllByPatientAndType(User patient, @NotNull ExaminationType type);
    List<ExaminationResult> findAllByDoctorAndType(User doctor, @NotNull ExaminationType type);
    List<ExaminationResult> findAllByPatient(User patient);
}
