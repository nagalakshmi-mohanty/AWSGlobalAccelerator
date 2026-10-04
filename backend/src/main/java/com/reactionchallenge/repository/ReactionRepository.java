package com.reactionchallenge.repository;

import com.reactionchallenge.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    @Query("SELECT COUNT(DISTINCT r.sessionId) FROM Reaction r")
    long countDistinctSessions();

    @Query("SELECT AVG(r.reactionTime) FROM Reaction r")
    Double findAverageReactionTime();

    @Query("SELECT COUNT(r) FROM Reaction r WHERE UPPER(r.connectionType) = UPPER(:connectionType)")
    long countByConnectionType(@Param("connectionType") String connectionType);

    @Query("SELECT AVG(r.reactionTime) FROM Reaction r WHERE UPPER(r.connectionType) = UPPER(:connectionType)")
    Double findAverageReactionTimeByConnectionType(@Param("connectionType") String connectionType);

    List<Reaction> findTop20ByOrderByCreatedAtDesc();
}
