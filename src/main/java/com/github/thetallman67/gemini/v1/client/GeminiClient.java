package com.github.thetallman67.gemini.v1.client;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import kong.unirest.*;
import kong.unirest.jackson.JacksonObjectMapper;

public abstract class GeminiClient {

    final boolean sandbox;

    public boolean isSandbox() {
        return sandbox;
    }

    abstract static class Builder<T extends Builder<T>> implements BuildGeminiClient<T> {
        private boolean sandbox;

        @Override
        public T useSandbox() {
            this.sandbox = true;
            return self();
        }

        abstract GeminiClient build();

        protected abstract T self();
    }

    public interface BuildGeminiClient<T> {
        T useSandbox();
    }

    protected GeminiClient(Builder<?> builder) {
        sandbox = builder.sandbox;

        ObjectMapper objectMapper = new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Unirest.config()
                .defaultBaseUrl("https://api." + (isSandbox() ? "sandbox." : "") + "gemini.com/v1")
                .setObjectMapper(new JacksonObjectMapper(objectMapper))
                .interceptor(new Interceptor() {
                    @Override
                    public void onResponse(HttpResponse<?> response, HttpRequestSummary request, Config config) {
                        Interceptor.super.onResponse(response, request, config);
                        response.ifFailure(e -> {
                            System.err.println(response.getStatus() + " " + response.getStatusText() + ": (" + request.getHttpMethod() + ")" + request.getUrl());
                            if (e.getParsingError().isPresent()) {
                                UnirestParsingException ex = e.getParsingError().get();
                                System.err.println(ex.getOriginalBody());
                                ex.printStackTrace();
                                throw e.getParsingError().get();
                            }
                            throw new UnirestException(response.getStatusText());
                        });
                    }
                });
    }
}