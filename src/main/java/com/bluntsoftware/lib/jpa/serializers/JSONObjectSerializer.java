package com.bluntsoftware.lib.jpa.serializers;

import com.fasterxml.jackson.databind.*;

import java.io.IOException;

/**
 *
 */
public class JSONObjectSerializer<T> {

    public String toString(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
           return  mapper.writeValueAsString(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
    public T fromString(String json) throws IOException {
         ObjectMapper mapper = new ObjectMapper();

         mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
         //mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
         ObjectReader reader = mapper.reader(this.getClass());
         return reader.readValue(json);
    }
}
