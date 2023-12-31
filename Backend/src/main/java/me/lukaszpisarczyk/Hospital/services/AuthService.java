package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.LoginRequest;
import me.lukaszpisarczyk.Hospital.dto.MessageResponse;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.dto.UserInfoResponse;
import org.springframework.http.ResponseCookie;

public interface AuthService {
    UserInfoResponse authenticateUser(LoginRequest loginRequest);
    MessageResponse registerUser(SignupRequest signUpRequest);
}
