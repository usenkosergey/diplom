package ru.skillbox.diplom.api.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsRequest {
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean STATISTICS_IS_PUBLIC;
    @JsonProperty("POST_PREMODERATION")
    private boolean POST_PREMODERATION;
    @JsonProperty("MULTIUSER_MODE")
    private boolean MULTIUSER_MODE;

    public SettingsRequest() {
    }

    public boolean isSTATISTICS_IS_PUBLIC() {
        return STATISTICS_IS_PUBLIC;
    }

    public void setSTATISTICS_IS_PUBLIC(boolean STATISTICS_IS_PUBLIC) {
        this.STATISTICS_IS_PUBLIC = STATISTICS_IS_PUBLIC;
    }

    public boolean isPOST_PREMODERATION() {
        return POST_PREMODERATION;
    }

    public void setPOST_PREMODERATION(boolean POST_PREMODERATION) {
        this.POST_PREMODERATION = POST_PREMODERATION;
    }

    public boolean isMULTIUSER_MODE() {
        return MULTIUSER_MODE;
    }

    public void setMULTIUSER_MODE(boolean MULTIUSER_MODE) {
        this.MULTIUSER_MODE = MULTIUSER_MODE;
    }
}
