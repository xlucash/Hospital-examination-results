package me.lukaszpisarczyk.Hospital.repositories;

import me.lukaszpisarczyk.Hospital.models.ExaminationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationResultRepository extends JpaRepository<ExaminationResult, Long> {
}
