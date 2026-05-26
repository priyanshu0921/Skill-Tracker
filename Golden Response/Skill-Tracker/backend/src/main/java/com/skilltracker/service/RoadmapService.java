package com.skilltracker.service;

import com.skilltracker.dto.RoadmapRequest;
import com.skilltracker.dto.RoadmapResourceResponse;
import com.skilltracker.dto.RoadmapResponse;
import com.skilltracker.dto.RoadmapStepResponse;
import com.skilltracker.model.AppUser;
import com.skilltracker.model.Roadmap;
import com.skilltracker.model.RoadmapResource;
import com.skilltracker.model.RoadmapStep;
import com.skilltracker.repository.RoadmapRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoadmapService {

    private final RoadmapRepository roadmapRepository;
    private final UserContextService userContextService;

    public RoadmapService(RoadmapRepository roadmapRepository, UserContextService userContextService) {
        this.roadmapRepository = roadmapRepository;
        this.userContextService = userContextService;
    }

    @Transactional(readOnly = true)
    public List<RoadmapResponse> getMyRoadmaps() {
        AppUser user = userContextService.getCurrentUser();
        return roadmapRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public RoadmapResponse createRoadmap(RoadmapRequest request) {
        AppUser user = userContextService.getCurrentUser();

        Roadmap roadmap = Roadmap.builder()
                .user(user)
                .title(request.title())
                .goal(request.goal())
                .currentLevel(request.currentLevel())
                .targetLevel(request.targetLevel())
                .introduction(buildIntroduction(request))
                .projects(List.of(
                        "Build a focused project that applies " + request.title() + " to a real use case",
                        "Publish a short progress log after each milestone to measure consistency"
                ))
                .tips(List.of(
                        "Schedule three deliberate practice sessions each week.",
                        "Retake an assessment after every milestone to validate progress.",
                        "Keep notes on blockers so your next sprint targets the weakest area."
                ))
                .progress(0)
                .createdAt(Instant.now())
                .build();

        List<RoadmapStep> steps = List.of(
                buildStep(roadmap, 1, "Build the foundation",
                        "Refresh the core concepts and define the exact sub-skills you need for " + request.goal() + ".",
                        "Guide", request.title() + " fundamentals", "https://roadmap.sh"),
                buildStep(roadmap, 2, "Practice with structure",
                        "Complete small, repeatable exercises that move you from " + request.currentLevel()
                                + " toward " + request.targetLevel() + ".",
                        "Practice", request.title() + " hands-on practice", "https://www.freecodecamp.org/"),
                buildStep(roadmap, 3, "Ship a proof project",
                        "Create one portfolio-quality deliverable that proves your learning goal in a real workflow.",
                        "Project", request.title() + " project inspiration", "https://github.com/topics"),
                buildStep(roadmap, 4, "Review and level up",
                        "Assess what improved, document remaining gaps, and plan your next milestone.",
                        "Review", request.title() + " interview questions", "https://www.geeksforgeeks.org/")
        );

        roadmap.setSteps(steps);
        Roadmap savedRoadmap = roadmapRepository.save(roadmap);
        return toResponse(savedRoadmap);
    }

    @Transactional
    public RoadmapResponse updateRoadmap(Long roadmapId, RoadmapRequest request) {
        AppUser user = userContextService.getCurrentUser();
        Roadmap roadmap = roadmapRepository.findByIdAndUser(roadmapId, user)
                .orElseThrow(() -> new IllegalArgumentException("Roadmap not found"));

        roadmap.setTitle(request.title());
        roadmap.setGoal(request.goal());
        roadmap.setCurrentLevel(request.currentLevel());
        roadmap.setTargetLevel(request.targetLevel());
        roadmap.setIntroduction(buildIntroduction(request));
        roadmap.setProjects(List.of(
                "Build a focused project that applies " + request.title() + " to a real use case",
                "Publish a short progress log after each milestone to measure consistency"
        ));
        roadmap.setTips(List.of(
                "Schedule three deliberate practice sessions each week.",
                "Retake an assessment after every milestone to validate progress.",
                "Keep notes on blockers so your next sprint targets the weakest area."
        ));
        return toResponse(roadmapRepository.save(roadmap));
    }

    @Transactional
    public RoadmapResponse toggleStep(Long roadmapId, Long stepId) {
        AppUser user = userContextService.getCurrentUser();
        Roadmap roadmap = roadmapRepository.findByIdAndUser(roadmapId, user)
                .orElseThrow(() -> new IllegalArgumentException("Roadmap not found"));

        RoadmapStep step = roadmap.getSteps()
                .stream()
                .filter(item -> item.getId().equals(stepId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Roadmap step not found"));

        step.setCompleted(!step.getCompleted());
        roadmap.setProgress(calculateProgress(roadmap.getSteps()));
        return toResponse(roadmap);
    }

    @Transactional
    public void deleteRoadmap(Long roadmapId) {
        AppUser user = userContextService.getCurrentUser();
        Roadmap roadmap = roadmapRepository.findByIdAndUser(roadmapId, user)
                .orElseThrow(() -> new IllegalArgumentException("Roadmap not found"));
        roadmapRepository.delete(roadmap);
    }

    private RoadmapStep buildStep(
            Roadmap roadmap,
            int stepOrder,
            String title,
            String description,
            String resourceType,
            String resourceTitle,
            String resourceUrl
    ) {
        RoadmapStep step = RoadmapStep.builder()
                .roadmap(roadmap)
                .title(title)
                .description(description)
                .completed(false)
                .stepOrder(stepOrder)
                .build();

        RoadmapResource resource = RoadmapResource.builder()
                .step(step)
                .type(resourceType)
                .title(resourceTitle)
                .url(resourceUrl)
                .build();

        step.setResources(List.of(resource));
        return step;
    }

    private String buildIntroduction(RoadmapRequest request) {
        return "This roadmap is designed to help you move from " + request.currentLevel()
                + " to " + request.targetLevel()
                + " while staying focused on " + request.goal() + ".";
    }

    private int calculateProgress(List<RoadmapStep> steps) {
        if (steps.isEmpty()) {
            return 0;
        }

        long completedSteps = steps.stream()
                .filter(step -> Boolean.TRUE.equals(step.getCompleted()))
                .count();
        return (int) Math.round((completedSteps * 100.0) / steps.size());
    }

    private RoadmapResponse toResponse(Roadmap roadmap) {
        List<RoadmapStepResponse> steps = roadmap.getSteps()
                .stream()
                .sorted(java.util.Comparator.comparingInt(RoadmapStep::getStepOrder))
                .map(step -> new RoadmapStepResponse(
                        step.getId(),
                        step.getTitle(),
                        step.getDescription(),
                        Boolean.TRUE.equals(step.getCompleted()),
                        step.getStepOrder(),
                        step.getResources().stream()
                                .map(resource -> new RoadmapResourceResponse(
                                        resource.getId(),
                                        resource.getType(),
                                        resource.getTitle(),
                                        resource.getUrl()))
                                .toList()))
                .toList();

        return new RoadmapResponse(
                roadmap.getId(),
                roadmap.getTitle(),
                roadmap.getGoal(),
                roadmap.getCurrentLevel(),
                roadmap.getTargetLevel(),
                roadmap.getIntroduction(),
                calculateProgress(roadmap.getSteps()),
                steps,
                roadmap.getProjects().stream().toList(),
                roadmap.getTips().stream().toList()
        );
    }
}
