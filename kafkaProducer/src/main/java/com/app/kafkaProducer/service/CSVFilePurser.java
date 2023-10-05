package com.app.kafkaProducer.service;


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
 * This class reads a group of .csv files from a specific location.
 */
@Component
public class CSVFilePurser {

    @Autowired
    DatasetEncoder datasetEncoder;

    private static final Logger logger = LoggerFactory.getLogger(CSVFilePurser.class);

    @Scheduled(fixedRate = 2000)
    public void getAudioDatasetFromFolder() {
        String dir = "/Users/koustavnaharoy/CustomerProjects/kafkaProducer/Dataset/csv";
//        File output = new File("/Users/koustavnaharoy/CustomerProjects/kafkaProducer/Dataset/csv/data.json");
        try {
            List<Path> paths = Files.walk(Paths.get(dir), 1) //by mentioning max depth as 1 it will only traverse immediate level
                    .filter(Files::isRegularFile)
                    .filter(path -> path.getFileName().toString().endsWith(".csv")) // fetch only the files which are ending with .JSON
                    .collect(Collectors.toList());
            //iterate all the paths and fetch data from corresponding file
            for (Path path : paths) {
                datasetEncoder.encodeAndSendData(path,"csv");
            }

        } catch (Exception e) {
            logger.error("No csv file exist");
        }
    }
}