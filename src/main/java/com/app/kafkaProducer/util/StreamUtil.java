package com.app.kafkaProducer.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * author koustavroy
 */

public class StreamUtil {

    // methods to covert .csv to bytes array
    public static List<Map<?, ?>> readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        try (MappingIterator<Map<?, ?>> mappingIterator = csvMapper.readerFor(Map.class).with(bootstrap).readValues(file)) {
            return mappingIterator.readAll();
        }
    }

    public static void writeAsJson(List<Map<?, ?>> data, File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(file, data);
    }

    public static String createKafkaMessage(String encodedMessage, String type, String fileName) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> kafkaMessage = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        kafkaMessage.put("uuid", uuid);
        kafkaMessage.put("retry_count", 0);
        kafkaMessage.put("message", encodedMessage);
        kafkaMessage.put("type", type);
        kafkaMessage.put("file_name", fileName);
        return mapper.writeValueAsString(kafkaMessage);
    }

    public static String encodeDataToBase64(File file) throws IOException {
        byte[] bytes = FileUtils.readFileToByteArray(file);
        String encoded = Base64.getEncoder().encodeToString(bytes);
        return encoded;
    }
}
