package com.hws.henritrip.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AudienceDTO {
    private UUID id;
    private String name;
}
