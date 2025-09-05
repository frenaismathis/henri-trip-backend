package com.hws.henriTrip.entity;

import lombok.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuideUserAccessId implements Serializable {
    private Long userId;
    private Long guideId;
}