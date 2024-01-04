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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
