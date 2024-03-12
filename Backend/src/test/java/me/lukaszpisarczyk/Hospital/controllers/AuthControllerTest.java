package me.lukaszpisarczyk.Hospital.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import me.lukaszpisarczyk.Hospital.dto.LoginRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.enums.UserRole;
import me.lukaszpisarczyk.Hospital.models.*;
import me.lukaszpisarczyk.Hospital.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PasswordEncoder encoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Address address;
    private Person person;
    private Address doctorAddress;
    private Person doctorPerson;
    private Doctor doctor;
    private User user;
    private User doctorUser;
    private Role userRole;
    private Role doctorRole;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        personRepository.deleteAll();
        addressRepository.deleteAll();
        doctorRepository.deleteAll();

        userRole = new Role();
        userRole.setName(UserRole.ROLE_USER);
        userRole = roleRepository.save(userRole);

        doctorRole = new Role();
        doctorRole.setName(UserRole.ROLE_DOCTOR);
        doctorRole = roleRepository.save(doctorRole);

        address = new Address("Test Street", "123", "1", "Test City", "00-000");
        addressRepository.save(address);

        person = new Person("John", "Doe", LocalDate.of(1990, 1, 1), "12345678901", "123-456-789");
        personRepository.save(person);

        user = new User("test@example.com", encoder.encode("testuser123"), person, address);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);

        doctorAddress = new Address("Doctor Street", "321", "2", "Doctor City", "11-111");
        addressRepository.save(doctorAddress);

        doctorPerson = new Person("Alice", "Smith", LocalDate.of(1985, 5, 15), "98765432101", "987-654-321");
        personRepository.save(doctorPerson);

        doctor = new Doctor();
        doctor.setLicenseNumber("D12345");
        doctor.setSpecialization("Cardiology");
        doctorRepository.save(doctor);

        doctorUser = new User("doctor@example.com", "testdoctor123", doctorPerson, doctorAddress, doctor);
        doctorUser.setRoles(Set.of(doctorRole));
        userRepository.save(doctorUser);

        JavaTimeModule module = new JavaTimeModule();

        module.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));

        objectMapper.registerModule(module);

    }

    @Test
    void authenticateUser_ShouldReturnUserInfo() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("testuser123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void registerUser_ShouldReturnSuccessMessage() throws Exception {
        SignupRequest signupRequest = new SignupRequest(
                "user@example.com",
                "password123",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "90345678901",
                "123-456-789",
                "Main Street",
                "123",
                "2",
                "City",
                "00-000"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("User registered successfully")));
    }

    @Test
    @WithMockUser(username = "doctor@example.com", password = "testdoctor123", roles = "DOCTOR")
    void registerDoctor_ShouldReturnSuccessMessage() throws Exception {
        SignupDoctorRequest signupDoctorRequest = new SignupDoctorRequest(
                "doctor2@example.com",
                "password123",
                "Alice",
                "Smith",
                LocalDate.of(1985, 5, 15),
                "85765432101",
                "321-654-987",
                "Second Street",
                "321",
                "1",
                "Town",
                "11-111",
                "123456",
                "Cardiology"
        );

        mockMvc.perform(post("/api/auth/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDoctorRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString("Doctor registered successfully")));
    }

    @Test
    public void registerUser_WhenEmailExists_ShouldReturnBadRequest() throws Exception {
        SignupRequest signupRequest = new SignupRequest(
                "test@example.com",
                "password123",
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "12345678901",
                "123-456-789",
                "Test Street",
                "123",
                "1",
                "Test City",
                "00-000");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Email is already in use!"));
    }

    @Test
    @WithMockUser(username = "doctor@example.com", password = "testdoctor123", roles = "DOCTOR")
    public void registerDoctor_WhenLicenseNumberExists_ShouldReturnBadRequest() throws Exception {
        SignupDoctorRequest signupDoctorRequest = new SignupDoctorRequest(
                "doctor@example.com",
                "password123",
                "John",
                "Doe",
                LocalDate.of(1985, 5, 15),
                "10345678901",
                "123-456-789",
                "Doctor Street",
                "321",
                "2",
                "Medic City",
                "11-111",
                "D12345",
                "Cardiology");

        mockMvc.perform(post("/api/auth/register/doctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupDoctorRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: License number is already in use!"));
    }
}