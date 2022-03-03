package com.ead.course.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfig {

    @Autowired
    CachingConnectionFactory cachingConnectionFactory;

    @Bean
    public RabbitTemplate rabbitTemplate() { // cria ponto de injecao dentro do publisher para enviar as mensagens/eventos
        RabbitTemplate template = new RabbitTemplate(cachingConnectionFactory); // conecta instancia com o Rabbitmq usando o endereço definido no application-prod.yaml
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() { // serialização/deserialização de envio e recebimento de msg para o broker
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // registro de JavaTimeModule para tratamento de data na conversão
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
