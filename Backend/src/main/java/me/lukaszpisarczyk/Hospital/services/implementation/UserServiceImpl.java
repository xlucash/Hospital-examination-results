package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.repositories.UserRepository;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PersonRepository personRepository;

    @Override
    public User findByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User findUserByPesel(String pesel) {
        Person person =  personRepository.findByPesel(pesel);
        return userRepository.findUserByPerson(person);
    }
}
