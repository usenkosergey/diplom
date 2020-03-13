package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.User;

import java.util.Optional;

@Repository
public interface UserRepositori extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
}
