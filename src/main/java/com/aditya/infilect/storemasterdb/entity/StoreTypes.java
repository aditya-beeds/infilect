package com.aditya.infilect.storemasterdb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Locale;


@Entity
@Table(name = "store_types",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_store_type_name", columnNames = "name")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StoreTypes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotBlank(message = "Store type name is required")
    @Size(min = 1, max = 255, message = "Store type name must be between 1 and 255 characters")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-&'.]+$", message = "Store type contains invalid characters")
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

}
