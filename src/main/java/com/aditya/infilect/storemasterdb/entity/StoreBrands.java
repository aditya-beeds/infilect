package com.aditya.infilect.storemasterdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;


@Entity
@Table(name = "store_brands",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_store_brand_name", columnNames = "name")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class StoreBrands {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Store Brand name is required")
    @Size(min = 1, max = 255, message = "Store Brand name must be between 1 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-&'.]+$", message = "Store Brand name contains invalid characters")
    @Column(name = "name", nullable = false, unique = true,length = 255)
    private String name;
}
