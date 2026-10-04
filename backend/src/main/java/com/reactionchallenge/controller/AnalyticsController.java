package com.reactionchallenge.controller;

import com.reactionchallenge.dto.AnalyticsResponse;
import com.reactionchallenge.service.AnalyticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics() {
        AnalyticsResponse response = analyticsService.getAnalytics();
        return ResponseEntity.ok(response);
    }
}
