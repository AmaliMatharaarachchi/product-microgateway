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

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wso2.micro.gateway.filter.core.grpc.server.analytics.AnalyticsSendServiceGrpc;
import org.wso2.micro.gateway.filter.core.grpc.server.analytics.AnalyticsStreamMessage;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AnalyticsService extends AnalyticsSendServiceGrpc.AnalyticsSendServiceImplBase {
    private static final Logger logger = LogManager.getLogger(AnalyticsService.class);
    StreamObserver<AnalyticsStreamMessage> requestObserver;
    final CountDownLatch finishLatch = new CountDownLatch(1);

    public void init() {
        final ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8090")
                .usePlaintext()
                .build();
        AnalyticsSendServiceGrpc.AnalyticsSendServiceStub asyncStub = AnalyticsSendServiceGrpc.newStub(channel);

        requestObserver = asyncStub.sendAnalytics(new StreamObserver<Empty>() {
            @Override
            public void onNext(Empty empty) {

            }

            @Override
            public void onError(Throwable throwable) {
                logger.warn("RecordRoute Failed: {0}", throwable.getMessage());
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                finishLatch.countDown();
            }
        });
    }

    public void send() {
        AnalyticsStreamMessage analyticsStreamMessage =
                AnalyticsStreamMessage.newBuilder()
                        .setApiName("amaliAPI")
                        .build();

        requestObserver.onNext(analyticsStreamMessage);
        requestObserver.onNext(analyticsStreamMessage);
        requestObserver.onNext(analyticsStreamMessage);
        // Send numPoints points randomly selected from the features list
        if (finishLatch.getCount() == 0) {
            // RPC completed or errored before we finished sending.
            // Sending further requests won't error, but they will just be thrown away.
            return;
        }

        // Mark the end of requests
        requestObserver.onCompleted();

        // Receiving happens asynchronously
        try {
            if (!finishLatch.await(1, TimeUnit.MINUTES)) {
                logger.warn("recordRoute can not finish within 1 minutes");
            }
        } catch (InterruptedException e) {
            logger.warn("Error while streaming analytics data. " + e.getMessage());
        }
    }

}
