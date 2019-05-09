package com.hcl.cloud.workerapp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hcl.cloud.workerapp.config.RabbitmqConfig;

import dto.CustomMessageBean;


@Service
public class MessageSenderService {
	@Autowired
	private RabbitTemplate rabbitTemplate;
	Random random = new Random();
	
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
	    rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME,RabbitmqConfig.ROUTING_KEY,customMessageBean);
	}
}
