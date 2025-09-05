package com.hws.henriTrip.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audience")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Audience {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
}
