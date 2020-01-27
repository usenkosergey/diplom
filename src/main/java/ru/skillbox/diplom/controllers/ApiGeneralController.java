package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.api.responses.InitData;
import ru.skillbox.diplom.entities.Settings;
import ru.skillbox.diplom.repositories.SettingsRepositori;
import ru.skillbox.diplom.services.EMailService;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/")
public class ApiGeneralController {

    @Autowired
    private SettingsRepositori settingsRepositori;

        @Autowired
        EMailService eMailService;


    @GetMapping("/api/init")
    public InitData getInit() {
        System.out.println("Это инит - я тут"); //TODO удалить позже

        try {
            eMailService.sendEmail();
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return InitData.getInitData();
    }

    @GetMapping("api/settings")
    public HashMap<String, Boolean> getGlobalSettings() {
        System.out.println("Это getGlobalSettings - я тут"); //TODO удалить позже
        List<Settings> tempSetting = settingsRepositori.findAll();
        HashMap<String, Boolean> currentSettings = new HashMap<>();
        for (Settings temp : tempSetting) {
            currentSettings.put(temp.getCode(), temp.getValue().equals("true"));
        }
        return currentSettings;
    }
}
