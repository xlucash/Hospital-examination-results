package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.repositories.UserRepository;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public User retrieveUserFromToken() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByEmail(userId);
        if(user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }
}
