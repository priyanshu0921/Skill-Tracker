package com.skilltracker.service;

import com.skilltracker.dto.TrainingResourceResponse;
import com.skilltracker.model.AppUser;
import com.skilltracker.model.TrainingResource;
import com.skilltracker.repository.TrainingResourceRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    private final TrainingResourceRepository trainingResourceRepository;
    private final UserContextService userContextService;

    public TrainingService(TrainingResourceRepository trainingResourceRepository, UserContextService userContextService) {
        this.trainingResourceRepository = trainingResourceRepository;
        this.userContextService = userContextService;
    }

    public List<TrainingResourceResponse> getMyTrainingResources() {
        AppUser user = userContextService.getCurrentUser();
        return trainingResourceRepository.findByUserOrderByEstimatedMinutesAsc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TrainingResourceResponse toResponse(TrainingResource resource) {
        return new TrainingResourceResponse(
                resource.getId(),
                resource.getSkill().getId(),
                resource.getSkill().getName(),
                resource.getType(),
                resource.getTitle(),
                resource.getDescription(),
                resource.getUrl(),
                resource.getEstimatedMinutes()
        );
    }
}
