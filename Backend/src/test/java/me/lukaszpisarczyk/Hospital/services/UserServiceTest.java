package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.repositories.UserRepository;
import me.lukaszpisarczyk.Hospital.services.implementation.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        User expectedUser = new User();
        when(userRepository.findUserByEmail(email)).thenReturn(expectedUser);

        User result = userService.findByEmail(email);

        assertThat(result).isEqualTo(expectedUser);
        verify(userRepository).findUserByEmail(email);
    }

    @Test
    void testFindUserByPesel() {
        String pesel = "12345678901";
        Person person = new Person();
        User expectedUser = new User();
        when(personRepository.findByPesel(pesel)).thenReturn(person);
        when(userRepository.findUserByPerson(person)).thenReturn(expectedUser);

        User result = userService.findUserByPesel(pesel);

        assertThat(result).isEqualTo(expectedUser);
        verify(personRepository).findByPesel(pesel);
        verify(userRepository).findUserByPerson(person);
    }

    @Test
    void testRetrieveUserFromToken() {
        String email = "user@example.com";
        User expectedUser = new User(); // Set up your user details
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        User result = userService.retrieveUserFromToken();

        assertThat(result).isEqualTo(expectedUser);
    }

    @Test
    void testFindById_userNotFound() {
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void testGetAllPatients() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        var result = userService.getAllPatients();

        assertThat(result).hasSize(1).contains(user);
    }

    @Test
    void testRetrieveUserFromToken_UserNotFound() {
        String email = "nonexistent@example.com";
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.retrieveUserFromToken())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void testFindById_UserNotFound() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findById(userId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(userId);
    }
}