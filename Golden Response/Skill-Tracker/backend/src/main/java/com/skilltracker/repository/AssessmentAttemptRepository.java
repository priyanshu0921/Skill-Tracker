package com.skilltracker.repository;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.AssessmentAttempt;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentAttemptRepository extends JpaRepository<AssessmentAttempt, Long> {

    @EntityGraph(attributePaths = {"assessment", "assessment.skill"})
    List<AssessmentAttempt> findTop5ByUserOrderByAttemptedAtDesc(AppUser user);
}
