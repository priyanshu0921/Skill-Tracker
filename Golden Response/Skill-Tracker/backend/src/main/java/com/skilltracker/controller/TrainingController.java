package com.skilltracker.controller;

import com.skilltracker.dto.TrainingResourceResponse;
import com.skilltracker.service.TrainingService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/training")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    public List<TrainingResourceResponse> getTrainingResources() {
        return trainingService.getMyTrainingResources();
    }
}
