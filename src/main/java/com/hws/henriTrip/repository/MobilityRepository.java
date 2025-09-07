package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Mobility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MobilityRepository extends JpaRepository<Mobility, UUID> {
}
