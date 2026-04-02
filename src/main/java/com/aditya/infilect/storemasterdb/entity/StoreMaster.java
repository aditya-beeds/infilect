package com.aditya.infilect.storemasterdb.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "store_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class StoreMaster {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @NotBlank(message = "Store ID is required")
        @Size(min = 1, max = 255, message = "Store ID must be between 1 and 255 characters")
        @Column(name = "store_id", nullable = false, unique = true, length = 255)
        private String storeId;

        @Column(name = "store_external_id", length = 255)
        private String storeExternalId = "";

        @NotBlank(message = "Name is required")
        @Column(name = "name", nullable = false)
        private String name;

        @NotBlank(message = "Title is required")
        @Column(name = "title", nullable = false)
        private String title;

        @ManyToOne
        @JoinColumn(name = "store_brand_id", foreignKey = @ForeignKey(name = "fk_store_brand"))
        @OnDelete(action = OnDeleteAction.SET_NULL)
        private StoreBrands storeBrands;

        @ManyToOne
        @JoinColumn(name = "store_type_id", foreignKey = @ForeignKey(name = "fk_store_type"))
        @OnDelete(action = OnDeleteAction.SET_NULL)
        private StoreTypes storeType;

        @ManyToOne
        @JoinColumn(name = "city_id", foreignKey = @ForeignKey(name = "fk_city"))
        @OnDelete(action = OnDeleteAction.SET_NULL)
        private Cities city;

        @ManyToOne
        @JoinColumn(name = "state_id", foreignKey = @ForeignKey(name = "fk_state"))
        @OnDelete(action = OnDeleteAction.SET_NULL)
        private States state;

        @ManyToOne
        @JoinColumn(name = "country_id", foreignKey = @ForeignKey(name = "fk_country"))
        @OnDelete(action = OnDeleteAction.SET_NULL)
        private Countries country;

        @ManyToOne
        @JoinColumn(name = "region_id", foreignKey = @ForeignKey(name = "fk_region"))
        @OnDelete(action = OnDeleteAction.SET_NULL)
        private Regions region;

        @Column(name = "latitude", columnDefinition = "FLOAT DEFAULT 0.0")
        private Float latitude;

        @Column(name = "longitude", columnDefinition = "FLOAT DEFAULT 0.0")
        private Float longitude;

        @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
        private Boolean isActive;

        @CreationTimestamp
        @Column(name = "created_on", updatable = false)
        private LocalDateTime createdOn;

        @UpdateTimestamp
        @Column(name = "modified_on")
        private LocalDateTime modifiedOn;
}
