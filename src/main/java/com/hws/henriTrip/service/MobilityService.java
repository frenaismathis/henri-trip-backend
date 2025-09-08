package com.hws.henritrip.service;

import com.hws.henritrip.dto.MobilityDTO;
import com.hws.henritrip.entity.Mobility;
import com.hws.henritrip.mapper.MobilityMapper;
import com.hws.henritrip.repository.MobilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MobilityService {

    private final MobilityRepository mobilityRepository;

    public List<MobilityDTO> findAll() {
        return mobilityRepository.findAll().stream()
                .map(MobilityMapper::toDto)
                .collect(Collectors.toList());
    }

    public MobilityDTO findById(UUID id) {
        return mobilityRepository.findById(id)
                .map(MobilityMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mobility not found: " + id));
    }

    public Mobility getById(UUID id) {
        return mobilityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Mobility not found: " + id));
    }

    public MobilityDTO create(MobilityDTO dto) {
        Mobility mobility = MobilityMapper.toEntity(dto);
        return MobilityMapper.toDto(mobilityRepository.save(mobility));
    }

    public MobilityDTO update(UUID id, MobilityDTO dto) {
        Mobility mobility = getById(id);
        mobility.setName(dto.getName());
        return MobilityMapper.toDto(mobilityRepository.save(mobility));
    }

    public void delete(UUID id) {
        if (!mobilityRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Mobility not found: " + id);
        }
        mobilityRepository.deleteById(id);
    }
}
