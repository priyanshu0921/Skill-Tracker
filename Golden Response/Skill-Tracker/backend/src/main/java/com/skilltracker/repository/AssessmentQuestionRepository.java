package com.skilltracker.repository;

import com.skilltracker.model.Assessment;
import com.skilltracker.model.AssessmentQuestion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentQuestionRepository extends JpaRepository<AssessmentQuestion, Long> {

    List<AssessmentQuestion> findByAssessmentOrderByIdAsc(Assessment assessment);
}
