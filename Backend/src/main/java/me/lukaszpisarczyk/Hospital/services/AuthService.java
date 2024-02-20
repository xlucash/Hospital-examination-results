package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.*;

public interface AuthService {
    UserInfoResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
    MessageResponse registerDoctor(SignupDoctorRequest signUpRequest);
}
