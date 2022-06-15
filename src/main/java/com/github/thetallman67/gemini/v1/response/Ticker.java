package com.github.thetallman67.gemini.v1.response;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Ticker extends GeminiResponse {
    private double bid;
    private double ask;
    private double last;
    private Volume volume;
}
