package me.lukaszpisarczyk.Hospital.controllers;

import me.lukaszpisarczyk.Hospital.enums.UserRole;
import me.lukaszpisarczyk.Hospital.models.Address;
import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.Role;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.*;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
class UserControllerTest {

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

    private Role userRole;
    private Address address;
    private Person person;
    private User user;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        roleRepository.deleteAll();
        personRepository.deleteAll();
        addressRepository.deleteAll();
        doctorRepository.deleteAll();

        userRole = roleRepository.save(new Role(UserRole.ROLE_USER));

        address = new Address("Test Street", "123", "1", "Test City", "00-000");
        addressRepository.save(address);

        person = new Person("John", "Doe", LocalDate.of(1990, 1, 1), "12345678901", "123-456-789");
        personRepository.save(person);

        user = new User("test@example.com", "password", person, address);
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
    }

    @Test
    public void getUserByEmail_ShouldReturnUser() throws Exception {
        String email = "test@example.com";

        mockMvc.perform(get("/api/user/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void getUserByPesel_ShouldReturnUser() throws Exception {
        String pesel = "12345678901";

        mockMvc.perform(get("/api/user/pesel/{pesel}", pesel)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.person.pesel").value(pesel));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        mockMvc.perform(get("/api/user/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    void getAllPatients_ShouldReturnAllUsers() throws Exception {
        mockMvc.perform(get("/api/user/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value(user.getEmail()));
    }
}