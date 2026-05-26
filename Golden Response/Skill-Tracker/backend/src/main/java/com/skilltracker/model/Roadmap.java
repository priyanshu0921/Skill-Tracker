package com.skilltracker.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String goal;

    @Column(nullable = false)
    private String currentLevel;

    @Column(nullable = false)
    private String targetLevel;

    @Column(nullable = false, length = 2000)
    private String introduction;

    @Builder.Default
    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoadmapStep> steps = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    private List<String> projects = new ArrayList<>();

    @Builder.Default
    @ElementCollection
    private List<String> tips = new ArrayList<>();

    @Column(nullable = false)
    private Integer progress;

    @Column(nullable = false)
    private Instant createdAt;
}
