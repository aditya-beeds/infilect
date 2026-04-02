package com.aditya.infilect.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StoreMasterDTO {
    private String storeId;
    private String storeExternalId;
    private String name;
    private String title;
    private String storeBrand;
    private String storeType;
    private String city;
    private String state;
    private String country;
    private String region;
    private Float latitude;
    private Float longitude;
}
