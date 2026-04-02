package com.aditya.infilect.storemasterdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Locale;

@Entity
@Table(name = "countries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_country_name", columnNames = "name")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Countries {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Store Country name is required")
    @Size(min = 1, max = 255, message = "Store Country name must be between 1 and 255 characters")
    @Column(name = "name", nullable = false, unique = true,length = 255)
    private String name;

}
