package com.skilltracker.service;

import com.skilltracker.dto.AssessmentAttemptResponse;
import com.skilltracker.dto.DailyActivityResponse;
import com.skilltracker.dto.DailyGrowthResponse;
import com.skilltracker.dto.DashboardResponse;
import com.skilltracker.dto.GoalResponse;
import com.skilltracker.dto.SkillResponse;
import com.skilltracker.dto.TrainingResourceResponse;
import com.skilltracker.model.GoalStatus;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final SkillService skillService;
    private final LearningGoalService goalService;
    private final AssessmentService assessmentService;
    private final TrainingService trainingService;
    private final UserContextService userContextService;

    public DashboardService(
            SkillService skillService,
            LearningGoalService goalService,
            AssessmentService assessmentService,
            TrainingService trainingService,
            UserContextService userContextService
    ) {
        this.skillService = skillService;
        this.goalService = goalService;
        this.assessmentService = assessmentService;
        this.trainingService = trainingService;
        this.userContextService = userContextService;
    }

    public DashboardResponse getDashboard() {
        List<SkillResponse> skills = skillService.getMySkills();
        List<GoalResponse> goals = goalService.getMyGoals();
        List<AssessmentAttemptResponse> attempts = assessmentService.getRecentAttempts();
        List<TrainingResourceResponse> training = trainingService.getMyTrainingResources();

        return new DashboardResponse(
                userContextService.getCurrentUser().getFullName(),
                skills.size(),
                (int) goals.stream().filter(goal -> goal.status() != GoalStatus.COMPLETED).count(),
                attempts.size(),
                skills.stream().mapToInt(SkillResponse::confidenceScore).average().orElse(0),
                buildDailyGrowth(skills, goals, attempts),
                skills.stream().limit(4).toList(),
                goals.stream().limit(4).toList(),
                attempts,
                training.stream().limit(4).toList(),
                buildInsights(skills, goals, attempts)
        );
    }

    private DailyGrowthResponse buildDailyGrowth(
            List<SkillResponse> skills,
            List<GoalResponse> goals,
            List<AssessmentAttemptResponse> attempts
    ) {
        LocalDate today = LocalDate.now();
        Set<LocalDate> activityDates = collectActivityDates(skills, attempts);
        int activeDaysThisWeek = (int) IntStream.range(0, 7)
                .mapToObj(today::minusDays)
                .filter(activityDates::contains)
                .count();

        return new DailyGrowthResponse(
                countCurrentStreak(activityDates, today),
                countLongestStreak(activityDates),
                activityDates.contains(today),
                activeDaysThisWeek,
                7,
                buildRecentActivity(activityDates, today, 14),
                buildMomentumLabel(activityDates, today, activeDaysThisWeek),
                buildNextMilestone(skills, goals)
        );
    }

    private Set<LocalDate> collectActivityDates(
            List<SkillResponse> skills,
            List<AssessmentAttemptResponse> attempts
    ) {
        Set<LocalDate> activityDates = new HashSet<>();

        skills.stream()
                .map(SkillResponse::lastPracticedOn)
                .filter(Objects::nonNull)
                .forEach(activityDates::add);

        attempts.stream()
                .map(AssessmentAttemptResponse::attemptedAt)
                .filter(Objects::nonNull)
                .map(timestamp -> timestamp.atZone(ZoneId.systemDefault()).toLocalDate())
                .forEach(activityDates::add);

        return activityDates;
    }

    private List<DailyActivityResponse> buildRecentActivity(Set<LocalDate> activityDates, LocalDate today, int days) {
        return IntStream.rangeClosed(0, days - 1)
                .mapToObj(offset -> today.minusDays(days - 1L - offset))
                .map(date -> new DailyActivityResponse(date, activityDates.contains(date)))
                .toList();
    }

    private int countCurrentStreak(Set<LocalDate> activityDates, LocalDate today) {
        if (!activityDates.contains(today)) {
            return 0;
        }

        int streak = 0;
        LocalDate cursor = today;
        while (activityDates.contains(cursor)) {
            streak++;
            cursor = cursor.minusDays(1);
        }
        return streak;
    }

    private int countLongestStreak(Set<LocalDate> activityDates) {
        List<LocalDate> orderedDates = activityDates.stream()
                .sorted(Comparator.naturalOrder())
                .toList();

        if (orderedDates.isEmpty()) {
            return 0;
        }

        int longest = 1;
        int current = 1;

        for (int index = 1; index < orderedDates.size(); index++) {
            if (orderedDates.get(index - 1).plusDays(1).equals(orderedDates.get(index))) {
                current++;
                longest = Math.max(longest, current);
            } else {
                current = 1;
            }
        }

        return longest;
    }

    private String buildMomentumLabel(Set<LocalDate> activityDates, LocalDate today, int activeDaysThisWeek) {
        if (activityDates.contains(today) && activeDaysThisWeek >= 5) {
            return "You're in a high-consistency stretch this week.";
        }
        if (activityDates.contains(today)) {
            return "Today already counts. Keep the chain moving with one more focused block.";
        }
        if (activityDates.contains(today.minusDays(1))) {
            return "You were active yesterday. A short practice session today keeps the momentum real.";
        }
        if (activeDaysThisWeek >= 3) {
            return "Your weekly rhythm is building. Lock in today to convert activity into a visible streak.";
        }
        return "Start with one intentional practice session today and let the streak begin.";
    }

    private String buildNextMilestone(List<SkillResponse> skills, List<GoalResponse> goals) {
        return skills.stream()
                .filter(skill -> skill.confidenceScore() < skill.targetScore())
                .findFirst()
                .map(skill -> {
                    int gap = skill.targetScore() - skill.confidenceScore();
                    return "Close the " + gap + "% gap on " + skill.name() + " to hit your next target.";
                })
                .orElseGet(() -> goals.stream()
                        .filter(goal -> goal.status() != GoalStatus.COMPLETED)
                        .findFirst()
                        .map(goal -> "Move \"" + goal.title() + "\" forward with one completed action today.")
                        .orElse("Add a new skill or goal to unlock a guided daily growth plan."));
    }


    private List<String> buildInsights(
            List<SkillResponse> skills,
            List<GoalResponse> goals,
            List<AssessmentAttemptResponse> attempts
    ) {
        List<String> insights = new ArrayList<>();

        skills.stream()
                .filter(skill -> skill.confidenceScore() < skill.targetScore())
                .findFirst()
                .ifPresent(skill -> insights.add(
                        skill.name() + " is below its target score. Prioritize one assessment and one training session this week."
                ));

        goals.stream()
                .filter(goal -> goal.status() == GoalStatus.IN_PROGRESS)
                .findFirst()
                .ifPresent(goal -> insights.add("Active momentum is on " + goal.skillName() + " via \"" + goal.title() + "\"."));

        attempts.stream()
                .findFirst()
                .ifPresent(attempt -> insights.add("Most recent assessment: " + attempt.score() + "/" + attempt.totalQuestions()
                        + " in " + attempt.skillName() + "."));

        if (insights.isEmpty()) {
            insights.add("Start by adding one skill and one goal, then take an assessment to generate recommendations.");
        }

        return insights;
    }
}
