package com.hws.henritrip.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "mobility")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mobility {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String name;
}