package register.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import register.exception.JsonProcessException;

@Slf4j
public class JsonUtil {

    private static ObjectMapper mapper;
    private static ObjectMapper iso8601DateFormatMapper;

    static {
        mapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        iso8601DateFormatMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static String getStringJson(Object obj) {
        if (obj == null)
            return null;
        try {
            mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("unsuccessful parsing json", e);
            throw new JsonProcessException(e);
        }
    }

}
