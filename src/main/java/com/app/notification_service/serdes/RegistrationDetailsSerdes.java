package com.app.notification_service.serdes;

import com.app.notification_service.model.RegistrationDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RegistrationDetailsSerdes implements Serde<RegistrationDetails> {

    private final RegistrationDetailsSerializer registrationDetailsSerializer;
    private final RegistrationDetailsDeSerializer registrationDetailsDeSerializer;

    public RegistrationDetailsSerdes() {
        ObjectMapper objectMapper = new ObjectMapper();
        this.registrationDetailsSerializer = new RegistrationDetailsSerializer(objectMapper);
        this.registrationDetailsDeSerializer = new RegistrationDetailsDeSerializer(objectMapper);
    }

    @Override
    public Serializer<RegistrationDetails> serializer() {
        return registrationDetailsSerializer;
    }

    @Override
    public Deserializer<RegistrationDetails> deserializer() {
        return registrationDetailsDeSerializer;
    }


    public static class RegistrationDetailsSerializer implements Serializer<RegistrationDetails>{
        private final ObjectMapper objectMapper;

        public RegistrationDetailsSerializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }


        @Override
        public byte[] serialize(String topic, RegistrationDetails registrationDetails) {
            try {
                return objectMapper.writeValueAsBytes(registrationDetails);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class RegistrationDetailsDeSerializer implements Deserializer<RegistrationDetails>{
        private final ObjectMapper objectMapper;

        public RegistrationDetailsDeSerializer(ObjectMapper objectMapper) {
            this.objectMapper = objectMapper;
        }

        @Override
        public RegistrationDetails deserialize(String topic, byte[] registrationDetailsBytes) {
            try {
                return objectMapper.readValue(registrationDetailsBytes, RegistrationDetails.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
