package com.hws.henriTrip.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "guide")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guide {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int daysCount;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToMany
    @JoinTable(
        name = "guide_mobility",
        joinColumns = @JoinColumn(name = "guide_id"),
        inverseJoinColumns = @JoinColumn(name = "mobility_id")
    )
    private Set<Mobility> mobilityOptions;

    @ManyToMany
    @JoinTable(
        name = "guide_season",
        joinColumns = @JoinColumn(name = "guide_id"),
        inverseJoinColumns = @JoinColumn(name = "season_id")
    )
    private Set<Season> seasons;

    @ManyToMany
    @JoinTable(
        name = "guide_audience",
        joinColumns = @JoinColumn(name = "guide_id"),
        inverseJoinColumns = @JoinColumn(name = "audience_id")
    )
    private Set<Audience> audiences;

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> activities;

    @ManyToMany(mappedBy = "guides")
    private Set<User> users;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
