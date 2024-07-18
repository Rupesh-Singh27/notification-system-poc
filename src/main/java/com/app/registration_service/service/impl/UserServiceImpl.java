package com.app.registration_service.service.impl;

import com.app.registration_service.model.RegistrationDetails;
import com.app.registration_service.service.UserService;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class UserServiceImpl implements UserService {

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    private final KafkaProducer<String, RegistrationDetails> kafkaProducer;

    public UserServiceImpl(KafkaProducer<String, RegistrationDetails> kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }


    @Override
    public RegistrationDetails registerUser(RegistrationDetails registrationDetails) {
        ProducerRecord<String, RegistrationDetails> record =new ProducerRecord<>(topicName, registrationDetails.getEmailId(), registrationDetails);
        try {
            RecordMetadata recordMetadata = kafkaProducer.send(record).get();
            System.out.println("topic: " + recordMetadata.topic());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(kafkaProducer::close));
        return registrationDetails;
    }
}
