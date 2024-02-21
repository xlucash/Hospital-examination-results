package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.services.implementation.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSavePerson() {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setName("John");
        signupRequest.setSurname("Doe");
        signupRequest.setDateOfBirth(LocalDate.of(1990, 1, 1));
        signupRequest.setPesel("12345678901");
        signupRequest.setPhoneNumber("123-456-789");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setName("John");
        savedPerson.setSurname("Doe");
        savedPerson.setDateOfBirth(LocalDate.of(1990, 1, 1));
        savedPerson.setPesel("12345678901");
        savedPerson.setPhoneNumber("123-456-789");

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        Person result = personService.savePerson(signupRequest);

        verify(personRepository, times(1)).save(any(Person.class));
        assertEquals(savedPerson, result);
    }

    @Test
    public void testSaveDoctor() {
        SignupDoctorRequest signupRequest = new SignupDoctorRequest();
        signupRequest.setName("Jane");
        signupRequest.setSurname("Smith");
        signupRequest.setDateOfBirth(LocalDate.of(1985, 5, 10));
        signupRequest.setPesel("98765432109");
        signupRequest.setPhoneNumber("987-654-321");
        signupRequest.setLicenseNumber("123456");
        signupRequest.setSpecialization("Cardiology");

        Person savedPerson = new Person();
        savedPerson.setId(2L);
        savedPerson.setName("Jane");
        savedPerson.setSurname("Smith");
        savedPerson.setDateOfBirth(LocalDate.of(1985, 5, 10));
        savedPerson.setPesel("98765432109");
        savedPerson.setPhoneNumber("987-654-321");

        when(personRepository.save(any(Person.class))).thenReturn(savedPerson);

        Person result = personService.savePerson(signupRequest);

        verify(personRepository, times(1)).save(any(Person.class));
        assertEquals(savedPerson, result);
    }
}