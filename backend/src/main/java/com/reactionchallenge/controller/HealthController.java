package com.reactionchallenge.controller;

import com.reactionchallenge.dto.HealthResponse;
import com.reactionchallenge.dto.NetworkTestResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api")
public class HealthController {

    @Value("${server.region:local}")
    private String serverRegion;

    @Value("${server.id:local-server}")
    private String serverId;

    @GetMapping("/health")
    public ResponseEntity<HealthResponse> getHealth() {
        HealthResponse response = HealthResponse.builder()
                .status("UP")
                .region(serverRegion)
                .server(serverId)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/network-test")
    public ResponseEntity<NetworkTestResponse> runNetworkTest() {
        NetworkTestResponse response = NetworkTestResponse.builder()
                .status("OK")
                .region(serverRegion)
                .server(serverId)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
}
