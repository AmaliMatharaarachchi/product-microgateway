/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.micro.gateway.enforcer.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.enforcer.Filter;
import org.wso2.micro.gateway.enforcer.api.RequestContext;
import org.wso2.micro.gateway.enforcer.api.config.APIConfig;
import org.wso2.micro.gateway.enforcer.dto.throttleDTOs.RequestStreamDTO;
import org.wso2.micro.gateway.enforcer.globalthrottle.ThrottleAgent;

import java.util.Map;

/**
 * This is the filter handling the authentication for the requests flowing through the gateway.
 */
public class ThrottleFilter implements Filter {
    public String UNLIMITED_TIER = "Unlimited";
    private static final Logger log = LogManager.getLogger(ThrottleFilter.class);
    private APIConfig apiConfig;

    @Override
    public void init(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    @Override
    public boolean handleRequest(RequestContext requestContext) {
        log.info("handle request called");
        RequestStreamDTO throttleEvent = generateGlobalThrottleEvent(requestContext);
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, String> map = oMapper.convertValue(throttleEvent, Map.class);
        log.info("map" + map.size() + "" + map.toString());
        ThrottleAgent.publishNonThrottledEvent(map);
        return true;
    }

    private RequestStreamDTO generateGlobalThrottleEvent(RequestContext requestContext) {
        RequestStreamDTO requestStreamDTO = new RequestStreamDTO();
        setCommonThrottleData(requestStreamDTO, requestContext);

//        requestStreamDTO.messageID = <string > context.attributes[MESSAGE_ID];
//        requestStreamDTO.userId = keyValidationDto.username;
//        requestStreamDTO.apiContext = apiContext;
//        requestStreamDTO.appTenant = keyValidationDto.subscriberTenantDomain;
//        requestStreamDTO.apiTenant = tenantDomain;
//        requestStreamDTO.apiName = context.getServiceName();
//        requestStreamDTO.appId = keyValidationDto.applicationId;
//
//        map<json> properties = getAdditionalProperties(context, req);
//        requestStreamDTO.properties = properties.toJsonString();
        return requestStreamDTO;
    }

    private void setCommonThrottleData(RequestStreamDTO requestStreamDTO, RequestContext requestContext) {
        requestStreamDTO.setAppTier("");
        requestStreamDTO.setApiTier("");
        requestStreamDTO.setSubscriptionTier("");
        requestStreamDTO.setApiContext("");
        requestStreamDTO.setSubscriptionKey("");
        requestStreamDTO.setAppKey("");
        //
        requestStreamDTO.setApiVersion("");
        requestStreamDTO.setApiKey("");
        requestStreamDTO.setSubscriptionKey("");

        if (requestStreamDTO.getApiTier() != UNLIMITED_TIER && requestStreamDTO.getApiTier() != "") {
            requestStreamDTO.setResourceTier(requestStreamDTO.getApiTier());
            requestStreamDTO.setResourceKey(requestStreamDTO.getApiKey());
        } else {
            String resourceKey = getResourceThrottleKey("apiContext", "apiVersion");
            requestStreamDTO.setResourceTier("");
            requestStreamDTO.setResourceKey("resourceKey");
        }

    }

    private String getResourceThrottleKey(String apiContext, String apiVersion) {
        String resourceLevelThrottleKey = apiContext;
        resourceLevelThrottleKey += "/" + apiVersion;
        resourceLevelThrottleKey += "MATCHING_RESOURCE" + ":" + "REQUEST_METHOD";
        return resourceLevelThrottleKey;
    }
}
