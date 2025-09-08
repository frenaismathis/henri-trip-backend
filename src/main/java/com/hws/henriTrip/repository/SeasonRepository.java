package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeasonRepository extends JpaRepository<Season, UUID> {
}
