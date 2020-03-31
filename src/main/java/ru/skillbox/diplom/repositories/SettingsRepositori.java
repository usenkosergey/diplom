package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.entities.Settings;

public interface SettingsRepositori extends JpaRepository<Settings, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Settings AS s " +
            "SET s.value = :value " +
            "WHERE s.code = :code")
    Integer updateSettings(String code,
                           Boolean value);
}
