package org.wso2.micro.gateway.filter.core.analytics;

import org.wso2.micro.gateway.filter.core.analytics.dto.AlertTypeDTO;
import org.wso2.micro.gateway.filter.core.analytics.dto.FaultPublisherDTO;
import org.wso2.micro.gateway.filter.core.analytics.dto.RequestResponseStreamDTO;
import org.wso2.micro.gateway.filter.core.analytics.dto.ThrottlePublisherDTO;
import org.wso2.micro.gateway.filter.core.exception.MGWException;

public class UsageDataPublisherImpl implements UsageDataPublisher {
    @Override
    public void init() {

    }

    @Override
    public void publishEvent(FaultPublisherDTO faultPublisherDTO) {

    }

    @Override
    public void publishEvent(ThrottlePublisherDTO throttlePublisherDTO) {

    }

    @Override
    public void publishEvent(AlertTypeDTO alertTypeDTO) throws MGWException {

    }

    @Override
    public void publishEvent(RequestResponseStreamDTO requestStream) {

    }
}
