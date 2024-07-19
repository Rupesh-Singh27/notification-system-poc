package com.app.notification_service.processor;

import com.app.notification_service.model.RegistrationDetails;
import com.app.notification_service.serdes.RegistrationDetailsSerdes;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Properties;

@Configuration
public class KTableProvider {

    private KafkaStreams streams;
    @Bean
    public KafkaStreams createKTable(){

        /*Step 1: Initializing Properties*/
        Properties props = new Properties();

        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "registration-stream");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, RegistrationDetailsSerdes.class);

        /*Step 2: StreamsBuilder Object*/
        StreamsBuilder builder = new StreamsBuilder();

        /*Step 3: Create KStream or KTable*/
        KStream<String, RegistrationDetails> kStream = builder.stream("register_topic", Consumed.with(Serdes.String(), new RegistrationDetailsSerdes()));

        KTable<String, RegistrationDetails> registrationDetailsKTable = kStream.toTable(
                Materialized.<String, RegistrationDetails, KeyValueStore<Bytes, byte[]>>as("registration-details-state-store")
                        .withKeySerde(Serdes.String())
                        .withValueSerde(new RegistrationDetailsSerdes())
        );

        registrationDetailsKTable.toStream().peek((key, value) -> System.out.println("Key: " + key + " value: " + value));

        /*Step 4: Create KafkaStreams object using StreamsBuilder object*/
        streams = new KafkaStreams(builder.build(), props);

        /*Step 5: Start the streams*/
        streams.start();

        return streams;
    }

    public void close(){
        if(streams != null) Runtime.getRuntime().addShutdownHook(new Thread(()->streams.close()));
    }
}
