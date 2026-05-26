package com.skilltracker.controller;

import com.skilltracker.dto.RoadmapRequest;
import com.skilltracker.dto.RoadmapResponse;
import com.skilltracker.service.RoadmapService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roadmaps")
public class RoadmapController {

    private final RoadmapService roadmapService;

    public RoadmapController(RoadmapService roadmapService) {
        this.roadmapService = roadmapService;
    }

    @GetMapping
    public List<RoadmapResponse> getRoadmaps() {
        return roadmapService.getMyRoadmaps();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoadmapResponse createRoadmap(@Valid @RequestBody RoadmapRequest request) {
        return roadmapService.createRoadmap(request);
    }

    @PutMapping("/{roadmapId}")
    public RoadmapResponse updateRoadmap(@PathVariable Long roadmapId, @Valid @RequestBody RoadmapRequest request) {
        return roadmapService.updateRoadmap(roadmapId, request);
    }

    @PatchMapping("/{roadmapId}/steps/{stepId}/toggle")
    public RoadmapResponse toggleStep(@PathVariable Long roadmapId, @PathVariable Long stepId) {
        return roadmapService.toggleStep(roadmapId, stepId);
    }

    @DeleteMapping("/{roadmapId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoadmap(@PathVariable Long roadmapId) {
        roadmapService.deleteRoadmap(roadmapId);
    }
}
