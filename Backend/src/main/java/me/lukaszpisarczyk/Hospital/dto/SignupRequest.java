package me.lukaszpisarczyk.Hospital.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
