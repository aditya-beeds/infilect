package com.aditya.infilect.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PermanentJourneyPlanDTO {
    private String username;
    private String storeId;
    private String date;
    private String isActive;
}
