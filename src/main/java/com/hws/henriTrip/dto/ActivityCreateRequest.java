package com.hws.henritrip.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO used to create an activity associated to a guide.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivityCreateRequest {
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

    @Min(value = 1, message = "Order in day must be at least 1")
    private int orderInDay;

    @Min(value = 1, message = "Day number must be at least 1")
    private int dayNumber;
}
