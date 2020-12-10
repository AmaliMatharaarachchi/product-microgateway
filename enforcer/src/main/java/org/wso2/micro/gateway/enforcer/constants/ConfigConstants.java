/*
 * Copyright (c) 2020, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.micro.gateway.enforcer.constants;

/**
 * This class holds the constant keys related to the Microgateway Configurations which will be read from the config file
 * or dynamically configured via the control plane.
 */
public class ConfigConstants {
    public static final String ENFORCER_HOME = "ENFORCER_HOME";
    public static final String CONF_DIR = "conf";
    public static final String CONF_FILTER_TABLE = "filter";
    // JWT Token Config
    public static final String JWT_TOKEN_CONFIG = "jwtTokenConfig";
    public static final String JWT_TOKEN_ISSUER = "issuer";
    public static final String JWT_TOKEN_ISSUER_NAME = "name";
    public static final String JWT_TOKEN_CERTIFICATE_ALIAS = "certificateAlias";
    public static final String JWT_TOKEN_JWKS_URL = "jwksURL";
    public static final String JWT_TOKEN_VALIDATE_SUBSCRIPTIONS = "validateSubscription";
    public static final String JWT_TOKEN_CONSUMER_KEY_CLAIM = "consumerKeyClaim";
    public static final String JWT_TOKEN_CLAIM_MAPPER_CLASS_NAME = "claimMapperClassName";
    public static final String JWT_TOKEN_ENABLE_REMOTE_USER_CLAIM_RETRIEVAL = "remoteUserClaimRetrievalEnabled";
    public static final String JWT_TOKEN_CLAIMS = "claims";

    // Event hub configuration
    public static final String EVENT_HUB_ENABLE = "eventHub.enable";
    public static final String EVENT_HUB_SERVICE_URL = "eventHub.serviceUrl";
    public static final String EVENT_HUB_USERNAME = "eventHub.username";
    public static final String EVENT_HUB_PASSWORD = "eventHub.password";
    public static final String EVENT_HUB_EVENT_LISTENING_ENDPOINT = "eventHub.eventListeningEndpoints";

    //KeyStore and Trust Store configuration
    public static final String MGW_KEY_STORE_LOCATION = "keystore.location";
    public static final String MGW_KEY_STORE_TYPE = "keystore.type";
    public static final String MGW_KEY_STORE_PASSWORD = "keystore.password";
    public static final String MGW_TRUST_STORE_LOCATION = "truststore.location";
    public static final String MGW_TRUST_STORE_TYPE = "truststore.type";
    public static final String MGW_TRUST_STORE_PASSWORD = "truststore.password";

    //tm configurations
    public static final String TM_BINARY_AGENT_THROTTLE_CONF_INSTANCE_ID = "throttlingConfig.binary.agent";
    public static final String TM_BINARY_AGENT_PROTOCOL_VERSIONS = "sslEnabledProtocols";
    public static final String TM_BINARY_AGENT_CIPHERS = "ciphers";
    public static final String TM_AGENT_QUEUE_SIZE = "queueSize";
    public static final String TM_AGENT_BATCH_SIZE = "batchSize";
    public static final String TM_AGENT_THREAD_POOL_CORE_SIZE = "corePoolSize";
    public static final String TM_AGENT_THREAD_POOL_MAXIMUM_SIZE = "maxPoolSize";
    public static final String TM_AGENT_SOCKET_TIMEOUT_MS = "socketTimeoutMS";
    public static final String TM_AGENT_THREAD_POOL_KEEP_ALIVE_TIME = "keepAliveTimeInPool";
    public static final String TM_AGENT_RECONNECTION_INTERVAL = "reconnectionInterval";
    public static final String TM_AGENT_MAX_TRANSPORT_POOL_SIZE = "maxTransportPoolSize";
    public static final String TM_AGENT_MAX_IDLE_CONNECTIONS = "maxIdleConnections";
    public static final String TM_AGENT_EVICTION_TIME_PERIOD = "evictionTimePeriod";
    public static final String TM_AGENT_MIN_IDLE_TIME_IN_POOL = "minIdleTimeInPool";
    public static final String TM_AGENT_SECURE_MAX_TRANSPORT_POOL_SIZE = "secureMaxIdleTransportPoolSize";
    public static final String TM_AGENT_SECURE_MAX_IDLE_CONNECTIONS = "secureMaxIdleConnections";
    public static final String TM_AGENT_SECURE_EVICTION_TIME_PERIOD = "secureEvictionTimePeriod";
    public static final String TM_AGENT_SECURE_MIN_IDLE_TIME_IN_POOL = "secureMinIdleTimeInPool";

}
