package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.CaptchaCode;

public interface CaptchaRepositori extends JpaRepository<CaptchaCode, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM captcha_codes WHERE time < (:currentTime);")
    void deleteOldCaptca(@Param("currentTime") long currentTime);
}
