package me.lukaszpisarczyk.Hospital.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;

@Getter
@Setter
public class ExaminationRequestDto {
    @Enumerated(EnumType.STRING)
    private ExaminationType type;
    private String title;
    private String description;
    private String pesel;
}
