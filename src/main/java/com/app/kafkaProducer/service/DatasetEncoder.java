package com.app.kafkaProducer.service;

import com.app.kafkaProducer.kafka.KafkaProducerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import static com.app.kafkaProducer.util.StreamUtil.createKafkaMessage;
import static com.app.kafkaProducer.util.StreamUtil.encodeDataToBase64;

/**
 * author koustavroy
 */

@Component
public class DatasetEncoder {

    @Value("${kafka.producer.topic:adas-main}")
    protected String topic;
    @Autowired
    KafkaProducerService kafkaProducerService;

    public void encodeAndSendData(Path path, String type) {
        String outputJson = null;
        try {
            File file = new File(path.toString());
            // Encode the json file to base64
            String encodedMessage = encodeDataToBase64(file);

            //sending the custom created kafka message to kafka producer service
            outputJson = createKafkaMessage(encodedMessage, type, file.getName());
            kafkaProducerService.sendMessage(outputJson, topic);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
