package org.wso2.micro.gateway.enforcer.dto.throttleDTOs;

public class RequestStreamDTO {
    private int resetTimestamp = 0;
    private int remainingQuota = 0;
    private boolean isThrottled = false;
    private String messageID = "";
    private String apiKey = "";
    private String appKey = "";
    private boolean stopOnQuota = true;
    private String subscriptionKey = "";
    private String policyKey = "";
    private String appTier = "";
    private String apiTier = "";
    private String subscriptionTier = "";
    private String resourceKey = "";
    private String resourceTier = "";
    private String userId = "";
    private String apiContext = "";
    private String apiVersion = "";
    private String appTenant = "";
    private String apiTenant = "";
    private String appId = "";
    private String apiName = "";
    private String properties = "";
    private int resourceTierCount = -1;
    private int resourceTierUnitTime = -1;
    private String resourceTierTimeUnit = "";
    private int appTierCount = -1;
    private int appTierUnitTime = -1;
    private String appTierTimeUnit = "";
    private int apiTierCount = -1;
    private int apiTierUnitTime = -1;
    private String apiTierTimeUnit = "";
    private int subscriptionTierCount = -1;
    private int subscriptionTierUnitTime = -1;
    private String subscriptionTierTimeUnit = "";

    public int getResetTimestamp() {
        return resetTimestamp;
    }

    public void setResetTimestamp(int resetTimestamp) {
        this.resetTimestamp = resetTimestamp;
    }

    public int getRemainingQuota() {
        return remainingQuota;
    }

    public void setRemainingQuota(int remainingQuota) {
        this.remainingQuota = remainingQuota;
    }

    public boolean isThrottled() {
        return isThrottled;
    }

    public void setThrottled(boolean throttled) {
        isThrottled = throttled;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public boolean isStopOnQuota() {
        return stopOnQuota;
    }

    public void setStopOnQuota(boolean stopOnQuota) {
        this.stopOnQuota = stopOnQuota;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getPolicyKey() {
        return policyKey;
    }

    public void setPolicyKey(String policyKey) {
        this.policyKey = policyKey;
    }

    public String getAppTier() {
        return appTier;
    }

    public void setAppTier(String appTier) {
        this.appTier = appTier;
    }

    public String getApiTier() {
        return apiTier;
    }

    public void setApiTier(String apiTier) {
        this.apiTier = apiTier;
    }

    public String getSubscriptionTier() {
        return subscriptionTier;
    }

    public void setSubscriptionTier(String subscriptionTier) {
        this.subscriptionTier = subscriptionTier;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public String getResourceTier() {
        return resourceTier;
    }

    public void setResourceTier(String resourceTier) {
        this.resourceTier = resourceTier;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiContext() {
        return apiContext;
    }

    public void setApiContext(String apiContext) {
        this.apiContext = apiContext;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAppTenant() {
        return appTenant;
    }

    public void setAppTenant(String appTenant) {
        this.appTenant = appTenant;
    }

    public String getApiTenant() {
        return apiTenant;
    }

    public void setApiTenant(String apiTenant) {
        this.apiTenant = apiTenant;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public int getResourceTierCount() {
        return resourceTierCount;
    }

    public void setResourceTierCount(int resourceTierCount) {
        this.resourceTierCount = resourceTierCount;
    }

    public int getResourceTierUnitTime() {
        return resourceTierUnitTime;
    }

    public void setResourceTierUnitTime(int resourceTierUnitTime) {
        this.resourceTierUnitTime = resourceTierUnitTime;
    }

    public String getResourceTierTimeUnit() {
        return resourceTierTimeUnit;
    }

    public void setResourceTierTimeUnit(String resourceTierTimeUnit) {
        this.resourceTierTimeUnit = resourceTierTimeUnit;
    }

    public int getAppTierCount() {
        return appTierCount;
    }

    public void setAppTierCount(int appTierCount) {
        this.appTierCount = appTierCount;
    }

    public int getAppTierUnitTime() {
        return appTierUnitTime;
    }

    public void setAppTierUnitTime(int appTierUnitTime) {
        this.appTierUnitTime = appTierUnitTime;
    }

    public String getAppTierTimeUnit() {
        return appTierTimeUnit;
    }

    public void setAppTierTimeUnit(String appTierTimeUnit) {
        this.appTierTimeUnit = appTierTimeUnit;
    }

    public int getApiTierCount() {
        return apiTierCount;
    }

    public void setApiTierCount(int apiTierCount) {
        this.apiTierCount = apiTierCount;
    }

    public int getApiTierUnitTime() {
        return apiTierUnitTime;
    }

    public void setApiTierUnitTime(int apiTierUnitTime) {
        this.apiTierUnitTime = apiTierUnitTime;
    }

    public String getApiTierTimeUnit() {
        return apiTierTimeUnit;
    }

    public void setApiTierTimeUnit(String apiTierTimeUnit) {
        this.apiTierTimeUnit = apiTierTimeUnit;
    }

    public int getSubscriptionTierCount() {
        return subscriptionTierCount;
    }

    public void setSubscriptionTierCount(int subscriptionTierCount) {
        this.subscriptionTierCount = subscriptionTierCount;
    }

    public int getSubscriptionTierUnitTime() {
        return subscriptionTierUnitTime;
    }

    public void setSubscriptionTierUnitTime(int subscriptionTierUnitTime) {
        this.subscriptionTierUnitTime = subscriptionTierUnitTime;
    }

    public String getSubscriptionTierTimeUnit() {
        return subscriptionTierTimeUnit;
    }

    public void setSubscriptionTierTimeUnit(String subscriptionTierTimeUnit) {
        this.subscriptionTierTimeUnit = subscriptionTierTimeUnit;
    }

}
