package me.lukaszpisarczyk.Hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
