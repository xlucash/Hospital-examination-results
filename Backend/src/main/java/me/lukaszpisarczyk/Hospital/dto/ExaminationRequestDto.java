package me.lukaszpisarczyk.Hospital.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;

public class ExaminationRequestDto {
    @Enumerated(EnumType.STRING)
    private ExaminationType type;
    private String title;
    private String description;
    private String pesel;

    public ExaminationType getType() {
        return type;
    }

    public void setType(ExaminationType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
}
