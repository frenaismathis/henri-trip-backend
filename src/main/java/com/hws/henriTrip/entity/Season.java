package com.hws.henriTrip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "season")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
}