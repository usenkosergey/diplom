package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.diplom.entities.CaptchaCode;

public interface CaptchaRepositori extends JpaRepository<CaptchaCode, Integer> {
}
