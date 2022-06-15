package com.github.thetallman67.gemini.v1.response;

import lombok.Getter;

@Getter
public abstract class GeminiResponse {
    private String result;
    private String reason;
    private String message;
}
