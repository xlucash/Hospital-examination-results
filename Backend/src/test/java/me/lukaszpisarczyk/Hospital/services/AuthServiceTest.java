package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.*;
import me.lukaszpisarczyk.Hospital.enums.UserRole;
import me.lukaszpisarczyk.Hospital.models.*;
import me.lukaszpisarczyk.Hospital.repositories.DoctorRepository;
import me.lukaszpisarczyk.Hospital.repositories.RoleRepository;
import me.lukaszpisarczyk.Hospital.repositories.UserRepository;
import me.lukaszpisarczyk.Hospital.security.jwt.JwtUtils;
import me.lukaszpisarczyk.Hospital.security.services.UserDetailsImpl;
import me.lukaszpisarczyk.Hospital.services.implementation.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private AddressService addressService;

    @Mock
    private PersonService personService;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    public void setUp() {
        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void testAuthenticateUser() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        UserDetailsImpl userDetails = new UserDetailsImpl(1L, "test@example.com", "password", Collections.emptyList());
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        Person person = new Person();
        person.setName("John");
        person.setSurname("Doe");
        person.setDateOfBirth(LocalDate.of(1990, 1, 1));
        person.setPesel("12345678901");
        user.setPerson(person);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtils.generateTokenFromUsername("test@example.com")).thenReturn("jwtToken");

        UserInfoResponse result = authService.authenticateUser(loginRequest);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(1)).findByEmail("test@example.com");
        verify(jwtUtils, times(1)).generateTokenFromUsername("test@example.com");
        assertEquals(userDetails.getId(), result.getId());
        assertEquals(userDetails.getUsername(), result.getUsername());
        assertEquals(person.getName(), result.getName());
        assertEquals(person.getSurname(), result.getSurname());
        assertEquals(person.getDateOfBirth(), result.getDateOfBirth());
        assertEquals(person.getPesel(), result.getPesel());
        assertEquals(userDetails.getEmail(), result.getEmail());
        assertEquals(Collections.emptyList(), result.getRoles());
    }

    @Test
    public void testRegisterUser() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);

        Address address = new Address();
        when(addressService.saveAddress(signupRequest)).thenReturn(address);

        Person person = new Person();
        when(personService.savePerson(signupRequest)).thenReturn(person);

        Role role = new Role(1, UserRole.ROLE_USER);
        when(roleRepository.findByName(UserRole.ROLE_USER)).thenReturn(Optional.of(role));


        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        MessageResponse result = authService.registerUser(signupRequest);

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(addressService, times(1)).saveAddress(signupRequest);
        verify(personService, times(1)).savePerson(signupRequest);
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("User registered successfully!", result.getMessage());
    }

    @Test
    public void testRegisterDoctor() {
        SignupDoctorRequest signupRequest = new SignupDoctorRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");
        signupRequest.setLicenseNumber("123456");
        signupRequest.setSpecialization("Cardiology");

        when(doctorRepository.existsByLicenseNumber("123456")).thenReturn(false);

        Address address = new Address();
        when(addressService.saveAddress(signupRequest)).thenReturn(address);

        Person person = new Person();
        when(personService.savePerson(signupRequest)).thenReturn(person);

        Doctor doctor = new Doctor();
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        Role role = new Role(1, UserRole.ROLE_DOCTOR);
        when(roleRepository.findByName(UserRole.ROLE_DOCTOR)).thenReturn(Optional.of(role));

        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        MessageResponse result = authService.registerDoctor(signupRequest);

        verify(doctorRepository, times(1)).existsByLicenseNumber("123456");
        verify(addressService, times(1)).saveAddress(signupRequest);
        verify(personService, times(1)).savePerson(signupRequest);
        verify(doctorRepository, times(1)).save(any(Doctor.class));
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals("Doctor registered successfully!", result.getMessage());
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        MessageResponse result = authService.registerUser(signupRequest);

        verify(userRepository, times(1)).existsByEmail("test@example.com");
        assertEquals("Error: Email is already in use!", result.getMessage());
    }

    @Test
    public void testRegisterDoctor_LicenseNumberAlreadyExists() {
        SignupDoctorRequest signupRequest = new SignupDoctorRequest();
        signupRequest.setEmail("doctor@example.com");
        signupRequest.setPassword("password");
        signupRequest.setLicenseNumber("123456");
        signupRequest.setSpecialization("Cardiology");

        when(doctorRepository.existsByLicenseNumber("123456")).thenReturn(true);

        MessageResponse result = authService.registerDoctor(signupRequest);

        verify(doctorRepository, times(1)).existsByLicenseNumber("123456");
        assertEquals("Error: License number is already in use!", result.getMessage());
    }

    @Test
    public void testRegisterUser_RoleNotFound() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("newuser@example.com");
        signupRequest.setPassword("password");

        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(roleRepository.findByName(UserRole.ROLE_USER)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> authService.registerUser(signupRequest));
        assertEquals("Error: Role is not found.", exception.getMessage());
    }
}