package com.reactionchallenge.dto;

import java.util.List;

public class AnalyticsResponse {

    private long playersTested;
    private long totalRounds;
    private long averageReaction;
    private long averageNetworkLatency;
    private boolean globalAccelerator;
    private String currentRegion;
    private String currentServer;
    private List<ServerStatusDto> servers;

    private long directTests;
    private long gaTests;
    private long directAverageReaction;
    private long gaAverageReaction;

    public AnalyticsResponse() {}

    public AnalyticsResponse(long playersTested, long totalRounds, long averageReaction, long averageNetworkLatency, boolean globalAccelerator, String currentRegion, String currentServer, List<ServerStatusDto> servers, long directTests, long gaTests, long directAverageReaction, long gaAverageReaction) {
        this.playersTested = playersTested;
        this.totalRounds = totalRounds;
        this.averageReaction = averageReaction;
        this.averageNetworkLatency = averageNetworkLatency;
        this.globalAccelerator = globalAccelerator;
        this.currentRegion = currentRegion;
        this.currentServer = currentServer;
        this.servers = servers;
        this.directTests = directTests;
        this.gaTests = gaTests;
        this.directAverageReaction = directAverageReaction;
        this.gaAverageReaction = gaAverageReaction;
    }

    public long getPlayersTested() { return playersTested; }
    public void setPlayersTested(long playersTested) { this.playersTested = playersTested; }

    public long getTotalRounds() { return totalRounds; }
    public void setTotalRounds(long totalRounds) { this.totalRounds = totalRounds; }

    public long getAverageReaction() { return averageReaction; }
    public void setAverageReaction(long averageReaction) { this.averageReaction = averageReaction; }

    public long getAverageNetworkLatency() { return averageNetworkLatency; }
    public void setAverageNetworkLatency(long averageNetworkLatency) { this.averageNetworkLatency = averageNetworkLatency; }

    public boolean isGlobalAccelerator() { return globalAccelerator; }
    public void setGlobalAccelerator(boolean globalAccelerator) { this.globalAccelerator = globalAccelerator; }

    public String getCurrentRegion() { return currentRegion; }
    public void setCurrentRegion(String currentRegion) { this.currentRegion = currentRegion; }

    public String getCurrentServer() { return currentServer; }
    public void setCurrentServer(String currentServer) { this.currentServer = currentServer; }

    public List<ServerStatusDto> getServers() { return servers; }
    public void setServers(List<ServerStatusDto> servers) { this.servers = servers; }

    public long getDirectTests() { return directTests; }
    public void setDirectTests(long directTests) { this.directTests = directTests; }

    public long getGaTests() { return gaTests; }
    public void setGaTests(long gaTests) { this.gaTests = gaTests; }

    public long getDirectAverageReaction() { return directAverageReaction; }
    public void setDirectAverageReaction(long directAverageReaction) { this.directAverageReaction = directAverageReaction; }

    public long getGaAverageReaction() { return gaAverageReaction; }
    public void setGaAverageReaction(long gaAverageReaction) { this.gaAverageReaction = gaAverageReaction; }

    public static AnalyticsResponseBuilder builder() {
        return new AnalyticsResponseBuilder();
    }

    public static class AnalyticsResponseBuilder {
        private long playersTested;
        private long totalRounds;
        private long averageReaction;
        private long averageNetworkLatency;
        private boolean globalAccelerator;
        private String currentRegion;
        private String currentServer;
        private List<ServerStatusDto> servers;
        private long directTests;
        private long gaTests;
        private long directAverageReaction;
        private long gaAverageReaction;

        public AnalyticsResponseBuilder playersTested(long playersTested) { this.playersTested = playersTested; return this; }
        public AnalyticsResponseBuilder totalRounds(long totalRounds) { this.totalRounds = totalRounds; return this; }
        public AnalyticsResponseBuilder averageReaction(long averageReaction) { this.averageReaction = averageReaction; return this; }
        public AnalyticsResponseBuilder averageNetworkLatency(long averageNetworkLatency) { this.averageNetworkLatency = averageNetworkLatency; return this; }
        public AnalyticsResponseBuilder globalAccelerator(boolean globalAccelerator) { this.globalAccelerator = globalAccelerator; return this; }
        public AnalyticsResponseBuilder currentRegion(String currentRegion) { this.currentRegion = currentRegion; return this; }
        public AnalyticsResponseBuilder currentServer(String currentServer) { this.currentServer = currentServer; return this; }
        public AnalyticsResponseBuilder servers(List<ServerStatusDto> servers) { this.servers = servers; return this; }
        public AnalyticsResponseBuilder directTests(long directTests) { this.directTests = directTests; return this; }
        public AnalyticsResponseBuilder gaTests(long gaTests) { this.gaTests = gaTests; return this; }
        public AnalyticsResponseBuilder directAverageReaction(long directAverageReaction) { this.directAverageReaction = directAverageReaction; return this; }
        public AnalyticsResponseBuilder gaAverageReaction(long gaAverageReaction) { this.gaAverageReaction = gaAverageReaction; return this; }

        public AnalyticsResponse build() {
            return new AnalyticsResponse(playersTested, totalRounds, averageReaction, averageNetworkLatency, globalAccelerator, currentRegion, currentServer, servers, directTests, gaTests, directAverageReaction, gaAverageReaction);
        }
    }
}
