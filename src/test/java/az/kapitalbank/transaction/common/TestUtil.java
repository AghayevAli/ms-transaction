package az.kapitalbank.transaction.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class TestUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    private static final ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

    public static String json(Object object) throws Exception {
        return objectWriter.writeValueAsString(object);
    }
} 