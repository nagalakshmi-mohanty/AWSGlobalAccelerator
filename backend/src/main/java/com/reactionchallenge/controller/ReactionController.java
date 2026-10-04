package com.reactionchallenge.controller;

import com.reactionchallenge.dto.ReactionRequest;
import com.reactionchallenge.dto.ReactionResponse;
import com.reactionchallenge.model.Reaction;
import com.reactionchallenge.service.ReactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reactions")
public class ReactionController {

    private final ReactionService reactionService;

    public ReactionController(ReactionService reactionService) {
        this.reactionService = reactionService;
    }

    @PostMapping
    public ResponseEntity<ReactionResponse> recordReaction(@Valid @RequestBody ReactionRequest request) {
        ReactionResponse response = reactionService.recordReaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Reaction>> getRecentReactions() {
        List<Reaction> recent = reactionService.getRecentReactions();
        return ResponseEntity.ok(recent);
    }
}
