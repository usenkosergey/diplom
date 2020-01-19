package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.entities.User;

@Repository
public interface UserRepositori extends JpaRepository<User, Integer> {

}
