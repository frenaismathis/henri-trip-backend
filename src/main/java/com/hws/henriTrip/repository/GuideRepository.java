package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GuideRepository extends JpaRepository<Guide, UUID> {
    // méthodes spécifiques futures

    // Find all guides where the given user is part of the invited users set.
    List<Guide> findAllByUsers_Id(UUID userId);

    Optional<Guide> findByTitle(String title);
}
