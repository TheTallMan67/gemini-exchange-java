package com.github.thetallman67.gemini;

import com.github.thetallman67.gemini.v1.client.PrivateClient;
import com.github.thetallman67.gemini.v1.client.PublicClient;

public class Gemini {

    public static PublicClient.Builder publicClient() {
        return new PublicClient.Builder();
    }

    public static PrivateClient.BuildAPIKey privateClient() {
        return new PrivateClient.Builder();
    }

}
