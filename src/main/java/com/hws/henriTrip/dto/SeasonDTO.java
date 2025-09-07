package com.hws.henritrip.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class SeasonDTO {
    private UUID id;
    private String name;
}