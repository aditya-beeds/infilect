package com.aditya.infilect.usermasterdb.entity;

import jakarta.persistence.*;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_users_email", columnNames = "email")
})
@Check(constraints = "user_type IN (1, 2, 3, 7)")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 150, message = "Username must be between 3 and 150 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores and hyphens")
    @Column(name = "username", nullable = false, unique = true, length = 150)
    private String username;

    @Column(name = "first_name", length = 150)
    private String firstName = "";

    @Column(name = "last_name", length = 150)
    private String lastName = "";

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 254, message = "Email must be less than 254 characters")
    @Column(name = "email", nullable = false, unique = true, length = 254)
    private String email;

    @Column(name = "user_type", columnDefinition = "INTEGER DEFAULT 1")
    @Min(value = 1, message = "User type must be 1, 2, 3, or 7")
    @Max(value = 7, message = "User type must be 1, 2, 3, or 7")
    private Integer userType = 1;

    @Column(name = "phone_number", length = 32)
    @Pattern(regexp = "^[+]?[0-9]+$", message = "Phone number contains invalid characters")
    private String phoneNumber = "";

    @ManyToOne
    @JoinColumn(name = "supervisor_id", foreignKey = @ForeignKey(name = "fk_user_supervisor"))
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private UserMaster supervisor;

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;

    @UpdateTimestamp
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    // Helper method to check user type
    public boolean isValidUserType() {
        return userType != null && (userType == 1 || userType == 2 || userType == 3 || userType == 7);
    }

    // Helper method to get full name
    public String getFullName() {
        if (firstName == null && lastName == null) return "";
        if (firstName == null) return lastName;
        if (lastName == null) return firstName;
        return firstName + " " + lastName;
    }

}
