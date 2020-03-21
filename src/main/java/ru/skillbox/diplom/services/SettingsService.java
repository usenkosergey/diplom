package ru.skillbox.diplom.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.skillbox.diplom.api.requests.SettingsRequest;
import ru.skillbox.diplom.repositories.SettingsRepositori;


@Service
public class SettingsService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SettingsRepositori settingsRepositori;

    public void changeSettings(SettingsRequest settingsRequest) {
        logger.info("//changeSettings");
        int answer = 0;

        answer = settingsRepositori.updateSettings("STATISTICS_IS_PUBLIC",
                settingsRequest.isSTATISTICS_IS_PUBLIC()) +
                settingsRepositori.updateSettings("POST_PREMODERATION",
                        settingsRequest.isPOST_PREMODERATION()) +
                settingsRepositori.updateSettings("MULTIUSER_MODE",
                        settingsRequest.isMULTIUSER_MODE());

        if (answer == 3) {
            logger.info("//changeSettings" + " настройки изменены");
        } else {
            logger.error("//changeSettings" + " настройки не записались в базу");
        }

    }
}
