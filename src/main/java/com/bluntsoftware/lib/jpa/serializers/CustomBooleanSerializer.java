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
 * Time: 4:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomBooleanSerializer  extends JsonSerializer<Boolean> {

    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider arg2) throws IOException, JsonProcessingException {
        if (value != null) {
            gen.writeString(value?"true":"false");
        }
    }
}