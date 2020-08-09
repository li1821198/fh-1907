package com.fh.shop.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalJackson extends JsonSerializer<BigDecimal> {

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if(null==bigDecimal){
            gen.writeString("");
        }else {
            gen.writeString(bigDecimal.toString());
        }
    }
}
