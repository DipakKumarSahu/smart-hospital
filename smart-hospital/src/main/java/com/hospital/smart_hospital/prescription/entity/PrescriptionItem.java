package com.hospital.smart_hospital.prescription.entity;



import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.medicine.entity.Medicine;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "prescription_items",
        indexes = {
                @Index(name = "idx_prescription", columnList = "prescription_id"),
                @Index(name = "idx_medicine", columnList = "medicine_id")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionItem extends AuditableEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "prescription_id",
            nullable = false
    )
    private Prescription prescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "medicine_id",
            nullable = false
    )
    private Medicine medicine;

    @Column(nullable = false, length = 100)
    private String dosage;

    @Column(nullable = false, length = 100)
    private String frequency;

    @Column(nullable = false)
    private Integer durationDays;

    @Column(length = 500)
    private String instructions;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;
}
