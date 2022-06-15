package com.github.thetallman67.gemini.v1.response.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.thetallman67.gemini.v1.response.Volume;
import com.sun.istack.internal.NotNull;

import java.io.IOException;
import java.util.Iterator;

public class VolumeDeserializer extends JsonDeserializer<Volume> {

    private static final String TIMESTAMP = "timestamp";

    private String symbol;

    public VolumeDeserializer(@NotNull String symbol) {
        this.symbol = symbol;
    }

    @Override
    public Volume deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        Iterator<String> fieldNameIter = node.fieldNames();

        long timestamp = 0;
        String priceCurrencySymbol= null;
        String priceVolume = null;
        String quantityCurrencySymbol = null;
        String quantityVolume = null;

        while (fieldNameIter.hasNext()) {
            String fieldName = fieldNameIter.next();
            if (TIMESTAMP.equals(fieldName)) {
                timestamp = node.get(TIMESTAMP).longValue();
            } else if (symbol.startsWith(fieldName)) {
                priceCurrencySymbol = fieldName;
                priceVolume = node.get(fieldName).textValue();
            } else if (symbol.endsWith(fieldName)) {
                quantityCurrencySymbol = fieldName;
                quantityVolume = node.get(fieldName).textValue();
            }
        }
        return new Volume(timestamp, priceCurrencySymbol, priceVolume, quantityCurrencySymbol, quantityVolume);
    }
}
