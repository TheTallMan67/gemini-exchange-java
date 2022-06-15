package com.github.thetallman67.gemini.v1.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.thetallman67.gemini.v1.request.SymbolRequest;
import com.github.thetallman67.gemini.v1.response.SymbolDetails;
import com.github.thetallman67.gemini.v1.response.Ticker;
import com.github.thetallman67.gemini.v1.response.Volume;
import com.github.thetallman67.gemini.v1.response.deserializer.VolumeDeserializer;
import kong.unirest.Unirest;
import kong.unirest.jackson.JacksonObjectMapper;

import java.util.Set;

public class PublicClient extends GeminiClient {

    public static class Builder extends GeminiClient.Builder<Builder> {

        @Override
        public PublicClient build() {
            return new PublicClient(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private PublicClient(Builder builder) {
        super(builder);
    }

    public Set<String> getSymbols() {
        return Unirest.get("/symbols")
                .asObject(Set.class)
                .getBody();
    }

    public SymbolDetails getSymbolDetails(SymbolRequest symbolRequest) {
        return Unirest.get("/symbols/details/{symbol}")
                .routeParam("symbol", symbolRequest.getSymbol())
                .asObject(SymbolDetails.class)
                .getBody();
    }

    public Ticker getTicker(SymbolRequest symbolRequest) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Volume.class, new VolumeDeserializer(symbolRequest.getSymbol()));

        ObjectMapper objectMapper = getObjectMapper();
        objectMapper.registerModule(module);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        return Unirest.get("/pubticker/{symbol}")
                .routeParam("symbol", symbolRequest.getSymbol())
                .withObjectMapper(new JacksonObjectMapper(objectMapper))
                .asObject(Ticker.class)
                .getBody();
    }
}