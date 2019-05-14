package com.hcl.cloud.workerapp.config;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcl.cloud.workerapp.constant.ConfigConstant;

@Configuration
public class RabbitmqConfig {

    @Bean
    public TopicExchange mqExchange() {
        return new TopicExchange(ConfigConstant.EXCHANGE_NAME);
    }

    @Bean
    public Queue appQueueSpecific() {
        return new Queue(ConfigConstant.QUEUE_SPECIFIC_NAME);
    }

    @Bean
    public Binding declareBindingSpecific() {
        return BindingBuilder.bind(appQueueSpecific()).to(mqExchange()).with(ConfigConstant.ROUTING_KEY);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());

        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter(ObjectMapper objectMapper) {
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("*");
        classMapper.setDefaultType(Map.class);
        Jackson2JsonMessageConverter converter = new ImplicitJsonMessageConverter(objectMapper);
        converter.setClassMapper(classMapper);
        return converter;
    }

    public static class ImplicitJsonMessageConverter extends Jackson2JsonMessageConverter {
        public ImplicitJsonMessageConverter(ObjectMapper jsonObjectMapper) {
            super(jsonObjectMapper, "*");
        }

        @Override
        public Object fromMessage(Message message) throws MessageConversionException {
            message.getMessageProperties().setContentType("application/json");
            return super.fromMessage(message);
        }
    }
}
