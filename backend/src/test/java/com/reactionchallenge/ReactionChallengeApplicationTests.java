package com.reactionchallenge;

import com.reactionchallenge.dto.ReactionRequest;
import com.reactionchallenge.dto.ReactionResponse;
import com.reactionchallenge.service.ReactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ReactionChallengeApplicationTests {

    @MockBean
    private ReactionService reactionService;

    @Test
    void contextLoads() {
        assertNotNull(reactionService);
    }

    @Test
    void testRecordReactionServiceMock() {
        ReactionRequest request = ReactionRequest.builder()
                .sessionId("test-session-123")
                .roundNumber(1)
                .reactionTime(150L)
                .targetShownAt(Instant.now())
                .clickedAt(Instant.now())
                .build();

        ReactionResponse expectedResponse = ReactionResponse.builder()
                .success(true)
                .id(1L)
                .reactionTime(150L)
                .serverRegion("local")
                .serverId("local-server")
                .serverProcessingTime(2L)
                .build();

        when(reactionService.recordReaction(any(ReactionRequest.class))).thenReturn(expectedResponse);

        ReactionResponse result = reactionService.recordReaction(request);
        assertTrue(result.isSuccess());
        assertEquals(150L, result.getReactionTime());
        assertEquals("local", result.getServerRegion());
    }
}
