package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.CaptchaCode;

import java.util.Optional;

public interface CaptchaRepositori extends JpaRepository<CaptchaCode, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "DELETE FROM captcha_codes WHERE time < (:currentTime);")
    void deleteOldCaptca(@Param("currentTime") long currentTime);

    @Transactional
    @Query(nativeQuery = true,
            value = "insert into captcha_codes (time, code, secret_code) " +
                    "values " +
                    "((:currentTime), (:code), (:secret_code)) " +
                    "on CONFLICT (code) DO NOTHING RETURNING id;")
    Optional<Integer> saveNewCaptcha(
            @Param("currentTime") long currentTime,
            @Param("code") String code,
            @Param("secret_code") String secret_code);

    Optional<CaptchaCode> findByCode(String code);

}
