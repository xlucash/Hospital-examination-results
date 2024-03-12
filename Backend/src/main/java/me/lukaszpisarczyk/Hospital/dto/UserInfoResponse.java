package me.lukaszpisarczyk.Hospital.dto;

import java.time.LocalDate;
import java.util.List;


public class UserInfoResponse {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private LocalDate dateOfBirth;
    private String pesel;
    private String email;
    private List<String> roles;
    private String token;

    public UserInfoResponse() {
    }

    public UserInfoResponse(Long id, String username, String name, String surname, LocalDate dateOfBirth, String pesel, String email, List<String> roles, String token) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.pesel = pesel;
        this.email = email;
        this.roles = roles;
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
