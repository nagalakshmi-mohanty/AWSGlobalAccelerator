package com.reactionchallenge.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "reactions")
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "reaction_time", nullable = false)
    private Long reactionTime;

    @Column(name = "target_shown_at")
    private Instant targetShownAt;

    @Column(name = "clicked_at")
    private Instant clickedAt;

    @Column(name = "server_received_at")
    private Instant serverReceivedAt;

    @Column(name = "server_processed_at")
    private Instant serverProcessedAt;

    @Column(name = "server_processing_time")
    private Long serverProcessingTime;

    @Column(name = "server_region")
    private String serverRegion;

    @Column(name = "server_id")
    private String serverId;

    @Column(name = "connection_type")
    private String connectionType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    public Reaction() {}

    public Reaction(Long id, String sessionId, Integer roundNumber, Long reactionTime, Instant targetShownAt, Instant clickedAt, Instant serverReceivedAt, Instant serverProcessedAt, Long serverProcessingTime, String serverRegion, String serverId, String connectionType, Instant createdAt) {
        this.id = id;
        this.sessionId = sessionId;
        this.roundNumber = roundNumber;
        this.reactionTime = reactionTime;
        this.targetShownAt = targetShownAt;
        this.clickedAt = clickedAt;
        this.serverReceivedAt = serverReceivedAt;
        this.serverProcessedAt = serverProcessedAt;
        this.serverProcessingTime = serverProcessingTime;
        this.serverRegion = serverRegion;
        this.serverId = serverId;
        this.connectionType = connectionType;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public Integer getRoundNumber() { return roundNumber; }
    public void setRoundNumber(Integer roundNumber) { this.roundNumber = roundNumber; }

    public Long getReactionTime() { return reactionTime; }
    public void setReactionTime(Long reactionTime) { this.reactionTime = reactionTime; }

    public Instant getTargetShownAt() { return targetShownAt; }
    public void setTargetShownAt(Instant targetShownAt) { this.targetShownAt = targetShownAt; }

    public Instant getClickedAt() { return clickedAt; }
    public void setClickedAt(Instant clickedAt) { this.clickedAt = clickedAt; }

    public Instant getServerReceivedAt() { return serverReceivedAt; }
    public void setServerReceivedAt(Instant serverReceivedAt) { this.serverReceivedAt = serverReceivedAt; }

    public Instant getServerProcessedAt() { return serverProcessedAt; }
    public void setServerProcessedAt(Instant serverProcessedAt) { this.serverProcessedAt = serverProcessedAt; }

    public Long getServerProcessingTime() { return serverProcessingTime; }
    public void setServerProcessingTime(Long serverProcessingTime) { this.serverProcessingTime = serverProcessingTime; }

    public String getServerRegion() { return serverRegion; }
    public void setServerRegion(String serverRegion) { this.serverRegion = serverRegion; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (connectionType == null || connectionType.trim().isEmpty()) {
            connectionType = "DIRECT";
        }
    }

    public static ReactionBuilder builder() {
        return new ReactionBuilder();
    }

    public static class ReactionBuilder {
        private Long id;
        private String sessionId;
        private Integer roundNumber;
        private Long reactionTime;
        private Instant targetShownAt;
        private Instant clickedAt;
        private Instant serverReceivedAt;
        private Instant serverProcessedAt;
        private Long serverProcessingTime;
        private String serverRegion;
        private String serverId;
        private String connectionType;
        private Instant createdAt;

        public ReactionBuilder id(Long id) { this.id = id; return this; }
        public ReactionBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public ReactionBuilder roundNumber(Integer roundNumber) { this.roundNumber = roundNumber; return this; }
        public ReactionBuilder reactionTime(Long reactionTime) { this.reactionTime = reactionTime; return this; }
        public ReactionBuilder targetShownAt(Instant targetShownAt) { this.targetShownAt = targetShownAt; return this; }
        public ReactionBuilder clickedAt(Instant clickedAt) { this.clickedAt = clickedAt; return this; }
        public ReactionBuilder serverReceivedAt(Instant serverReceivedAt) { this.serverReceivedAt = serverReceivedAt; return this; }
        public ReactionBuilder serverProcessedAt(Instant serverProcessedAt) { this.serverProcessedAt = serverProcessedAt; return this; }
        public ReactionBuilder serverProcessingTime(Long serverProcessingTime) { this.serverProcessingTime = serverProcessingTime; return this; }
        public ReactionBuilder serverRegion(String serverRegion) { this.serverRegion = serverRegion; return this; }
        public ReactionBuilder serverId(String serverId) { this.serverId = serverId; return this; }
        public ReactionBuilder connectionType(String connectionType) { this.connectionType = connectionType; return this; }
        public ReactionBuilder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }

        public Reaction build() {
            return new Reaction(id, sessionId, roundNumber, reactionTime, targetShownAt, clickedAt, serverReceivedAt, serverProcessedAt, serverProcessingTime, serverRegion, serverId, connectionType, createdAt);
        }
    }
}
