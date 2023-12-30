package me.lukaszpisarczyk.Hospital.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public Address(String streetAddress, String house, String apartment, String city, String postalCode) {
        this.streetAddress = streetAddress;
        this.house = house;
        this.apartment = apartment;
        this.city = city;
        this.postalCode = postalCode;
    }
}
