package com.app.registration_service.config;

import com.app.registration_service.model.RegistrationDetails;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class ProducerConfig {

    @Value("${spring.kafka.producer.bootstrap-server}")
    private String bootstrapServer;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;


    @Bean
    public KafkaProducer<String, RegistrationDetails> kafkaProducer(){
        Properties props = new Properties();

        System.out.println("Value Serializer: " + valueSerializer);
        props.put("bootstrap.servers", bootstrapServer);
        props.put("key.serializer", keySerializer);
        props.put("value.serializer", valueSerializer);

        return new KafkaProducer<>(props);
    }


}
