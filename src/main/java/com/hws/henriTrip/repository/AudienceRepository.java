package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Audience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AudienceRepository extends JpaRepository<Audience, UUID> {
}
