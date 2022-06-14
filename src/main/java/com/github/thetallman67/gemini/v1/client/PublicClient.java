package com.github.thetallman67.gemini.v1.client;

import com.github.thetallman67.gemini.v1.request.SymbolRequest;
import com.github.thetallman67.gemini.v1.response.SymbolDetails;
import kong.unirest.Unirest;

import java.util.Set;

public class PublicClient extends GeminiClient {

    public static class Builder extends GeminiClient.Builder<Builder> {

        @Override public PublicClient build() {
            return new PublicClient(this);
        }

        @Override protected Builder self() {
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
}