package com.reactionchallenge.dto;

public class ReactionResponse {

    private boolean success;
    private Long id;
    private Long reactionTime;
    private String serverRegion;
    private String serverId;
    private Long serverProcessingTime;

    public ReactionResponse() {}

    public ReactionResponse(boolean success, Long id, Long reactionTime, String serverRegion, String serverId, Long serverProcessingTime) {
        this.success = success;
        this.id = id;
        this.reactionTime = reactionTime;
        this.serverRegion = serverRegion;
        this.serverId = serverId;
        this.serverProcessingTime = serverProcessingTime;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getReactionTime() { return reactionTime; }
    public void setReactionTime(Long reactionTime) { this.reactionTime = reactionTime; }

    public String getServerRegion() { return serverRegion; }
    public void setServerRegion(String serverRegion) { this.serverRegion = serverRegion; }

    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }

    public Long getServerProcessingTime() { return serverProcessingTime; }
    public void setServerProcessingTime(Long serverProcessingTime) { this.serverProcessingTime = serverProcessingTime; }

    public static ReactionResponseBuilder builder() {
        return new ReactionResponseBuilder();
    }

    public static class ReactionResponseBuilder {
        private boolean success;
        private Long id;
        private Long reactionTime;
        private String serverRegion;
        private String serverId;
        private Long serverProcessingTime;

        public ReactionResponseBuilder success(boolean success) { this.success = success; return this; }
        public ReactionResponseBuilder id(Long id) { this.id = id; return this; }
        public ReactionResponseBuilder reactionTime(Long reactionTime) { this.reactionTime = reactionTime; return this; }
        public ReactionResponseBuilder serverRegion(String serverRegion) { this.serverRegion = serverRegion; return this; }
        public ReactionResponseBuilder serverId(String serverId) { this.serverId = serverId; return this; }
        public ReactionResponseBuilder serverProcessingTime(Long serverProcessingTime) { this.serverProcessingTime = serverProcessingTime; return this; }

        public ReactionResponse build() {
            return new ReactionResponse(success, id, reactionTime, serverRegion, serverId, serverProcessingTime);
        }
    }
}
