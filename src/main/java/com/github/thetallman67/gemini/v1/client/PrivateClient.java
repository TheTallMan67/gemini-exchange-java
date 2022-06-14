package com.github.thetallman67.gemini.v1.client;

public class PrivateClient extends GeminiClient {

    public static class Builder extends GeminiClient.Builder<Builder> implements BuildAPIKey, BuildAPISecret  {

        private String apiKey;
        private String apiSecret;

        @Override public PrivateClient build() {
            return new PrivateClient(this);
        }

        @Override protected Builder self() {
            return this;
        }

        @Override
        public BuildAPISecret withAPIKey(String apiKey) {
            this.apiKey = apiKey;
            return self();
        }

        @Override
        public Builder withAPISecret(String apiSecret) {
            this.apiSecret = apiSecret;
            return self();
        }
    }

    public interface BuildAPIKey {
        BuildAPISecret withAPIKey(String apiKey);
    }

    public interface BuildAPISecret {
        PrivateClient.Builder withAPISecret(String apiSecret);
    }

    private PrivateClient(Builder builder) {
        super(builder);
    }
}