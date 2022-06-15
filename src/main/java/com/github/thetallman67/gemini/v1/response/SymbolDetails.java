package com.github.thetallman67.gemini.v1.response;

import com.github.thetallman67.gemini.v1.model.SymbolStatus;
import lombok.Getter;

@Getter
public class SymbolDetails extends GeminiResponse {
    private String symbol;
    private String baseCurrency;
    private String quoteCurrency;
    private double tickSize;
    private double quoteIncrement;
    private String minOrderSize;
    private SymbolStatus status;
    private boolean wrapEnabled;
}
