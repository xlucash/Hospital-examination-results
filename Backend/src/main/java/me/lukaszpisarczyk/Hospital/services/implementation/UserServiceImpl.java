package me.lukaszpisarczyk.Hospital.services.implementation;

import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.User;
import me.lukaszpisarczyk.Hospital.repositories.PersonRepository;
import me.lukaszpisarczyk.Hospital.repositories.UserRepository;
import me.lukaszpisarczyk.Hospital.services.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    public UserServiceImpl(UserRepository userRepository, PersonRepository personRepository) {
        this.userRepository = userRepository;
        this.personRepository = personRepository;
    }

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

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }
}
