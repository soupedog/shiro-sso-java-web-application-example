package io.github.soupedog.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Xavier
 * @date 2021/9/22
 */
public class LongTimeStampSerializer extends JsonSerializer<Long> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    @Override
    public void serialize(Long target, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (target == null) {
            jsonGenerator.writeString((String) null);
        } else {
            ZonedDateTime time = ZonedDateTime.ofInstant(Instant.ofEpochMilli(target), ZONE_ID);
            jsonGenerator.writeString(time.format(FORMATTER));
        }
    }
}
