package ru.skillbox.diplom.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skillbox.diplom.entities.Settings;

public interface SettingsRepositori extends JpaRepository<Settings, Integer> {
}
