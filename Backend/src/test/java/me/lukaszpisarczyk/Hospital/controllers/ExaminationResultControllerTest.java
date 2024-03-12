package me.lukaszpisarczyk.Hospital.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.lukaszpisarczyk.Hospital.dto.ExaminationRequestDto;
import me.lukaszpisarczyk.Hospital.enums.ExaminationType;
import me.lukaszpisarczyk.Hospital.enums.UserRole;
import me.lukaszpisarczyk.Hospital.models.*;
import me.lukaszpisarczyk.Hospital.repositories.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class ExaminationResultControllerTest {

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
    private ExaminationResultRepository examinationResultRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Address patientAddress;
    private Person patientPerson;
    private Address doctorAddress;
    private Person doctorPerson;
    private Doctor doctor;
    private User patientUser;
    private User doctorUser;
    private Role userRole;
    private Role doctorRole;
    private ExaminationResult examinationResult;

    @BeforeEach
    public void setup() {
        examinationResultRepository.deleteAll();
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

        patientAddress = new Address("Test Street", "123", "1", "Test City", "00-000");
        addressRepository.save(patientAddress);

        patientPerson = new Person("John", "Doe", LocalDate.of(1990, 1, 1), "12345678901", "123-456-789");
        personRepository.save(patientPerson);

        patientUser = new User("test@example.com", "testuser123", patientPerson, patientAddress);
        patientUser.setRoles(Set.of(userRole));
        userRepository.save(patientUser);

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

        examinationResult = new ExaminationResult(ExaminationType.XRAY, "Test Title", "Test Description", LocalDate.now(), patientUser, doctorUser, new ArrayList<>());
        examinationResultRepository.save(examinationResult);
    }

    @Test
    @WithMockUser(username = "doctor@example.com", password = "testdoctor123", roles = "DOCTOR")
    public void saveExaminationResult_ShouldReturnCreated() throws Exception {
        ExaminationRequestDto examinationRequestDto = new ExaminationRequestDto();
        examinationRequestDto.setType(ExaminationType.XRAY);
        examinationRequestDto.setTitle("Test Title");
        examinationRequestDto.setDescription("Test Description");
        examinationRequestDto.setPesel(patientUser.getPerson().getPesel());

        MockMultipartFile examinationResultDtoFile = new MockMultipartFile("examinationResult", "", "application/json", objectMapper.writeValueAsString(examinationRequestDto).getBytes(StandardCharsets.UTF_8));

        MockMultipartFile imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "test image content".getBytes());

        mockMvc.perform(multipart("/api/examination-result")
                        .file(examinationResultDtoFile)
                        .file(imageFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    public void getExaminationResult_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/examination-result/{id}", examinationResult.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(examinationResult.getId()))
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    public void getExaminationResultPdf_ShouldReturnPdf() throws Exception {
        mockMvc.perform(get("/api/examination-result/{id}/pdf", examinationResult.getId()))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "PATIENT")
    public void getExaminationResultByPatient_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/examination-result/patient/{type}", "XRAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("XRAY"));
    }

    @Test
    @WithMockUser(username = "doctor@example.com", roles = "DOCTOR")
    public void getExaminationResultByDoctor_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/examination-result/doctor/{type}", "XRAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("XRAY"));
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "PATIENT")
    public void getAllExaminationResult_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/examination-result/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}