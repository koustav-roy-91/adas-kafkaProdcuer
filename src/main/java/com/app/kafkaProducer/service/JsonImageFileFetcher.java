package com.app.kafkaProducer.service;

import com.app.kafkaProducer.kafka.KafkaProducerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * author koustavroy
 *
 *  This class reads a group of .jpg image files from a specific location.
 */
@Component
public class JsonImageFileFetcher {

    private static final Logger logger = LoggerFactory.getLogger(JsonImageFileFetcher.class);
    @Autowired
    KafkaProducerService kafkaProducerService;
    @Autowired
    DatasetEncoder datasetEncoder;

    @Scheduled(fixedRate = 2000)
    public void getDatasetJSONFromFolder() {
        String dir = "/Users/koustavnaharoy/CustomerProjects/kafkaProducer/Dataset/Image";

        try {
            List<Path> paths = Files.walk(Paths.get(dir), 1) //by mentioning max depth as 1 it will only traverse immediate level
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".jpg")) // fetch only the files which are ending with .JSON
                    .collect(Collectors.toList());
            //iterate all the paths and fetch data from corresponding file
            for (Path path : paths) {
                datasetEncoder.encodeAndSendData(path,"image");
            }
        } catch (Exception e) {
            logger.error("No image exist");
        }
    }
}
