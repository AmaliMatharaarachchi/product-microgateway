/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
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
package org.wso2.apimgt.gateway.cli.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import org.apache.commons.lang3.StringUtils;
import org.ballerinalang.packerina.init.InitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.apimgt.gateway.cli.codegen.CodeGenerationContext;
import org.wso2.apimgt.gateway.cli.codegen.CodeGenerator;
import org.wso2.apimgt.gateway.cli.codegen.ThrottlePolicyGenerator;
import org.wso2.apimgt.gateway.cli.config.TOMLConfigParser;
import org.wso2.apimgt.gateway.cli.constants.GatewayCliConstants;
import org.wso2.apimgt.gateway.cli.constants.RESTServiceConstants;
import org.wso2.apimgt.gateway.cli.exception.BallerinaServiceGenException;
import org.wso2.apimgt.gateway.cli.exception.CLIInternalException;
import org.wso2.apimgt.gateway.cli.exception.CLIRuntimeException;
import org.wso2.apimgt.gateway.cli.exception.CliLauncherException;
import org.wso2.apimgt.gateway.cli.exception.ConfigParserException;
import org.wso2.apimgt.gateway.cli.exception.HashingException;
import org.wso2.apimgt.gateway.cli.hashing.HashUtils;
import org.wso2.apimgt.gateway.cli.model.config.BasicAuth;
import org.wso2.apimgt.gateway.cli.model.config.Client;
import org.wso2.apimgt.gateway.cli.model.config.Config;
import org.wso2.apimgt.gateway.cli.model.config.ContainerConfig;
import org.wso2.apimgt.gateway.cli.model.config.Token;
import org.wso2.apimgt.gateway.cli.model.config.TokenBuilder;
import org.wso2.apimgt.gateway.cli.model.config.Etcd;
import org.wso2.apimgt.gateway.cli.model.config.BasicAuth;
import org.wso2.apimgt.gateway.cli.model.rest.ClientCertMetadataDTO;
import org.wso2.apimgt.gateway.cli.model.rest.ext.ExtendedAPI;
import org.wso2.apimgt.gateway.cli.model.rest.policy.ApplicationThrottlePolicyDTO;
import org.wso2.apimgt.gateway.cli.model.rest.policy.SubscriptionThrottlePolicyDTO;
import org.wso2.apimgt.gateway.cli.oauth.OAuthService;
import org.wso2.apimgt.gateway.cli.oauth.OAuthServiceImpl;
import org.wso2.apimgt.gateway.cli.rest.RESTAPIService;
import org.wso2.apimgt.gateway.cli.rest.RESTAPIServiceImpl;
import org.wso2.apimgt.gateway.cli.utils.GatewayCmdUtils;
import org.wso2.apimgt.gateway.cli.utils.OpenApiCodegenUtils;
import org.wso2.apimgt.gateway.cli.utils.grpc.GRPCUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.wso2.apimgt.gateway.cli.utils.grpc.GrpcGen.BalGenerationConstants.PROTO_SUFFIX;

/**
 * This class represents the "setup" command and it holds arguments and flags specified by the user.
 */
@Parameters(commandNames = "setup", commandDescription = "setup information")
public class SetupCmd implements GatewayLauncherCmd {
    private static final Logger logger = LoggerFactory.getLogger(SetupCmd.class);
    private static PrintStream outStream = System.out;

    @SuppressWarnings("unused")
    @Parameter(hidden = true, required = true)
    private List<String> mainArgs;

    @SuppressWarnings("unused")
    @Parameter(names = "--java.debug", hidden = true)
    private String javaDebugPort;

    @Parameter(names = {"-u", "--username"}, hidden = true)
    private String username;

    @Parameter(names = {"-p", "--password"}, hidden = true)
    private String password;

    @SuppressWarnings("unused")
    @Parameter(names = {"-l", "--label"}, hidden = true)
    private String label;

    @Parameter(names = {"-s", "--server-url"}, hidden = true)
    private String baseURL;

    @Parameter(names = {"-oa", "--openapi"}, hidden = true)
    private String openApi;

    @Parameter(names = {"-e", "--endpoint"}, hidden = true)
    private String endpoint;

    @Parameter(names = {"-ec", "--endpointConfig"}, hidden = true)
    private String endpointConfig;

    @Parameter(names = {"-t", "--truststore"}, hidden = true)
    private String trustStoreLocation;

    @Parameter(names = {"-w", "--truststore-pass"}, hidden = true)
    private String trustStorePassword;

    @Parameter(names = {"-c", "--config"}, hidden = true)
    private String toolkitConfigPath;

    @SuppressWarnings("unused")
    @Parameter(names = {"-d", "--deployment-config"}, hidden = true)
    private String deploymentConfigPath;

    @SuppressWarnings("unused")
    @Parameter(names = {"-a", "--api-name"}, hidden = true)
    private String apiName;

    @SuppressWarnings("unused")
    @Parameter(names = {"-v", "--version"}, hidden = true)
    private String version;

    @SuppressWarnings("unused")
    @Parameter(names = {"-f", "--force"}, hidden = true, arity = 0)
    private boolean isForcefully;

    @SuppressWarnings("unused")
    @Parameter(names = {"-k", "--insecure"}, hidden = true, arity = 0)
    private boolean isInsecure;

    @Parameter(names = {"-b", "--security"}, hidden = true)
    private String security;

    @Parameter(names = {"-etcd", "--enable-etcd"}, hidden = true, arity = 0)
    private boolean isEtcdEnabled;

    private String publisherEndpoint;
    private String adminEndpoint;
    private String registrationEndpoint;
    private String tokenEndpoint;
    private String publisherEndpointforConfig;
    private String adminEndpointforConfig;
    private String registrationEndpointforConfig;
    private String tokenEndpointforConfig;
    private String restVersion;


    private String clientSecret;
    private boolean isOverwriteRequired;


    public void execute() {
        String clientID;
        String workspace = GatewayCmdUtils.getUserDir();
        boolean isOpenApi = StringUtils.isNotEmpty(openApi);
        boolean isGRPC = false;
        String grpc = null;
        String projectName = GatewayCmdUtils.getProjectName(mainArgs);
        if (projectName.contains(" ")) {
            throw GatewayCmdUtils.createUsageException("Only one argument accepted as the project name. but provided:" +
                    " " + projectName);
        }
        if (StringUtils.isEmpty(toolkitConfigPath)) {
            toolkitConfigPath = GatewayCmdUtils.getMainConfigLocation();
        }

        if (new File(workspace + File.separator + projectName).exists() && !isForcefully) {
            throw GatewayCmdUtils.createUsageException("Project name `" + projectName
                    + "` already exist. use -f or --force to forcefully update the project directory.");
        }

        init(projectName, toolkitConfigPath, deploymentConfigPath);

        //set etcd requirement
        Etcd etcd = new Etcd();
        etcd.setEtcdEnabled(isEtcdEnabled);
        GatewayCmdUtils.setEtcd(etcd);
        logger.debug("Etcd is enabled : " + isEtcdEnabled);

        Config config = GatewayCmdUtils.getConfig();
        isOverwriteRequired = false;

        /*
         * If api is created via an api definition, the setup flow is altered
         */
        if (isOpenApi) {
            outStream.println("Loading Open Api Specification from Path: " + openApi);
            String api = OpenApiCodegenUtils.readApi(openApi);

            if (openApi.toLowerCase(Locale.ENGLISH).endsWith(PROTO_SUFFIX)) {
                grpc = openApi;
                outStream.println("Loading ProtoBuff Api Specification from Path: " + grpc);
                GRPCUtils grpcUtils = new GRPCUtils(grpc);
                grpcUtils.execute();
                logger.debug("Successfully read the api definition file");
                CodeGenerator codeGenerator = new CodeGenerator();
                try {
                    if (StringUtils.isEmpty(endpointConfig)) {
                        if (StringUtils.isEmpty(endpoint)) {
                            /*
                             * if an endpoint config or an endpoint is not provided as an argument, it is prompted from
                             * the user
                             */
                            if ((endpoint = promptForTextInput("Enter Endpoint URL: ")).trim().isEmpty()) {
                                throw GatewayCmdUtils.createUsageException("Micro gateway setup failed: empty endpoint.");
                            }
                        }
                    }
                    codeGenerator.generateGrpc(projectName, api, true);
                    //Initializing the ballerina project and creating .bal folder.
                    logger.debug("Creating source artifacts");
                    InitHandler.initialize(Paths.get(GatewayCmdUtils.getProjectDirectoryPath(projectName)), null,
                            new ArrayList<>(), null);
                } catch (IOException | BallerinaServiceGenException e) {
                    logger.error("Error while generating ballerina source.", e);
                    throw new CLIInternalException("Error while generating ballerina source.");
                }
                outStream.println("Setting up project " + projectName + " is successful.");
            } else {
                logger.debug("Successfully read the api definition file");
                CodeGenerator codeGenerator = new CodeGenerator();
                try {
                    if (StringUtils.isEmpty(endpointConfig)) {
                        if (StringUtils.isEmpty(endpoint)) {
                            /*
                             * if an endpoint config or an endpoint is not provided as an argument, it is prompted from
                             * the user
                             */
                            if ((endpoint = promptForTextInput("Enter Endpoint URL: ")).trim().isEmpty()) {
                                throw GatewayCmdUtils.createUsageException("Micro gateway setup failed: empty endpoint.");
                            }
                        }
                        endpointConfig = "{\"production_endpoints\":{\"url\":\"" + endpoint.trim() +
                                "\"},\"endpoint_type\":\"http\"}";
                    }
                    codeGenerator.generate(projectName, api, endpointConfig, true);
                    //Initializing the ballerina project and creating .bal folder.
                    logger.debug("Creating source artifacts");
                    InitHandler.initialize(Paths.get(GatewayCmdUtils.getProjectDirectoryPath(projectName)), null,
                            new ArrayList<>(), null);
                } catch (IOException | BallerinaServiceGenException e) {
                    logger.error("Error while generating ballerina source.", e);
                    throw new CLIInternalException("Error while generating ballerina source.");
                }
                outStream.println("Setting up project " + projectName + " is successful.");
            }

        } else {

            validateAPIGetRequestParams(label, apiName, version);
            //Setup username
            String configuredUser = config.getToken().getUsername();
            if (StringUtils.isEmpty(configuredUser)) {
                if (StringUtils.isEmpty(username)) {
                    isOverwriteRequired = true;
                    if ((username = promptForTextInput("Enter Username: ")).trim().isEmpty()) {
                        throw GatewayCmdUtils.createUsageException("Micro gateway setup failed: empty username.");
                    }
                }
            } else {
                username = configuredUser;
            }

            //Setup password
            if (StringUtils.isEmpty(password)) {
                if ((password = promptForPasswordInput("Enter Password for " + username + ": ")).trim().isEmpty()) {
                    if (StringUtils.isEmpty(password)) {
                        password = promptForPasswordInput("Password can't be empty; enter password for "
                                + username + ": ");
                        if (password.trim().isEmpty()) {
                            throw GatewayCmdUtils.createUsageException("Micro gateway setup failed: empty password.");
                        }
                    }
                }
            }

            //setup endpoints
            Token configToken = config.getToken();
            setEndpoints(configToken);

            //configure trust store
            String configuredTrustStore = config.getToken().getTrustStoreLocation();
            if (StringUtils.isEmpty(configuredTrustStore)) {
                if (StringUtils.isEmpty(trustStoreLocation)) {
                    isOverwriteRequired = true;
                    if ((trustStoreLocation = promptForTextInput(
                            "Enter Trust store location: [" + RESTServiceConstants.DEFAULT_TRUSTSTORE_PATH +
                                    "]")).trim()
                            .isEmpty()) {
                        trustStoreLocation = RESTServiceConstants.DEFAULT_TRUSTSTORE_PATH;
                    }
                }
            } else {
                trustStoreLocation = configuredTrustStore;
            }

            //configure trust store password
            String encryptedPass = config.getToken().getTrustStorePassword();
            String configuredTrustStorePass;
            if (StringUtils.isEmpty(encryptedPass)) {
                configuredTrustStorePass = null;
            } else {
                try {
                    configuredTrustStorePass = GatewayCmdUtils.decrypt(encryptedPass, password);
                } catch (CliLauncherException e) {
                    //different password used to encrypt
                    configuredTrustStorePass = null;
                }
            }

            if (StringUtils.isEmpty(configuredTrustStorePass)) {
                if (StringUtils.isEmpty(trustStorePassword)) {
                    isOverwriteRequired = true;
                    if ((trustStorePassword = promptForPasswordInput("Enter Trust store password: " +
                            "[ use default? ]")).trim()
                            .isEmpty()) {
                        trustStorePassword = RESTServiceConstants.DEFAULT_TRUSTSTORE_PASS;
                    }
                }
            } else {
                trustStorePassword = configuredTrustStorePass;
            }

            File trustStoreFile = new File(trustStoreLocation);
            if (!trustStoreFile.isAbsolute()) {
                trustStoreLocation = GatewayCmdUtils.getUnixPath(GatewayCmdUtils.getCLIHome() + File.separator
                        + trustStoreLocation);
            }
            trustStoreFile = new File(trustStoreLocation);
            if (!trustStoreFile.exists()) {
                logger.error("Provided trust store location {} does not exist.", trustStoreLocation);
                throw new CLIRuntimeException("Provided trust store location does not exist.");
            }

            //set the trustStore
            System.setProperty("javax.net.ssl.keyStoreType", "pkcs12");
            System.setProperty("javax.net.ssl.trustStore", trustStoreLocation);
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);

            //Security Schemas settings
            if (security == null) {
                security = "oauth2";
            } else if (security == "") {
                security = "oauth2";
            }
            setSecuritySchemas(security);

            OAuthService manager = new OAuthServiceImpl();
            clientID = config.getToken().getClientId();
            String encryptedSecret = config.getToken().getClientSecret();
            if (!StringUtils.isEmpty(clientID.trim()) && !StringUtils.isEmpty(encryptedSecret.trim())) {
                try {
                    clientSecret = GatewayCmdUtils.decrypt(encryptedSecret, password);
                } catch (CliLauncherException e) {
                    //different password used to encrypt
                    clientSecret = null;
                }
            }

            if (StringUtils.isEmpty(clientID) || StringUtils.isEmpty(clientSecret)) {
                String[] clientInfo = manager
                        .generateClientIdAndSecret(registrationEndpoint, username, password.toCharArray(), isInsecure);
                clientID = clientInfo[0];
                clientSecret = clientInfo[1];
            }

            String accessToken = manager
                    .generateAccessToken(tokenEndpoint, username, password.toCharArray(), clientID, clientSecret,
                            isInsecure);

            List<ExtendedAPI> apis = new ArrayList<>();
            RESTAPIService service = new RESTAPIServiceImpl(publisherEndpoint, adminEndpoint, isInsecure);
            if (label != null) {
                apis = service.getAPIs(label, accessToken);
            } else {
                ExtendedAPI api = service.getAPI(apiName, version, accessToken);
                if (api != null) {
                    apis.add(api);
                }
            }
            if (apis == null || (apis != null && apis.isEmpty())) {
                // Delete folder
                GatewayCmdUtils.deleteProject(workspace + File.separator + projectName);
                String errorMsg;
                if (label != null) {
                    errorMsg = "No APIs found for the given label: " + label;
                } else {
                    errorMsg = "No Published APIs matched for name:" + apiName + ", version:" + version;
                }
                throw new CLIRuntimeException(errorMsg);
            }
            List<ApplicationThrottlePolicyDTO> applicationPolicies = service.getApplicationPolicies(accessToken);
            List<SubscriptionThrottlePolicyDTO> subscriptionPolicies = service.getSubscriptionPolicies(accessToken);
            List<ClientCertMetadataDTO> clientCertificates = service.getClientCertificates(accessToken);
            logger.info(String.valueOf(clientCertificates));

            ThrottlePolicyGenerator policyGenerator = new ThrottlePolicyGenerator();
            CodeGenerator codeGenerator = new CodeGenerator();
            boolean changesDetected;
            try {
                policyGenerator.generate(GatewayCmdUtils.getProjectSrcDirectoryPath(projectName) + File.separator
                        + GatewayCliConstants.POLICY_DIR, applicationPolicies, subscriptionPolicies);
                codeGenerator.generate(projectName, apis, true);
                //Initializing the ballerina project and creating .bal folder.
                InitHandler.initialize(Paths.get(GatewayCmdUtils.getProjectDirectoryPath(projectName)), null,
                        new ArrayList<>(), null);
                try {
                    changesDetected = HashUtils.detectChanges(apis, subscriptionPolicies,
                            applicationPolicies, projectName);
                } catch (HashingException e) {
                    logger.error("Error while checking for changes of resources. Skipping no-change detection..", e);
                    throw new CLIInternalException(
                            "Error while checking for changes of resources. Skipping no-change detection..");
                }
            } catch (IOException | BallerinaServiceGenException e) {
                logger.error("Error while generating ballerina source.", e);
                throw new CLIInternalException("Error while generating ballerina source.");
            }

            //if all the operations are success, write new config to file
            if (isOverwriteRequired) {
                Config newConfig = new Config();
                Client client = new Client();
                client.setHttpRequestTimeout(1000000);
                newConfig.setClient(client);

                String encryptedCS = GatewayCmdUtils.encrypt(clientSecret, password);
                String encryptedTrustStorePass = GatewayCmdUtils.encrypt(trustStorePassword, password);
                Token token = new TokenBuilder()
                        .setRestVersion(restVersion)
                        .setPublisherEndpoint(publisherEndpointforConfig)
                        .setAdminEndpoint(adminEndpointforConfig)
                        .setRegistrationEndpoint(registrationEndpointforConfig)
                        .setTokenEndpoint(tokenEndpointforConfig)
                        .setUsername(username)
                        .setClientId(clientID)
                        .setClientSecret(encryptedCS)
                        .setTrustStoreLocation(trustStoreLocation)
                        .setTrustStorePassword(encryptedTrustStorePass)
                        .build();
                newConfig.setToken(token);
                newConfig.setCorsConfiguration(GatewayCmdUtils.getDefaultCorsConfig());
                GatewayCmdUtils.saveConfig(newConfig, toolkitConfigPath);
            }

            if (!changesDetected) {
                outStream.println(
                        "No changes received from the server since the previous setup."
                                + " If you have already a built distribution, it can be reused.");
            }
            outStream.println("Setting up project " + projectName + " is successful.");

            //There should not be any logic after this system exit
            if (!changesDetected) {
                Runtime.getRuntime().exit(GatewayCliConstants.EXIT_CODE_NOT_MODIFIED);
            }
        }
    }

    /**
     * Validates label, API name and version parameters in for below conditions.
     * 1. Either label should be provided or both API name and version should be provided.
     * 2. Cannot provide all params; i.e. label, API name and version at the same time.
     *
     * @param label   Label name
     * @param apiName API name
     * @param version API version
     */
    private void validateAPIGetRequestParams(String label, String apiName, String version) {
        if ((StringUtils.isEmpty(label) && (StringUtils.isEmpty(apiName) || StringUtils.isEmpty(version))) ||
                StringUtils.isNotEmpty(label) && (StringUtils.isNotEmpty(apiName) || StringUtils.isNotEmpty(version)) ||
                (StringUtils.isEmpty(apiName) && StringUtils.isNotEmpty(version)) ||
                (StringUtils.isNotEmpty(apiName) && StringUtils.isEmpty(version))) {
            throw GatewayCmdUtils.createUsageException(
                    "Either label (-l <label>) or API name (-a <api-name>) with version (-v <version>) "
                            + "should be provided."
                            + "\n\nEx:\tmicro-gw setup accounts-project -l accounts"
                            + "\n\tmicro-gw setup pizzashack-project -a Pizzashack -v 1.0.0");
        }
    }

    @Override
    public String getName() {
        return GatewayCliCommands.SETUP;
    }

    @Override
    public void setParentCmdParser(JCommander parentCmdParser) {
    }

    private String promptForTextInput(String msg) {
        outStream.println(msg);
        return System.console().readLine();
    }

    private String promptForPasswordInput(String msg) {
        outStream.println(msg);
        return new String(System.console().readPassword());
    }

    private static void init(String projectName, String configPath, String deploymentConfigPath) {
        try {
            GatewayCmdUtils.createProjectStructure(projectName);
            GatewayCmdUtils.createDeploymentConfig(projectName, deploymentConfigPath);

            Path configurationFile = Paths.get(configPath);
            if (Files.exists(configurationFile)) {
                Config config = TOMLConfigParser.parse(configPath, Config.class);
                GatewayCmdUtils.setConfig(config);
            } else {
                logger.error("Configuration: {} Not found.", configPath);
                throw new CLIInternalException("Error occurred while loading configurations.");
            }

            deploymentConfigPath = GatewayCmdUtils.getDeploymentConfigLocation(projectName);
            ContainerConfig containerConfig = TOMLConfigParser.parse(deploymentConfigPath, ContainerConfig.class);
            GatewayCmdUtils.setContainerConfig(containerConfig);

            CodeGenerationContext codeGenerationContext = new CodeGenerationContext();
            codeGenerationContext.setProjectName(projectName);
            GatewayCmdUtils.setCodeGenerationContext(codeGenerationContext);
        } catch (ConfigParserException e) {
            logger.error("Error occurred while parsing the configurations {}", configPath, e);
            throw new CLIInternalException("Error occurred while loading configurations.");
        } catch (IOException e) {
            logger.error("Error occurred while generating project configurationss", e);
            throw new CLIInternalException("Error occurred while loading configurations.");
        }
    }

    public void setSecuritySchemas(String schemas) {
        Config config = GatewayCmdUtils.getConfig();
        BasicAuth basicAuth = new BasicAuth();
        boolean basic = false;
        boolean oauth2 = false;
        String[] schemasArray = schemas.trim().split("\\s*,\\s*");
        for (int i = 0; i < schemasArray.length; i++) {
            if (schemasArray[i].equalsIgnoreCase("basic")) {
                basic = true;
            } else if (schemasArray[i].equalsIgnoreCase("oauth2")) {
                oauth2 = true;
            }
        }
        if (basic && oauth2) {
            basicAuth.setOptional(true);
            basicAuth.setRequired(false);
        } else if (basic && !oauth2) {
            basicAuth.setRequired(true);
            basicAuth.setOptional(false);
        } else if (!basic && oauth2) {
            basicAuth.setOptional(false);
            basicAuth.setRequired(false);
        }
        config.setBasicAuth(basicAuth);
    }

    /**
     * Set endpoints of publisher, admin, registration and token
     *
     * @param token token from config file
     */
    private void setEndpoints(Token token) {
        boolean endPointsneeded;
        boolean baseURLneeded;
        boolean restVersionNeeded;

        publisherEndpointforConfig = token.getPublisherEndpoint();
        adminEndpointforConfig = token.getAdminEndpoint();
        registrationEndpointforConfig = token.getRegistrationEndpoint();
        tokenEndpointforConfig = token.getTokenEndpoint();
        restVersion = token.getRestVersion();

        endPointsneeded = StringUtils.isEmpty(publisherEndpointforConfig) || StringUtils.isEmpty(adminEndpointforConfig)
                || StringUtils.isEmpty(registrationEndpointforConfig) || StringUtils.isEmpty(tokenEndpointforConfig);

        baseURLneeded = publisherEndpointforConfig.contains("{baseURL}") || adminEndpointforConfig.contains("{baseURL}")
                || registrationEndpointforConfig.contains("{baseURL}") || tokenEndpointforConfig.contains("{baseURL}")
                || endPointsneeded;

        restVersionNeeded = publisherEndpointforConfig.contains("{restVersion}") ||
                adminEndpointforConfig.contains("{restVersion}") ||
                registrationEndpointforConfig.contains("{restVersion}") || endPointsneeded;

        //generate endpoints
        if (endPointsneeded) {
            if (StringUtils.isEmpty(publisherEndpointforConfig)) {
                publisherEndpointforConfig = RESTServiceConstants.CONFIG_PUBLISHER_ENDPOINT;
            }
            if (StringUtils.isEmpty(adminEndpointforConfig)) {
                adminEndpointforConfig = RESTServiceConstants.CONFIG_ADMIN_ENDPOINT;
            }
            if (StringUtils.isEmpty(registrationEndpointforConfig)) {
                registrationEndpointforConfig = RESTServiceConstants.CONFIG_REGISTRATION_ENDPOINT;
            }
            if (StringUtils.isEmpty(tokenEndpointforConfig)) {
                tokenEndpointforConfig = RESTServiceConstants.CONFIG_TOKEN_ENDPOINT;
            }
        }

        //setup base URL
        if (baseURLneeded) {
            String userInputURL = getBaseURLfromCmd();
            if (!userInputURL.isEmpty()) {
                baseURL = userInputURL;
            } else {
                baseURL = RESTServiceConstants.DEFAULT_HOST;
            }

            publisherEndpointforConfig = publisherEndpointforConfig.replace("{baseURL}", baseURL);
            adminEndpointforConfig = adminEndpointforConfig.replace("{baseURL}", baseURL);
            registrationEndpointforConfig = registrationEndpointforConfig.replace("{baseURL}", baseURL);
            tokenEndpointforConfig = tokenEndpointforConfig.replace("{baseURL}", baseURL);

            isOverwriteRequired = true;
        }

        //get rest version
        if (restVersionNeeded) {
            if (StringUtils.isEmpty(restVersion)) {
                restVersion = RESTServiceConstants.CONFIG_REST_VERSION;
            }
            informRestVersiontoUser(restVersion);
        }

        //generate endpoint URLs
        generateURLs(restVersion);
    }

    /**
     * generate URLs of publisher, admin, registration and token endpoints
     *
     * @param restVersion API Manager's REST version
     */
    private void generateURLs(String restVersion) {

        try {
            publisherEndpoint=publisherEndpointforConfig;
            adminEndpoint=adminEndpointforConfig;
            registrationEndpoint=registrationEndpointforConfig;
            tokenEndpoint=tokenEndpointforConfig;

            publisherEndpoint = new URL(publisherEndpoint.replace("{restVersion}", restVersion))
                    .toString();
            adminEndpoint = new URL(adminEndpoint.replace("{restVersion}", restVersion))
                    .toString();
            registrationEndpoint = new URL(registrationEndpoint.replace("{restVersion}", restVersion))
                    .toString();
            tokenEndpoint = new URL(tokenEndpoint).toString();

        } catch (MalformedURLException e) {
            logger.error("Malformed URL provided {}", baseURL);
            throw new CLIInternalException("Error occurred while setting up URL configurations.");
        }
    }

    /**
     * prompt to get the base URL
     */
    private String getBaseURLfromCmd() {
        String userInputURL;
        userInputURL = promptForTextInput("Enter APIM base URL [" + baseURL + "]: ").trim();
        return userInputURL;
    }

    /**
     * inform user on REST version of endpoint URLs
     *
     * @param restVersion API Manager's REST version
     */
    private void informRestVersiontoUser(String restVersion) {
        outStream.println("You are using REST version - " + restVersion + " (If you want to change this, go to " +
                "<MICROGW_HOME>/conf/toolkit-config.toml)");
    }

}


