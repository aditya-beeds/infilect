package com.aditya.infilect.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMasterDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Integer userType;
    private Long phoneNumber;
    private String supervisorUsername;
    private String isActive;
}
