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

    @Value("${server.region:local}")
    private String serverRegion;

    @Value("${server.id:local-server}")
    private String serverId;

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

        Reaction reaction = Reaction.builder()
                .sessionId(request.getSessionId())
                .roundNumber(request.getRoundNumber())
                .reactionTime(request.getReactionTime())
                .targetShownAt(request.getTargetShownAt())
                .clickedAt(request.getClickedAt())
                .connectionType(connType)
                .serverReceivedAt(serverReceivedAt)
                .serverRegion(serverRegion)
                .serverId(serverId)
                .build();

        Reaction saved = reactionRepository.save(reaction);

        Instant serverProcessedAt = Instant.now();
        long processingTimeMs = Duration.between(serverReceivedAt, serverProcessedAt).toMillis();

        saved.setServerProcessedAt(serverProcessedAt);
        saved.setServerProcessingTime(processingTimeMs);
        reactionRepository.save(saved);

        log.info("Reaction received: session={} round={} reaction={}ms connectionType={} region={} server={}",
                request.getSessionId(), request.getRoundNumber(), request.getReactionTime(), connType, serverRegion, serverId);

        return ReactionResponse.builder()
                .success(true)
                .id(saved.getId())
                .reactionTime(saved.getReactionTime())
                .serverRegion(serverRegion)
                .serverId(serverId)
                .serverProcessingTime(processingTimeMs)
                .build();
    }

    @Transactional(readOnly = true)
    public List<Reaction> getRecentReactions() {
        return reactionRepository.findTop20ByOrderByCreatedAtDesc();
    }
}
