package com.bluntsoftware.lib.jpa.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/26/15
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomTimeSerializer extends JsonSerializer<Time> {
    @Override
    public void serialize(Time value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        if (value != null) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = formatter.format(value);
            gen.writeString(formattedDate);

        }
    }
}
