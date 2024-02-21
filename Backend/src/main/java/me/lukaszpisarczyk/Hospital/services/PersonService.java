package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.models.Person;

public interface PersonService {
    Person savePerson(SignupRequest signupRequest);

    Person savePerson(SignupDoctorRequest signupRequest);
}
