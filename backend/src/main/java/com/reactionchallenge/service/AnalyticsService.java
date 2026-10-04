package com.reactionchallenge.service;

import com.reactionchallenge.dto.AnalyticsResponse;
import com.reactionchallenge.dto.ServerStatusDto;
import com.reactionchallenge.repository.ReactionRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnalyticsService {

    private final ReactionRepository reactionRepository;

    @Value("${server.region:local}")
    private String serverRegion;

    @Value("${server.id:local-server}")
    private String serverId;

    @Value("${global.accelerator.enabled:false}")
    private boolean globalAcceleratorEnabled;

    public AnalyticsService(ReactionRepository reactionRepository) {
        this.reactionRepository = reactionRepository;
    }

    @Transactional(readOnly = true)
    public AnalyticsResponse getAnalytics() {
        long playersTested = reactionRepository.countDistinctSessions();
        long totalRounds = reactionRepository.count();

        Double avgReactionDouble = reactionRepository.findAverageReactionTime();
        long averageReaction = avgReactionDouble != null ? Math.round(avgReactionDouble) : 0;

        long directTests = reactionRepository.countByConnectionType("DIRECT");
        long gaTests = reactionRepository.countByConnectionType("GA");

        Double directAvgDouble = reactionRepository.findAverageReactionTimeByConnectionType("DIRECT");
        long directAverageReaction = directAvgDouble != null ? Math.round(directAvgDouble) : 0;

        Double gaAvgDouble = reactionRepository.findAverageReactionTimeByConnectionType("GA");
        long gaAverageReaction = gaAvgDouble != null ? Math.round(gaAvgDouble) : 0;

        List<ServerStatusDto> servers = new ArrayList<>();
        servers.add(ServerStatusDto.builder()
                .region(serverRegion)
                .server(serverId)
                .status("HEALTHY")
                .latency(34L)
                .build());

        if ("ap-south-1".equalsIgnoreCase(serverRegion)) {
            servers.add(ServerStatusDto.builder()
                    .region("us-east-1")
                    .server("usa-01")
                    .status("HEALTHY")
                    .latency(92L)
                    .build());
        } else if ("us-east-1".equalsIgnoreCase(serverRegion)) {
            servers.add(ServerStatusDto.builder()
                    .region("ap-south-1")
                    .server("mumbai-01")
                    .status("HEALTHY")
                    .latency(92L)
                    .build());
        }

        return AnalyticsResponse.builder()
                .playersTested(playersTested)
                .totalRounds(totalRounds)
                .averageReaction(averageReaction)
                .averageNetworkLatency(34L)
                .globalAccelerator(globalAcceleratorEnabled)
                .currentRegion(serverRegion)
                .currentServer(serverId)
                .servers(servers)
                .directTests(directTests)
                .gaTests(gaTests)
                .directAverageReaction(directAverageReaction)
                .gaAverageReaction(gaAverageReaction)
                .build();
    }
}
