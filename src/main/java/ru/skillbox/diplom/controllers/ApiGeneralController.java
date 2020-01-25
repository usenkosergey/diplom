package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.api.responses.InitData;
import ru.skillbox.diplom.entities.Settings;
import ru.skillbox.diplom.repositories.SettingsRepositori;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/")
public class ApiGeneralController {

    @Autowired
    private SettingsRepositori settingsRepositori;

    @GetMapping("/api/init")
    public InitData getInit() {
        System.out.println("Это инит - я тут"); //TODO удалить позже
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
