package me.lukaszpisarczyk.Hospital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
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
    @NotBlank
    @Pattern(regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\d\\s,\\-]{2,50}$", message = "Nieprawidłowy format ulicy")
    private String streetAddress;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\d\\s]{1,20}$", message = "Nieprawidłowy format numeru domu")
    private String house;
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z\\d\\s]{1,20}$", message = "Nieprawidłowy format numeru mieszkania")
    private String apartment;
    @NotBlank
    @Pattern(regexp = "^[a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ\\d\\s,\\-]{1,50}$", message = "Nieprawidłowy format miasta")
    private String city;
    @NotBlank
    @Pattern(regexp = "^\\d{2}-\\d{3}$", message = "Niepraawidłowy format kodu pocztowego")
    private String postalCode;
}
