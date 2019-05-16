package com.hcl.cloud.workerapp.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.hcl.cloud.workerapp.constant.ConfigConstant;

import dto.CustomMessageBean;

@Service
public class MessageSenderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    Random random = new Random();
    @Autowired
    private Environment env;

    static Logger log = LoggerFactory.getLogger(MessageSenderService.class);
    @Value("${worker-app.success.message}")
    public String successMessage;

     public void sendNow(CustomMessageBean customMessageBean) {
        log.info(successMessage + " : " + customMessageBean.toString());
        rabbitTemplate.convertAndSend(ConfigConstant.EXCHANGE_NAME, env.getProperty(ConfigConstant.ROUTING_KEY),
                customMessageBean);
    }
}
