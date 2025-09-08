package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    List<Activity> findByGuide_IdOrderByDayNumberAscOrderInDayAsc(UUID guideId);

    List<Activity> findByGuide_IdAndDayNumberOrderByOrderInDayAsc(UUID guideId, int dayNumber);
}
