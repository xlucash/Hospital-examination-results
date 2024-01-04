package me.lukaszpisarczyk.Hospital.dto;

import lombok.Getter;
import lombok.Setter;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ExaminationResultDto {
    private Long id;
    private ExaminationType type;
    private String title;
    private String description;
    private LocalDate examinationDate;
    private Long patientId;
    private Long doctorId;
    private List<Long> imageIds;
}
