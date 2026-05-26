package com.skilltracker.repository;

import com.skilltracker.model.AppUser;
import com.skilltracker.model.Skill;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByUserOrderByPriorityRankAscNameAsc(AppUser user);

    Optional<Skill> findByIdAndUser(Long id, AppUser user);

    void deleteByIdAndUser(Long id, AppUser user);
}
