package me.lukaszpisarczyk.Hospital.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "examination_results")
public class ExaminationResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ExaminationType type;
    @NotNull
    private String title;
    @NotNull
    @Size(min = 10, max = 500)
    private String description;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate examinationDate = LocalDate.now();
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private User patient;
    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private User doctor;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "examination_result_id")
    private List<Image> images;

    public ExaminationResult() {
    }

    public ExaminationResult(Long id, ExaminationType type, String title, String description, LocalDate examinationDate, User patient, User doctor, List<Image> images) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.examinationDate = examinationDate;
        this.patient = patient;
        this.doctor = doctor;
        this.images = images;
    }

    public ExaminationResult(ExaminationType type, String title, String description, LocalDate examinationDate, User patient, User doctor, List<Image> images) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.examinationDate = examinationDate;
        this.patient = patient;
        this.doctor = doctor;
        this.images = images;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDate getExaminationDate() {
        return examinationDate;
    }

    public void setExaminationDate(LocalDate examinationDate) {
        this.examinationDate = examinationDate;
    }

    public User getPatient() {
        return patient;
    }

    public void setPatient(User patient) {
        this.patient = patient;
    }

    public User getDoctor() {
        return doctor;
    }

    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
