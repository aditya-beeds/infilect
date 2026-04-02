package com.aditya.infilect.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserMasterDTO {
    private String username;
    private String first_name;
    private String last_name;
    private String email;
    private Integer user_type;
    private Long phone_number;
    private String supervisor_username;
    private String is_active;
}
