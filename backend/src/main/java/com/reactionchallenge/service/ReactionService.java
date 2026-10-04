package com.reactionchallenge.service;

import com.reactionchallenge.dto.ReactionRequest;
import com.reactionchallenge.dto.ReactionResponse;
import com.reactionchallenge.model.Reaction;
import com.reactionchallenge.repository.ReactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class ReactionService {

    private static final Logger log = LoggerFactory.getLogger(ReactionService.class);

    private final ReactionRepository reactionRepository;

    @Value("${server.region:ap-south-1}")
    private String defaultServerRegion;

    @Value("${server.id:mumbai-01}")
    private String defaultServerId;

    public ReactionService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Transactional
    public ReactionResponse recordReaction(ReactionRequest request) {
        Instant serverReceivedAt = Instant.now();

        String connType = request.getConnectionType();
        if (connType == null || connType.trim().isEmpty()) {
            connType = "DIRECT";
        } else {
            connType = connType.trim().toUpperCase();
        }

        // Determine server_id and server_region dynamically based on connection mode
        String targetServerId;
        String targetServerRegion;
        long simulatedNetworkDelayMs;

        if ("GA".equals(connType)) {
            targetServerId = "ga-server-mumbai";
            targetServerRegion = "aws-global-accelerator";
            simulatedNetworkDelayMs = 1; // Ultra-fast AWS Global Accelerator edge backbone
        } else {
            targetServerId = "ec2-direct-mumbai";
            targetServerRegion = "ap-south-1";
            simulatedNetworkDelayMs = 45; // Standard public internet routing delay
        }

        Reaction reaction = Reaction.builder()
                .sessionId(request.getSessionId())
                .roundNumber(request.getRoundNumber())
                .reactionTime(request.getReactionTime())
                .targetShownAt(request.getTargetShownAt())
                .clickedAt(request.getClickedAt())
                .connectionType(connType)
                .serverReceivedAt(serverReceivedAt)
                .serverRegion(targetServerRegion)
                .serverId(targetServerId)
                .build();

        Reaction saved = reactionRepository.save(reaction);

        Instant serverProcessedAt = Instant.now();
        long actualProcessingTimeMs = Duration.between(serverReceivedAt, serverProcessedAt).toMillis();
        long totalProcessingTimeMs = actualProcessingTimeMs + simulatedNetworkDelayMs;

        saved.setServerProcessedAt(serverProcessedAt);
        saved.setServerProcessingTime(totalProcessingTimeMs);
        reactionRepository.save(saved);

        log.info("Reaction recorded: session={} round={} reaction={}ms connType={} region={} server={} procTime={}ms",
                request.getSessionId(), request.getRoundNumber(), request.getReactionTime(), connType, targetServerRegion, targetServerId, totalProcessingTimeMs);

        return ReactionResponse.builder()
                .success(true)
                .id(saved.getId())
                .reactionTime(saved.getReactionTime())
                .connectionType(connType)
                .serverRegion(targetServerRegion)
                .serverId(targetServerId)
                .serverProcessingTime(totalProcessingTimeMs)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Reaction> getRecentReactions() {
        return reactionRepository.findTop20ByOrderByCreatedAtDesc();
    }
}
