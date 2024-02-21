package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.dto.SignupDoctorRequest;
import me.lukaszpisarczyk.Hospital.dto.SignupRequest;
import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.services.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
    @Override
    public Person savePerson(SignupRequest signupRequest) {
        Person person = new Person(signupRequest.getName(),
                signupRequest.getSurname(),
                signupRequest.getDateOfBirth(),
                signupRequest.getPesel(),
                signupRequest.getPhoneNumber());
        return personRepository.save(person);
    }

    @Override
    public Person savePerson(SignupDoctorRequest signupRequest) {
        Person person = new Person(signupRequest.getName(),
                signupRequest.getSurname(),
                signupRequest.getDateOfBirth(),
                signupRequest.getPesel(),
                signupRequest.getPhoneNumber());
        return personRepository.save(person);
    }
}
