package com.hws.henritrip.seed;

import com.hws.henritrip.entity.*;
import com.hws.henritrip.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final MobilityRepository mobilityRepository;
    private final SeasonRepository seasonRepository;
    private final AudienceRepository audienceRepository;
    private final PasswordEncoder passwordEncoder;
    private final GuideRepository guideRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedRoles();
        seedUsers();
        seedMobilities();
        seedSeasons();
        seedAudiences();
        seedGuides();
    }

    private void seedRoles() {
        if (roleRepository.findByName("ADMIN").isEmpty()) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("USER").isEmpty()) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }
    }

    private void seedUsers() {
        if (userRepository.findByEmail("admin@henritrip.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseThrow();
            User admin = new User();
            admin.setFirstname("Admin");
            admin.setLastname("User");
            admin.setEmail("admin@henritrip.com");
            admin.setPassword(passwordEncoder.encode("admin@123"));
            admin.setRole(adminRole);
            userRepository.save(admin);
        }

        if (userRepository.findByEmail("john.doe@example.com").isEmpty()) {
            Role userRole = roleRepository.findByName("USER").orElseThrow();
            User user = new User();
            user.setFirstname("John");
            user.setLastname("Doe");
            user.setEmail("john.doe@example.com");
            user.setPassword(passwordEncoder.encode("user@123"));
            user.setRole(userRole);
            userRepository.save(user);
        }
    }

    private void seedMobilities() {
        List<String> mobilityNames = List.of("WALK", "CAR", "BIKE", "BUS", "TRAIN", "MOTORBIKE");

        for (String name : mobilityNames) {
            boolean exists = mobilityRepository.findAll().stream()
                    .anyMatch(m -> m.getName().equals(name));
            if (!exists) {
                Mobility mobility = new Mobility();
                mobility.setName(name);
                mobilityRepository.save(mobility);
            }
        }
    }

    private void seedSeasons() {
        List<String> seasonNames = List.of("SUMMER", "WINTER", "SPRING", "AUTUMN");

        for (String name : seasonNames) {
            boolean exists = seasonRepository.findAll().stream()
                    .anyMatch(s -> s.getName().equals(name));
            if (!exists) {
                Season season = new Season();
                season.setName(name);
                seasonRepository.save(season);
            }
        }
    }

    private void seedAudiences() {
        List<String> audienceNames = List.of("FAMILY", "ADULTS", "CHILDREN", "TEENS", "SENIORS", "LONELY", "COUPLE",
                "FRIENDS", "ALL");

        for (String name : audienceNames) {
            boolean exists = audienceRepository.findAll().stream()
                    .anyMatch(a -> a.getName().equals(name));
            if (!exists) {
                Audience audience = new Audience();
                audience.setName(name);
                audienceRepository.save(audience);
            }
        }
    }

    private void seedGuides() {
        if (!guideRepository.findAll().isEmpty()) {
            return;
        }

        List<Mobility> allMobilities = mobilityRepository.findAll();
        List<Season> allSeasons = seasonRepository.findAll();
        List<Audience> allAudiences = audienceRepository.findAll();

        // Guide 1 : Circuit historique (3 jours)
        Guide circuitHistorique = new Guide();
        circuitHistorique.setTitle("Circuit Historique");
        circuitHistorique.setDescription("Visite guidée de trois jours à travers le cœur historique de la ville.");
        circuitHistorique.setDaysCount(3);
        circuitHistorique.setMobilityOptions(new HashSet<>(allMobilities.stream()
                .filter(m -> List.of("MARCHE", "VOITURE").contains(m.getName().toUpperCase()))
                .toList()));
        circuitHistorique.setSeasons(new HashSet<>(allSeasons));
        circuitHistorique.setAudiences(new HashSet<>(allAudiences));

        Activity histA1 = new Activity();
        histA1.setGuide(circuitHistorique);
        histA1.setDayNumber(1);
        histA1.setOrderInDay(1);
        histA1.setTitle("Musée des Antiquités");
        histA1.setDescription("Collection d'objets anciens et d'archéologie locale.");
        histA1.setCategory("musée");
        histA1.setAddress("Place de l'Hôtel de Ville");
        histA1.setOpeningHours("10:00-18:00");

        Activity histA2 = new Activity();
        histA2.setGuide(circuitHistorique);
        histA2.setDayNumber(2);
        histA2.setOrderInDay(1);
        histA2.setTitle("Visite du château vieux");
        histA2.setDescription("Découverte des salles historiques et de la tour principale.");
        histA2.setCategory("château");
        histA2.setAddress("Rue du Château, 1");
        histA2.setOpeningHours("09:00-17:00");

        Activity histA3 = new Activity();
        histA3.setGuide(circuitHistorique);
        histA3.setDayNumber(3);
        histA3.setOrderInDay(1);
        histA3.setTitle("Promenade au parc central");
        histA3.setDescription("Temps libre pour se détendre dans le parc emblématique.");
        histA3.setCategory("parc");
        histA3.setAddress("Avenue du Parc");
        histA3.setOpeningHours("06:00-22:00");

        circuitHistorique.getActivities().add(histA1);
        circuitHistorique.getActivities().add(histA2);
        circuitHistorique.getActivities().add(histA3);

        // Guide 2 : Aventures souterraines (2 jours)
        Guide aventuresSouterraines = new Guide();
        aventuresSouterraines.setTitle("Aventures souterraines");
        aventuresSouterraines.setDescription("Exploration guidée de grottes et sites naturels souterrains.");
        aventuresSouterraines.setDaysCount(2);
        aventuresSouterraines.setMobilityOptions(new HashSet<>(allMobilities.stream()
                .filter(m -> List.of("MARCHE", "BUS").contains(m.getName().toUpperCase()))
                .toList()));
        aventuresSouterraines.setSeasons(new HashSet<>(allSeasons.stream()
                .filter(s -> List.of("PRINTEMPS", "ETE").contains(s.getName().toUpperCase()))
                .toList()));
        aventuresSouterraines.setAudiences(new HashSet<>(allAudiences));

        Activity caveA1 = new Activity();
        caveA1.setGuide(aventuresSouterraines);
        caveA1.setDayNumber(1);
        caveA1.setOrderInDay(1);
        caveA1.setTitle("Grotte de la Vallée");
        caveA1.setDescription("Parcours guidé à l'intérieur d'une grotte spectaculaire.");
        caveA1.setCategory("grotte");
        caveA1.setAddress("Route de la Grotte");
        caveA1.setOpeningHours("09:00-16:00");

        Activity caveA2 = new Activity();
        caveA2.setGuide(aventuresSouterraines);
        caveA2.setDayNumber(2);
        caveA2.setOrderInDay(1);
        caveA2.setTitle("Atelier spéléologie");
        caveA2.setDescription("Initiation à la spéléologie pour découvrir la technique de progression.");
        caveA2.setCategory("activité");
        caveA2.setAddress("Centre d'Activités Aventure");
        caveA2.setOpeningHours("10:00-15:00");

        aventuresSouterraines.getActivities().add(caveA1);
        aventuresSouterraines.getActivities().add(caveA2);

        // Guide 3 : Saveurs locales (1 jour)
        Guide saveursLocales = new Guide();
        saveursLocales.setTitle("Saveurs locales");
        saveursLocales
                .setDescription("Parcours d'une journée pour goûter les spécialités locales et visiter le marché.");
        saveursLocales.setDaysCount(1);
        saveursLocales.setMobilityOptions(new HashSet<>(allMobilities.stream()
                .filter(m -> List.of("MARCHE").contains(m.getName().toUpperCase()))
                .toList()));
        saveursLocales.setSeasons(new HashSet<>(allSeasons.stream()
                .filter(s -> List.of("PRINTEMPS", "ETE", "AUTOMNE").contains(s.getName().toUpperCase()))
                .toList()));
        saveursLocales.setAudiences(new HashSet<>(allAudiences));

        Activity foodA1 = new Activity();
        foodA1.setGuide(saveursLocales);
        foodA1.setDayNumber(1);
        foodA1.setOrderInDay(1);
        foodA1.setTitle("Visite du marché couvert");
        foodA1.setDescription("Découverte des étals, dégustations et achats de produits frais.");
        foodA1.setCategory("activité");
        foodA1.setAddress("Marché Central");
        foodA1.setOpeningHours("07:00-13:00");

        Activity foodA2 = new Activity();
        foodA2.setGuide(saveursLocales);
        foodA2.setDayNumber(1);
        foodA2.setOrderInDay(2);
        foodA2.setTitle("Dîner chez le chef local");
        foodA2.setDescription("Menu dégustation préparé par un chef local.");
        foodA2.setCategory("activité");
        foodA2.setAddress("Rue Gourmande, 8");
        foodA2.setOpeningHours("19:00-22:00");

        saveursLocales.getActivities().add(foodA1);
        saveursLocales.getActivities().add(foodA2);

        // Guide 4 : Châteaux et jardins (4 jours)
        Guide chateauxJardins = new Guide();
        chateauxJardins.setTitle("Châteaux et jardins");
        chateauxJardins
                .setDescription("Parcours de quatre jours autour des plus beaux châteaux et jardins de la région.");
        chateauxJardins.setDaysCount(4);
        chateauxJardins.setMobilityOptions(new HashSet<>(allMobilities.stream()
                .filter(m -> List.of("VOITURE", "VELO", "MARChE".toUpperCase()).contains(m.getName().toUpperCase()))
                .toList()));
        chateauxJardins.setSeasons(new HashSet<>(allSeasons));
        chateauxJardins.setAudiences(new HashSet<>(allAudiences));

        Activity castleA1 = new Activity();
        castleA1.setGuide(chateauxJardins);
        castleA1.setDayNumber(1);
        castleA1.setOrderInDay(1);
        castleA1.setTitle("Château de la Rivière");
        castleA1.setDescription("Visite guidée du château et de ses appartements historiques.");
        castleA1.setCategory("château");
        castleA1.setAddress("Domaine de la Rivière");
        castleA1.setOpeningHours("10:00-17:00");

        Activity castleA2 = new Activity();
        castleA2.setGuide(chateauxJardins);
        castleA2.setDayNumber(2);
        castleA2.setOrderInDay(1);
        castleA2.setTitle("Jardin à la française");
        castleA2.setDescription("Promenade dans les jardins classés et explication de leur histoire.");
        castleA2.setCategory("parc");
        castleA2.setAddress("Allée des Jardins");
        castleA2.setOpeningHours("09:00-18:00");

        Activity castleA3 = new Activity();
        castleA3.setGuide(chateauxJardins);
        castleA3.setDayNumber(3);
        castleA3.setOrderInDay(1);
        castleA3.setTitle("Visite du petit château");
        castleA3.setDescription("Petit château familial avec visite guidée.");
        castleA3.setCategory("château");
        castleA3.setAddress("Petit Château Lane");
        castleA3.setOpeningHours("10:00-16:00");

        Activity castleA4 = new Activity();
        castleA4.setGuide(chateauxJardins);
        castleA4.setDayNumber(4);
        castleA4.setOrderInDay(1);
        castleA4.setTitle("Parc naturel et belvédère");
        castleA4.setDescription("Randonnée facile et panorama depuis le belvédère.");
        castleA4.setCategory("parc");
        castleA4.setAddress("Route du Belvédère");
        castleA4.setOpeningHours("08:00-19:00");

        chateauxJardins.getActivities().add(castleA1);
        chateauxJardins.getActivities().add(castleA2);
        chateauxJardins.getActivities().add(castleA3);
        chateauxJardins.getActivities().add(castleA4);

        // Persist all guides (activities cascade)
        guideRepository.save(circuitHistorique);
        guideRepository.save(aventuresSouterraines);
        guideRepository.save(saveursLocales);
        guideRepository.save(chateauxJardins);

        Optional<User> optionalRegularUser = userRepository.findByEmail("john.doe@example.com");
        if (optionalRegularUser.isEmpty()) {
            return;
        }

        User regularUser = optionalRegularUser.get();

        // Load guides from DB (ensure we operate on managed entities)
        List<Guide> allGuides = guideRepository.findAll();

        if (allGuides.size() < 2) {
            // Not enough guides to associate
            return;
        }

        // Choose two guides to associate (by title to be deterministic)
        Guide firstGuide = allGuides.stream()
                .filter(g -> "Circuit Historique".equals(g.getTitle()))
                .findFirst()
                .orElse(allGuides.get(0));

        Guide secondGuide = allGuides.stream()
                .filter(g -> "Saveurs locales".equals(g.getTitle()))
                .findFirst()
                .orElseGet(() -> allGuides.size() > 1 ? allGuides.get(1) : allGuides.get(0));

        // Add associations on owning side (User.guides). Check existing associations to
        // keep idempotency.
        boolean changed = false;

        if (regularUser.getGuides().stream()
                .noneMatch(g -> g.getId() != null && g.getId().equals(firstGuide.getId()))) {
            regularUser.getGuides().add(firstGuide);
            changed = true;
        }

        if (regularUser.getGuides().stream()
                .noneMatch(g -> g.getId() != null && g.getId().equals(secondGuide.getId()))) {
            regularUser.getGuides().add(secondGuide);
            changed = true;
        }

        if (changed) {
            userRepository.save(regularUser);
        }

        associateGuidesToRegularUserIfNeeded();
    }

    public void associateGuidesToRegularUserIfNeeded() {
        Optional<User> optionalRegularUser = userRepository.findByEmail("john.doe@example.com");
        if (optionalRegularUser.isEmpty()) {
            return;
        }

        User regularUser = optionalRegularUser.get();

        // Load guides from DB (ensure we operate on managed entities)
        List<Guide> allGuides = guideRepository.findAll();

        // Choose two guides to associate (by title to be deterministic)
        Guide firstGuide = allGuides.stream()
                .filter(g -> "Circuit Historique".equals(g.getTitle()))
                .findFirst()
                .orElse(allGuides.get(0));

        Guide secondGuide = allGuides.stream()
                .filter(g -> "Saveurs locales".equals(g.getTitle()))
                .findFirst()
                .orElseGet(() -> allGuides.size() > 1 ? allGuides.get(1) : allGuides.get(0));

        // Add associations on owning side (User.guides). Check existing associations to
        // keep idempotency.
        boolean changed = false;

        if (regularUser.getGuides().stream()
                .noneMatch(g -> g.getId() != null && g.getId().equals(firstGuide.getId()))) {
            regularUser.getGuides().add(firstGuide);
            changed = true;
        }

        if (regularUser.getGuides().stream()
                .noneMatch(g -> g.getId() != null && g.getId().equals(secondGuide.getId()))) {
            regularUser.getGuides().add(secondGuide);
            changed = true;
        }

        if (changed) {
            userRepository.save(regularUser);
        }
    }
}
