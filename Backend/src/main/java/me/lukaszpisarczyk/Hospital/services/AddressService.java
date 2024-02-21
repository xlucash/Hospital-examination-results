package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.models.Address;

public interface AddressService {
    Address saveAddress(SignupRequest signupRequest);

    Address saveAddress(SignupDoctorRequest signupRequest);
}
