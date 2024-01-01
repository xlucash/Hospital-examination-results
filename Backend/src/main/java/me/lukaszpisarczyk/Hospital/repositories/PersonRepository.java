package me.lukaszpisarczyk.Hospital.repositories;

import me.lukaszpisarczyk.Hospital.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {
    Person findByPesel (String pesel);
}
