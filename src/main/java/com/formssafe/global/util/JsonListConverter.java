package com.formssafe.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonListConverter {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonListConverter() {
    }

    public static String convertToDatabaseColumn(List<String> entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return null;
    }

    public static List<String> convertToEntityAttribute(String json) {

        try {
            return mapper.readValue(json, new TypeReference<>() {
            });
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return new ArrayList<>();
    }
}