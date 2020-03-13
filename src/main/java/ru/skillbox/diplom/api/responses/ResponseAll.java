package ru.skillbox.diplom.api.responses;

import java.util.Map;
import java.util.TreeMap;

public class ResponseAll {

    private Boolean result;
    private Map<String, String> errors;

    public ResponseAll(Boolean result){
        this.result = result;
    }

    public ResponseAll(Boolean result, String nameError, String descriptionError){
        this.result = result;
        Map<String, String> error = new TreeMap<String, String>();
        error.put(nameError,descriptionError);
        this.errors = error;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
