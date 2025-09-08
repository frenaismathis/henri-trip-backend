package com.hws.henritrip.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuideCreateRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @Min(value = 1, message = "Days count must be at least 1")
    private int daysCount;

    @NotNull(message = "Mobility option ids are required")
    private Set<@NotNull(message = "Mobility id cannot be null") UUID> mobilityOptionIds;

    @NotNull(message = "Season ids are required")
    private Set<@NotNull(message = "Season id cannot be null") UUID> seasonIds;

    @NotNull(message = "Audience ids are required")
    private Set<@NotNull(message = "Audience id cannot be null") UUID> audienceIds;
}