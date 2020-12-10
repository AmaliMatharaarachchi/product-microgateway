/*
 *  Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.micro.gateway.enforcer.globalthrottle;

import org.wso2.micro.gateway.enforcer.dto.throttleConfigDTOs.TMBinaryAgentConfigDTO;
import org.wso2.micro.gateway.enforcer.globalthrottle.databridge.agent.conf.AgentConfiguration;
import org.wso2.micro.gateway.enforcer.globalthrottle.databridge.publisher.PublisherConfiguration;
import org.wso2.micro.gateway.enforcer.globalthrottle.databridge.publisher.ThrottleDataPublisher;

import java.util.Map;

/**
 * This class is used for ballerina interop invocations related to Global Throttle Event Publishing
 * via binary communication.
 */
public class ThrottleAgent {

    private static ThrottleDataPublisher throttleDataPublisher = null;

//    public static void setTMBinaryAgentConfiguration(TMBinaryAgentConfigDTO publisherConfiguration) {
//        AgentConfiguration.getInstance().setConfiguration(publisherConfiguration);
//    }

//    public static void setTMBinaryPublisherConfiguration(Map<String, Object> publisherConfiguration) {
//        PublisherConfiguration.getInstance().setConfiguration(publisherConfiguration);
//    }

    public static void startThrottlePublisherPool() {
        throttleDataPublisher = new ThrottleDataPublisher();
    }

    public static void publishNonThrottledEvent(Map<String, String> throttleEvent) {
        throttleDataPublisher.publishNonThrottledEvent(throttleEvent);
    }
}
