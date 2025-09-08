package com.hws.henritrip.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "guide")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    @Min(value = 1, message = "daysCount must be greater than 0")
    private int daysCount;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ManyToMany
    @JoinTable(name = "guide_mobility", joinColumns = @JoinColumn(name = "guide_id"), inverseJoinColumns = @JoinColumn(name = "mobility_id"))
    private Set<Mobility> mobilityOptions = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "guide_season", joinColumns = @JoinColumn(name = "guide_id"), inverseJoinColumns = @JoinColumn(name = "season_id"))
    private Set<Season> seasons = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "guide_audience", joinColumns = @JoinColumn(name = "guide_id"), inverseJoinColumns = @JoinColumn(name = "audience_id"))
    private Set<Audience> audiences = new HashSet<>();

    @OneToMany(mappedBy = "guide", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> activities = new HashSet<>();

    @ManyToMany(mappedBy = "guides")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Guide))
            return false;
        Guide guide = (Guide) o;
        return id != null && id.equals(guide.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
