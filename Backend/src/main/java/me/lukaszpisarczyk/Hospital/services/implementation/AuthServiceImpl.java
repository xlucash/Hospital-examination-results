package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.dto.*;
import me.lukaszpisarczyk.Hospital.enums.UserRole;
import me.lukaszpisarczyk.Hospital.models.*;
import me.lukaszpisarczyk.Hospital.repositories.*;
import me.lukaszpisarczyk.Hospital.security.jwt.JwtUtils;
import me.lukaszpisarczyk.Hospital.security.services.UserDetailsImpl;
import me.lukaszpisarczyk.Hospital.services.AddressService;
import me.lukaszpisarczyk.Hospital.services.AuthService;
import me.lukaszpisarczyk.Hospital.services.PersonService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final DoctorRepository doctorRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final AddressService addressService;

    private final PersonService personService;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           RoleRepository roleRepository, DoctorRepository doctorRepository,
                           PasswordEncoder encoder, JwtUtils jwtUtils,
                           AddressService addressService, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.doctorRepository = doctorRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.addressService = addressService;
        this.personService = personService;
    }

    @Override
    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        return new UserInfoResponse(userDetails.getId(),
                userDetails.getUsername(),
                user.getPerson().getName(),
                user.getPerson().getSurname(),
                user.getPerson().getDateOfBirth(),
                user.getPerson().getPesel(),
                userDetails.getEmail(),
                roles,
                jwtToken);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new MessageResponse("Error: Email is already in use!");
        }

        Address address = addressService.saveAddress(signUpRequest);
        Person person = personService.savePerson(signUpRequest);

        User user = new User(
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                person,
                address);

        assignRolesAndSaveUser(user, UserRole.ROLE_USER);

        return new MessageResponse("User registered successfully!");
    }

    @Override
    public MessageResponse registerDoctor(SignupDoctorRequest signUpRequest) {
        if (doctorRepository.existsByLicenseNumber(signUpRequest.getLicenseNumber())) {
            return new MessageResponse("Error: License number is already in use!");
        }

        Address address = addressService.saveAddress(signUpRequest);
        Person person = personService.savePerson(signUpRequest);

        Doctor doctor = new Doctor(signUpRequest.getLicenseNumber(),
                signUpRequest.getSpecialization());
        doctor = doctorRepository.save(doctor);

        User user = new User(
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                person,
                address,
                doctor);

        assignRolesAndSaveUser(user, UserRole.ROLE_DOCTOR);

        return new MessageResponse("Doctor registered successfully!");
    }

    private void assignRolesAndSaveUser(User user, UserRole userRoleEnum) {
        Role userRole = roleRepository.findByName(userRoleEnum)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);
    }
}
