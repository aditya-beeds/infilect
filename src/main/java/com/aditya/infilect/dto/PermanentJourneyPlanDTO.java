package com.aditya.infilect.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PermanentJourneyPlanDTO {
    private String username;
    private String store_id;
    private String date;
    private Boolean is_active;
}
