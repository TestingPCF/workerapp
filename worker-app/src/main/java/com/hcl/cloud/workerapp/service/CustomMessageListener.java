package com.hcl.cloud.workerapp.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.hcl.cloud.workerapp.constant.ConfigConstant;

import dto.CustomMessageBean;

@Service
public class CustomMessageListener {

    private static final Logger log = LoggerFactory.getLogger(CustomMessageListener.class);

    @RabbitListener(queues = ConfigConstant.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final Message message) {
        log.info("Received message as generic: {}", message.toString());
    }

    @RabbitListener(queues = ConfigConstant.QUEUE_SPECIFIC_NAME)
    public void receiveMessage(final CustomMessageBean customMessage) {
        log.info("Received message as specific class: {}", customMessage.toString());
    }
}
