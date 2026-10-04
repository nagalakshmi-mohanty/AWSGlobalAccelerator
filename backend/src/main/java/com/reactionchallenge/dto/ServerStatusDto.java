package com.reactionchallenge.dto;

public class ServerStatusDto {

    private String region;
    private String server;
    private String status;
    private Long latency;

    public ServerStatusDto() {}

    public ServerStatusDto(String region, String server, String status, Long latency) {
        this.region = region;
        this.server = server;
        this.status = status;
        this.latency = latency;
    }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Long getLatency() { return latency; }
    public void setLatency(Long latency) { this.latency = latency; }

    public static ServerStatusDtoBuilder builder() {
        return new ServerStatusDtoBuilder();
    }

    public static class ServerStatusDtoBuilder {
        private String region;
        private String server;
        private String status;
        private Long latency;

        public ServerStatusDtoBuilder region(String region) { this.region = region; return this; }
        public ServerStatusDtoBuilder server(String server) { this.server = server; return this; }
        public ServerStatusDtoBuilder status(String status) { this.status = status; return this; }
        public ServerStatusDtoBuilder latency(Long latency) { this.latency = latency; return this; }

        public ServerStatusDto build() {
            return new ServerStatusDto(region, server, status, latency);
        }
    }
}
