package com.reactionchallenge.dto;

import java.time.Instant;

public class NetworkTestResponse {

    private String status;
    private String region;
    private String server;
    private Instant timestamp;

    public NetworkTestResponse() {}

    public NetworkTestResponse(String status, String region, String server, Instant timestamp) {
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

    public static NetworkTestResponseBuilder builder() {
        return new NetworkTestResponseBuilder();
    }

    public static class NetworkTestResponseBuilder {
        private String status;
        private String region;
        private String server;
        private Instant timestamp;

        public NetworkTestResponseBuilder status(String status) { this.status = status; return this; }
        public NetworkTestResponseBuilder region(String region) { this.region = region; return this; }
        public NetworkTestResponseBuilder server(String server) { this.server = server; return this; }
        public NetworkTestResponseBuilder timestamp(Instant timestamp) { this.timestamp = timestamp; return this; }

        public NetworkTestResponse build() {
            return new NetworkTestResponse(status, region, server, timestamp);
        }
    }
}
