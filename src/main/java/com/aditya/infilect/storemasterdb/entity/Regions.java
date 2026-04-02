package com.aditya.infilect.storemasterdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Locale;

@Entity
@Table(name = "regions",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_region_name", columnNames = "name")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Regions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Store Region name is required")
    @Size(min = 1, max = 255, message = "Store Region name must be between 1 and 255 characters")
    @Column(name = "name", nullable = false, unique = true,length = 255)
    private String name;

}
