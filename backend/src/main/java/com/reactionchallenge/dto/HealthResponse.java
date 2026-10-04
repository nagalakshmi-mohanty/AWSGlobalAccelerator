package com.reactionchallenge.dto;

import java.time.Instant;

public class HealthResponse {

    private String status;
    private String region;
    private String server;
    private Instant timestamp;

    public HealthResponse() {}

    public HealthResponse(String status, String region, String server, Instant timestamp) {
        this.status = status;
        this.region = region;
        this.server = server;
        this.timestamp = timestamp;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public static HealthResponseBuilder builder() {
        return new HealthResponseBuilder();
    }

    public static class HealthResponseBuilder {
        private String status;
        private String region;
        private String server;
        private Instant timestamp;

        public HealthResponseBuilder status(String status) { this.status = status; return this; }
        public HealthResponseBuilder region(String region) { this.region = region; return this; }
        public HealthResponseBuilder server(String server) { this.server = server; return this; }
        public HealthResponseBuilder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }

        public HealthResponse build() {
            return new HealthResponse(status, region, server, timestamp);
        }
    }
}
