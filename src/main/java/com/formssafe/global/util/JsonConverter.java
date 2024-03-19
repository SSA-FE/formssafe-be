package com.formssafe.global.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class JsonConverter {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonConverter() {
    }

    public static <T> String toJson(T entity) {
        try {
            return mapper.writeValueAsString(entity);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return null;
    }

    public static <T> List<T> toList(String json, Class<T> type) {
        try {
            return mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, type));
        } catch (JsonProcessingException e) {
            log.error("JSON reading error", e);
        }

        return new ArrayList<>();
    }
}