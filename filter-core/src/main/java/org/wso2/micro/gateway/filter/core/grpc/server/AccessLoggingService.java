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

package org.wso2.micro.gateway.filter.core.grpc.server;

import io.envoyproxy.envoy.service.accesslog.v3.AccessLogServiceGrpc;
import io.envoyproxy.envoy.service.accesslog.v3.StreamAccessLogsMessage;
import io.envoyproxy.envoy.service.accesslog.v3.StreamAccessLogsResponse;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.channel.EventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.nio.NioEventLoopGroup;
import io.grpc.netty.shaded.io.netty.channel.socket.nio.NioServerSocketChannel;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.filter.core.analytics.AnalyticsFilter;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

//import io.envoyproxy.envoy.config.core.v3.HeaderValue;
//import io.envoyproxy.envoy.config.core.v3.HeaderValueOption;
//import io.envoyproxy.envoy.service.auth.v3.AuthorizationGrpc;
//import io.envoyproxy.envoy.service.auth.v3.CheckRequest;
//import io.envoyproxy.envoy.service.auth.v3.CheckResponse;
//import io.envoyproxy.envoy.service.auth.v3.DeniedHttpResponse;
//import io.envoyproxy.envoy.service.auth.v3.OkHttpResponse;
//import io.envoyproxy.envoy.type.v3.HttpStatus;

/**
 * This is the gRPC server written to match with the envoy ext-authz filter proto file. Envoy proxy call this service.
 * This is the entry point to the filter chain process for a request.
 */
public class AccessLoggingService extends AccessLogServiceGrpc.AccessLogServiceImplBase {

    private RequestHandler requestHandler = new RequestHandler();
    private static final Logger logger = LogManager.getLogger(AccessLoggingService.class);
    AnalyticsFilter analyticsFilter;
    AnalyticsService analyticsService = new AnalyticsService();

    public AccessLoggingService(AnalyticsFilter analyticsFilter) {
        this.analyticsFilter = analyticsFilter;
        startAccessLoggingServer();
    }

    @Override
    public StreamObserver<StreamAccessLogsMessage> streamAccessLogs
            (StreamObserver<StreamAccessLogsResponse> responseObserver) {
        return new StreamObserver<StreamAccessLogsMessage>() {
            @Override
            public void onNext(StreamAccessLogsMessage message) {
                logger.info("Received msg" + message.toString());
                analyticsService.send();
                analyticsFilter.handleMsg(message);
            }

            @Override
            public void onError(Throwable throwable) {
                logger.info("Error in receiving access log from envoy" + throwable.getMessage());
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                logger.info("grpc logger completed");
                responseObserver.onNext(StreamAccessLogsResponse.newBuilder().build());
                responseObserver.onCompleted();
            }
        };
    }

    private boolean startAccessLoggingServer() {
        final EventLoopGroup bossGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
        final EventLoopGroup workerGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors() * 2);
        int blockingQueueLength = 1000;
        final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingQueue(blockingQueueLength);
        final Executor executor = new MGWThreadPoolExecutor(400, 500, 30, TimeUnit.SECONDS, blockingQueue);

        Server analyticsServer = NettyServerBuilder.forPort(18090).maxConcurrentCallsPerConnection(20)
                .keepAliveTime(60, TimeUnit.SECONDS).maxInboundMessageSize(1000000000).bossEventLoopGroup(bossGroup)
                .workerEventLoopGroup(workerGroup).addService(this)
                .channelType(NioServerSocketChannel.class).executor(executor).build();
        // Start the server
        try {
            analyticsServer.start();
        } catch (IOException e) {
            logger.error("Error while starting the grpc access logging server");
        }
        logger.info("Access loggers Sever started Listening in port : " + 18090);
        return true;
    }
}
