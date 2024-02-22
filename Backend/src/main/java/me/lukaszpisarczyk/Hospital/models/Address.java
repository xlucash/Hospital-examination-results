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

    public Address() {
    }

    public Address(Long id, String streetAddress, String house, String apartment, String city, String postalCode) {
        this.id = id;
        this.streetAddress = streetAddress;
        this.house = house;
        this.apartment = apartment;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Address(String streetAddress, String house, String apartment, String city, String postalCode) {
        this.streetAddress = streetAddress;
        this.house = house;
        this.apartment = apartment;
        this.city = city;
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
