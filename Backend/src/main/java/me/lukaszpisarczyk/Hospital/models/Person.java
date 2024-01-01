package me.lukaszpisarczyk.Hospital.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "persons", uniqueConstraints = {
        @UniqueConstraint(columnNames = "pesel"),
})
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Pattern(regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{2,50}$", message = "Nieprawidłowy format imienia")
    private String name;
    @Pattern(regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]{2,50}$", message = "Nieprawidłowy format nazwiska")
    private String surname;
    @DateTimeFormat(pattern = "MM/dd/yyyy")
    private LocalDate dateOfBirth;
    @Pattern(regexp = "^\\d{11}$", message = "Nieprawidłowy format peselu")
    private String pesel;
    @Pattern(
            regexp = "^\\d{3}-\\d{3}-\\d{3}$",
            message = "Nieprawidłowy format numeru telefonu")
    private String phoneNumber;

    public Person(String name, String surname, LocalDate dateOfBirth, String pesel, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.pesel = pesel;
        this.phoneNumber = phoneNumber;
    }
}
