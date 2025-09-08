package com.hws.henritrip.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {
    @NotNull(message = "Id is required for this operation")
    private UUID id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Address is required")
    private String address;

    private String phone;

    private String openingHours;

    private String website;

    @NotNull(message = "Day number is required")
    @Min(value = 1, message = "Day number must be at least 1")
    private int dayNumber;

    @Min(value = 1, message = "Order in day must be at least 1")
    private int orderInDay;

    @NotNull(message = "Guide id is required")
    private UUID guideId;
}
