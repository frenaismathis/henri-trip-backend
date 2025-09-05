package com.hws.henriTrip.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guide_user_access")
@Data
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GuideUserAccessId.class)
public class GuideUserAccess {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "guide_id")
    private Long guideId;

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