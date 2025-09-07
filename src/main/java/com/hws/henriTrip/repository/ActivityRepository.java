package com.hws.henritrip.repository;

import com.hws.henritrip.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
    // Toutes les activités d'un guide triées par jour puis ordre
    List<Activity> findByGuide_IdOrderByDayNumberAscOrderInDayAsc(UUID guideId);

    // Activités d'un guide pour un jour spécifique, triées par ordre
    List<Activity> findByGuide_IdAndDayNumberOrderByOrderInDayAsc(UUID guideId, int dayNumber);
}
