package com.aditya.infilect.storeusermapdb.entity;

import com.aditya.infilect.storemasterdb.entity.StoreMaster;
import com.aditya.infilect.usermasterdb.entity.UserMaster;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "permanent_journey_plans",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_store_date",
                        columnNames = {"user_id", "store_id", "date"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PermanentJourneyPlan {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotNull(message = "User ID is required")
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_permanent_journey_plan_user"))
        @OnDelete(action = OnDeleteAction.CASCADE)
        private UserMaster userMaster;


        @NotNull(message = "Store ID is required")
        @ManyToOne
        @JoinColumn(name = "store_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_permanent_journey_plan_store"))
        @OnDelete(action = OnDeleteAction.CASCADE)
        private StoreMaster storeMaster;

        @PastOrPresent(message = "Date cannot be in the future")
        @Column(name = "date")
        private LocalDate date;

        @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
        private Boolean isActive = true;

        @CreationTimestamp
        @Column(name = "created_on", updatable = false)
        private LocalDateTime createdOn;

        @UpdateTimestamp
        @Column(name = "modified_on")
        private LocalDateTime modifiedOn;

}
