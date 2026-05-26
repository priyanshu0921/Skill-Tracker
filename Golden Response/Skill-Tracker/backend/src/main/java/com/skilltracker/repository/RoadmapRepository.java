package com.skilltracker.repository;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.Roadmap;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long> {

    List<Roadmap> findByUserOrderByCreatedAtDesc(AppUser user);

    Optional<Roadmap> findByIdAndUser(Long id, AppUser user);
}
