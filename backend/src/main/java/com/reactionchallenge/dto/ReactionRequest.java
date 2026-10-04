package com.reactionchallenge.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public class ReactionRequest {

    @NotBlank(message = "sessionId is required")
    private String sessionId;

    @NotNull(message = "roundNumber is required")
    @Min(value = 1, message = "roundNumber must be >= 1")
    private Integer roundNumber;

    @NotNull(message = "reactionTime is required")
    @Min(value = 1, message = "reactionTime must be > 0")
    private Long reactionTime;

    private Instant targetShownAt;

    private Instant clickedAt;

    private String connectionType;

    public ReactionRequest() {}

    public ReactionRequest(String sessionId, Integer roundNumber, Long reactionTime, Instant targetShownAt, Instant clickedAt, String connectionType) {
        this.sessionId = sessionId;
        this.roundNumber = roundNumber;
        this.reactionTime = reactionTime;
        this.targetShownAt = targetShownAt;
        this.clickedAt = clickedAt;
        this.connectionType = connectionType;
    }

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

    public String getConnectionType() { return connectionType; }
    public void setConnectionType(String connectionType) { this.connectionType = connectionType; }

    public static ReactionRequestBuilder builder() {
        return new ReactionRequestBuilder();
    }

    public static class ReactionRequestBuilder {
        private String sessionId;
        private Integer roundNumber;
        private Long reactionTime;
        private Instant targetShownAt;
        private Instant clickedAt;
        private String connectionType;

        public ReactionRequestBuilder sessionId(String sessionId) { this.sessionId = sessionId; return this; }
        public ReactionRequestBuilder roundNumber(Integer roundNumber) { this.roundNumber = roundNumber; return this; }
        public ReactionRequestBuilder reactionTime(Long reactionTime) { this.reactionTime = reactionTime; return this; }
        public ReactionRequestBuilder targetShownAt(Instant targetShownAt) { this.targetShownAt = targetShownAt; return this; }
        public ReactionRequestBuilder clickedAt(Instant clickedAt) { this.clickedAt = clickedAt; return this; }
        public ReactionRequestBuilder connectionType(String connectionType) { this.connectionType = connectionType; return this; }

        public ReactionRequest build() {
            return new ReactionRequest(sessionId, roundNumber, reactionTime, targetShownAt, clickedAt, connectionType);
        }
    }
}
