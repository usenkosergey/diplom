package ru.skillbox.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.api.responses.TagsForTopicResponse;
import ru.skillbox.diplom.entities.Settings;
import ru.skillbox.diplom.repositories.SettingsRepositori;
import ru.skillbox.diplom.services.TagService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@ConfigurationProperties(prefix = "my")
public class ApiGeneralController {

    @Autowired
    private SettingsRepositori settingsRepositori;

    @Autowired
    private TagService tagService;

//        @Autowired TODO это почту я проверял, удалить.
//        EMailService eMailService;

    private List<String> initData = new ArrayList<String>();

    public List<String> getinitData() {
        return this.initData;
    }

    @GetMapping("/api/init")
    public Map<String, String> getInit() {
        System.out.println("Это инит - я тут"); //TODO удалить позже

        Map<String, String> initDataResponse = new HashMap<>();
        List<String> tempListInitData = initData;
        for (String data : tempListInitData) {
            String[] tempData = data.split(":");
            initDataResponse.put(tempData[0], tempData[1]);
        }

//        try { //TODO это почту я проверял, удалить.
//            eMailService.sendEmail();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        return initDataResponse;
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

    @GetMapping("api/tag")
    public TagsForTopicResponse getTagsForTopic() {
        return tagService.getTagsForTopic();
    }

    @GetMapping("api/auth/check")
    public Map<String, Boolean> chek() {
        HashMap<String, Boolean> temp = new HashMap<>();
        temp.put("result", false);
        return temp;
    }

}
