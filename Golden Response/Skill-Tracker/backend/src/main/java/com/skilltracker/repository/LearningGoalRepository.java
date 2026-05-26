package com.skilltracker.repository;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.LearningGoal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LearningGoalRepository extends JpaRepository<LearningGoal, Long> {

    @EntityGraph(attributePaths = "skill")
    List<LearningGoal> findByUserOrderByTargetDateAsc(AppUser user);

    @EntityGraph(attributePaths = "skill")
    Optional<LearningGoal> findByIdAndUser(Long id, AppUser user);

    void deleteByIdAndUser(Long id, AppUser user);
}
