package ru.skillbox.diplom.Mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Constant {

    public static Map<String, Integer> auth = new HashMap<>();

    public static Map responseTrue(){
        Map<String, Boolean> responseTrue = new HashMap<>();
        responseTrue.put("result", true);
        return responseTrue;
    }

    public static Map responseFalse(){
        Map<String, Boolean> responseFalse = new HashMap<>();
        responseFalse.put("result", false);
        return responseFalse;
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

    public static String codeGenerator(){
        int length = 10;
        Random r = new Random();
        String s = r.ints(48, 122)
                .filter(i -> (i < 57 || i > 65) && (i < 90 || i > 97))
                .mapToObj(i -> (char) i)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        return s;
    }
}
