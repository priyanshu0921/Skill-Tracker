package com.skilltracker.repository;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.Assessment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {

    @EntityGraph(attributePaths = "skill")
    List<Assessment> findByUserOrderByCreatedAtDesc(AppUser user);

    @EntityGraph(attributePaths = "skill")
    Optional<Assessment> findByIdAndUser(Long id, AppUser user);
}
