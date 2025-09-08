package com.hws.henritrip.seed;

import com.hws.henritrip.entity.*;
import com.hws.henritrip.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

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
    public void run(String... args) {
        seedRoles();
        seedUsers();
        seedMobilities();
        seedSeasons();
        seedAudiences();
        seedGuide();
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
        List<String> mobilityNames = List.of("WALK", "PUBLIC_TRANSPORT", "CAR");

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
        List<String> audienceNames = List.of("ADULT", "CHILD", "FAMILY");

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

    private void seedGuide() {
        if (guideRepository.findAll().isEmpty()) {
            Guide guide = new Guide();
            guide.setTitle("City Tour");
            guide.setDescription("A sample guided tour of the city.");
            guide.setDaysCount(3);

            // Assign mobilities
            List<Mobility> allMobilities = mobilityRepository.findAll();
            guide.setMobilityOptions(new HashSet<>(allMobilities.stream()
                    .filter(m -> List.of("WALK", "PUBLIC_TRANSPORT").contains(m.getName()))
                    .toList()));

            // Assign seasons
            List<Season> allSeasons = seasonRepository.findAll();
            guide.setSeasons(new HashSet<>(allSeasons));

            // Assign audiences
            List<Audience> allAudiences = audienceRepository.findAll();
            guide.setAudiences(new HashSet<>(allAudiences));

            guideRepository.save(guide);
        }
    }
}
