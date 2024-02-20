package me.lukaszpisarczyk.Hospital.controllers;

import jakarta.validation.Valid;

import me.lukaszpisarczyk.Hospital.dto.*;
import me.lukaszpisarczyk.Hospital.security.jwt.JwtUtils;
import me.lukaszpisarczyk.Hospital.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final JwtUtils jwtUtils;

    private final AuthService authService;

    public AuthController(JwtUtils jwtUtils, AuthService authService) {
        this.jwtUtils = jwtUtils;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        UserInfoResponse userInfoResponse = authService.authenticateUser(loginRequest);

        return ResponseEntity.ok().body(userInfoResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        MessageResponse messageResponse = authService.registerUser(signUpRequest);
        if (messageResponse.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }

    @PostMapping("/register/doctor")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody SignupDoctorRequest signUpRequest) {
        MessageResponse messageResponse = authService.registerDoctor(signUpRequest);
        if (messageResponse.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(messageResponse);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResponse);
    }
}
