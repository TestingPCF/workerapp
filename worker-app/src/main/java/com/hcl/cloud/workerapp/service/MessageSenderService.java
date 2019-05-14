package com.hcl.cloud.workerapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
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
	@Scheduled(fixedDelay = 60000, initialDelay = 15000)
	public void sendMessage() {
	    CustomMessageBean customBean1=new CustomMessageBean("PRODUCT1", 10+random.nextInt(90));
        CustomMessageBean customBean2=new CustomMessageBean("PRODUCT2", 10+random.nextInt(90));
        CustomMessageBean customBean3=new CustomMessageBean("PRODUCT3", 10+random.nextInt(90));
        CustomMessageBean customBean4=new CustomMessageBean("PRODUCT4", 10+random.nextInt(90));
        CustomMessageBean customBean5=new CustomMessageBean("PRODUCT5", 10+random.nextInt(90));
        CustomMessageBean customBean6=new CustomMessageBean("PRODUCT6", 10+random.nextInt(90));
        List<CustomMessageBean> productList=new ArrayList<CustomMessageBean>();
        productList.add(customBean1);
        productList.add(customBean2);
        productList.add(customBean3);
        productList.add(customBean4);
        productList.add(customBean5);
        productList.add(customBean6);
		for(CustomMessageBean beanList : productList) {
		    sendNow(beanList);
		}
		
	} 
	
	public void sendNow(CustomMessageBean customMessageBean) {
	    log.info(successMessage+" : "+customMessageBean.toString());
	    rabbitTemplate.convertAndSend(ConfigConstant.EXCHANGE_NAME,env.getProperty(ConfigConstant.ROUTING_KEY),customMessageBean);
	}
}
