package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User findByEmail (String email);

    User findUserByPesel (String pesel);

    User retrieveUserFromToken();

    User findById(Long id);

    List<User> getAllPatients();
}
