package me.lukaszpisarczyk.Hospital.services;

import me.lukaszpisarczyk.Hospital.models.User;

import java.util.Optional;

public interface UserService {

    User findByEmail (String email);

    User findUserByPesel (String pesel);
}
