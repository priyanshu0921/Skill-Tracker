package com.skilltracker.repository;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.TrainingResource;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingResourceRepository extends JpaRepository<TrainingResource, Long> {

    @EntityGraph(attributePaths = "skill")
    List<TrainingResource> findByUserOrderByEstimatedMinutesAsc(AppUser user);
}
