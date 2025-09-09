package org.precize.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.precize.model.Order;

import java.util.Map;

public class CommonUtil {
    public String  prettyPrintHistory(Map<String, Map<String, Order>> orderStore) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT);
        String prettyJson = mapper.writeValueAsString(orderStore);
        return prettyJson;
    }
}
