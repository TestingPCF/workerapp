/**
 * 
 */

package com.hcl.cloud.workerapp.service;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import dto.CustomMessageBean;

/**
 * @author BrijendraK
 *
 */
@Service
public class AmazonClient {
   
    MessageSenderService messageSenderService;
    CustomMessageListener customMessageListener;
    @Autowired
    public AmazonClient(MessageSenderService messageSenderService,CustomMessageListener customMessageListener){
    	this.messageSenderService=messageSenderService;
    	this.customMessageListener=customMessageListener;
    }
    public AmazonS3 s3client;

    static Logger log = LoggerFactory.getLogger(AmazonClient.class);

    @Value("${amazonProperties.bucketName}")
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    
    //@Value("${amazonProperties.key_name}")
    //private String key_name;

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.US_EAST_2).build();
        // getFeedFromS3();
    }

    @Scheduled(fixedDelay = 600000, initialDelay = 60000)
    public void getFeedFromS3() {
       S3Object s3object = s3client.getObject(bucketName, "inventoryFeedS3.json");
    	//S3Object s3object = s3client.getObject(bucketName, key_name);
    	
        S3ObjectInputStream inputStream = s3object.getObjectContent();

        try {
            // JSON parser object to parse read file
            byte[] byteArray = IOUtils.toByteArray(s3object.getObjectContent());
            JSONParser parser = new JSONParser();
            JSONArray objArray = (JSONArray) parser.parse(new String(byteArray));
            for (Object o : objArray) {
                JSONObject feedObj = (JSONObject) o;

                String skuCode = (String) feedObj.get("skucode");
                long quantity = (Long) feedObj.get("quantity");
                CustomMessageBean customMessageBean = new CustomMessageBean(skuCode, quantity);
                messageSenderService.sendNow(customMessageBean);
                customMessageListener.receiveMessage(customMessageBean);
            }
        } catch (Exception e) {
            log.info("Error occured while receiving feed from S3 :", e.getLocalizedMessage());
            inputStream.abort();
        }
        log.info("Successfully received and sent feed from S3 to inventory");
    }
}
