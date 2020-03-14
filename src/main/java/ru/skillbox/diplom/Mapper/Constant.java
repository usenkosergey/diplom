package ru.skillbox.diplom.Mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Constant {
    public static Map<String, Integer> auth = new HashMap<>();

    public static Map responseTrue(){
        Map<String, Boolean> responseTrue = new HashMap<>();
        responseTrue.put("result", true);
        return responseTrue;
    }

    public static Map responseId(int id) {
        Map<String, Integer> responseId = new HashMap<>();
        responseId.put("id", id);
        return responseId;
    }

    public static Map responseError(String nameError, String descriptionError){
        Map<String, Object> responseError = new HashMap<>();
        responseError.put("result", false);
        Map<String, String> error = new TreeMap<String, String>();
        error.put(nameError,descriptionError);
        responseError.put("errors", error);
        return responseError;
    }
}
