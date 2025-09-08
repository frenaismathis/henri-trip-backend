package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Guide;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GuideRepository extends JpaRepository<Guide, UUID> {
    List<Guide> findAllByUsers_Id(UUID userId);
}
