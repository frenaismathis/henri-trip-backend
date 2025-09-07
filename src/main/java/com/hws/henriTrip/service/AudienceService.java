package com.hws.henritrip.service;

import com.hws.henritrip.dto.AudienceDTO;
import com.hws.henritrip.entity.Audience;
import com.hws.henritrip.mapper.AudienceMapper;
import com.hws.henritrip.repository.AudienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AudienceService {

    private final AudienceRepository audienceRepository;

    public List<AudienceDTO> findAll() {
        return audienceRepository.findAll().stream()
                .map(AudienceMapper::toDto)
                .collect(Collectors.toList());
    }

    public AudienceDTO findById(UUID id) {
        return audienceRepository.findById(id)
                .map(AudienceMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Audience not found: " + id));
    }

    public Audience getById(UUID id) {
        return audienceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Audience not found: " + id));
    }

    public AudienceDTO create(AudienceDTO dto) {
        Audience audience = AudienceMapper.toEntity(dto);
        return AudienceMapper.toDto(audienceRepository.save(audience));
    }

    public AudienceDTO update(UUID id, AudienceDTO dto) {
        Audience audience = getById(id);
        audience.setName(dto.getName());
        return AudienceMapper.toDto(audienceRepository.save(audience));
    }

    public void delete(UUID id) {
        if (!audienceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Audience not found: " + id);
        }
        audienceRepository.deleteById(id);
    }
}
