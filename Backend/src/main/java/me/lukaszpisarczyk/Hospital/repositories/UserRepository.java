package me.lukaszpisarczyk.Hospital.repositories;

import me.lukaszpisarczyk.Hospital.models.Person;
import me.lukaszpisarczyk.Hospital.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findUserByEmail (String email);
    Boolean existsByEmail(String email);
    User findUserByPerson (Person person);
    List<User> findAll();
}
