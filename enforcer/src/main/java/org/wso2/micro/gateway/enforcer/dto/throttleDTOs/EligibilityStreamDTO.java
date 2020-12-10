package org.wso2.micro.gateway.enforcer.dto.throttleDTOs;

public class EligibilityStreamDTO {
    private String messageID = "";
    private boolean isEligible = false;
    private String throttleKey = "";
    private int expiryTimeStamp = 0;

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public boolean isEligible() {
        return isEligible;
    }

    public void setEligible(boolean eligible) {
        isEligible = eligible;
    }

    public String getThrottleKey() {
        return throttleKey;
    }

    public void setThrottleKey(String throttleKey) {
        this.throttleKey = throttleKey;
    }

    public int getExpiryTimeStamp() {
        return expiryTimeStamp;
    }

    public void setExpiryTimeStamp(int expiryTimeStamp) {
        this.expiryTimeStamp = expiryTimeStamp;
    }
}
