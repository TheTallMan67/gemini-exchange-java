package com.github.thetallman67.gemini.v1.request;

public class SymbolRequest {

    private final String symbol;

    public String getSymbol() {
        return symbol;
    }

    public SymbolRequest(SymbolRequestBuilder builder) {
        this.symbol = builder.symbol;
    }

    public static BuildSymbol builder() {
        return new SymbolRequestBuilder();
    }

    public interface BuildSymbol {
        Build withSymbol(String symbol);
    }

    public interface Build {
        SymbolRequest build();
    }

    private static class SymbolRequestBuilder implements BuildSymbol, Build {
        private String symbol;

        @Override
        public Build withSymbol(String symbol) {
            this.symbol = symbol;
            return this;
        }

        @Override
        public SymbolRequest build() {
            return new SymbolRequest(this);
        }
    }
}
