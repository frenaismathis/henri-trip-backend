package com.hws.henritrip.service;

import com.hws.henritrip.dto.SeasonDTO;
import com.hws.henritrip.entity.Season;
import com.hws.henritrip.mapper.SeasonMapper;
import com.hws.henritrip.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeasonService {

    private final SeasonRepository seasonRepository;

    public List<SeasonDTO> findAll() {
        return seasonRepository.findAll().stream()
                .map(SeasonMapper::toDto)
                .collect(Collectors.toList());
    }

    public SeasonDTO findById(UUID id) {
        return seasonRepository.findById(id)
                .map(SeasonMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found: " + id));
    }

    public Season getById(UUID id) {
        return seasonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found: " + id));
    }

    public SeasonDTO create(SeasonDTO dto) {
        Season season = SeasonMapper.toEntity(dto);
        return SeasonMapper.toDto(seasonRepository.save(season));
    }

    public SeasonDTO update(UUID id, SeasonDTO dto) {
        Season season = getById(id);
        season.setName(dto.getName());
        return SeasonMapper.toDto(seasonRepository.save(season));
    }

    public void delete(UUID id) {
        if (!seasonRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found: " + id);
        }
        seasonRepository.deleteById(id);
    }
}
