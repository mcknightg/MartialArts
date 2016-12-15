package com.bluntsoftware.lib.jpa.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/26/15
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomSecuredSerializer extends JsonSerializer<String> {
    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        StringBuffer buffer = new StringBuffer();
        if (value != null && value.length() > 4) {
            for (int i = 0; i < value.length(); i++) {
                buffer.append((i >= value.length() - 4) ? value.charAt(i) : '*');
            }
            gen.writeString(buffer.toString());
        } else {
            gen.writeString("");
        }
    }
}