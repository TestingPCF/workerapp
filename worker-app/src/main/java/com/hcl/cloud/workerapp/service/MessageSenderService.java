package com.hcl.cloud.workerapp.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.cloud.workerapp.constant.ConfigConstant;

import dto.CustomMessageBean;

@Service
public class MessageSenderService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    Random random = new Random();

//    private AmazonClient amazonClient;
//    
//    @Autowired
//    MessageSenderService(AmazonClient amazonClient){
//        this.amazonClient = amazonClient;
//    }
    @Autowired
    private Environment env;

    static Logger log = LoggerFactory.getLogger(MessageSenderService.class);
    @Value("${worker-app.success.message}")
    public String successMessage;

   
//    @Scheduled(fixedDelay = 600000, initialDelay = 30000)
//    public void sendMessage() {
//        
//           amazonClient.getFeedFromS3();
//           
//          //JSON parser object to parse read file
//            JSONParser parser = new JSONParser();
//            try {
//                
//                JSONArray  objArray = (JSONArray) parser.parse(new FileReader(
//                        "src/main/resources/inventoryfeed.json"));
//     
//                for (Object o : objArray)
//                {
//                   // CustomMessageBean customMessageBean=new CustomMessageBean();
//                  JSONObject feedObj = (JSONObject) o;
//                  
//                  String skuCode = (String) feedObj.get("skucode");                 
//                 long quantity = (Long) feedObj.get("quantity");
//                  CustomMessageBean customMessageBean=new CustomMessageBean(skuCode,quantity);
//                  sendNow(customMessageBean);
//                }
//     
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//    }

    public void sendNow(CustomMessageBean customMessageBean) {
        log.info(successMessage + " : " + customMessageBean.toString());
        rabbitTemplate.convertAndSend(ConfigConstant.EXCHANGE_NAME, env.getProperty(ConfigConstant.ROUTING_KEY),
                customMessageBean);
    }
}
