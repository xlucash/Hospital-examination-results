package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.dto.LoginRequest;
import me.lukaszpisarczyk.Hospital.dto.MessageResponse;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.dto.UserInfoResponse;
import me.lukaszpisarczyk.Hospital.enums.UserRole;
import me.lukaszpisarczyk.Hospital.models.Address;
import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.Role;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.AddressRepository;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.repositories.RoleRepository;
import me.lukaszpisarczyk.Hospital.repositories.UserRepository;
import me.lukaszpisarczyk.Hospital.security.jwt.JwtUtils;
import me.lukaszpisarczyk.Hospital.security.services.UserDetailsImpl;
import me.lukaszpisarczyk.Hospital.services.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    private final AddressRepository addressRepository;

    private final PersonRepository personRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository, RoleRepository roleRepository, AddressRepository addressRepository, PersonRepository personRepository, PasswordEncoder encoder, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.addressRepository = addressRepository;
        this.personRepository = personRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public UserInfoResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).get();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
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

        Address address = new Address(signUpRequest.getStreetAddress(),
                signUpRequest.getHouse(),
                signUpRequest.getApartment(),
                signUpRequest.getCity(),
                signUpRequest.getPostalCode());
        address = addressRepository.save(address);

        Person person = new Person(signUpRequest.getName(),
                signUpRequest.getSurname(),
                signUpRequest.getDateOfBirth(),
                signUpRequest.getPesel(),
                signUpRequest.getPhoneNumber());
        person = personRepository.save(person);

        User user = new User(
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                person,
                address);

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(UserRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        userRepository.save(user);

        return new MessageResponse("User registered successfully!");
    }
}
