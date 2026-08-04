package com.hospital.smart_hospital.medicine.entity;


import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.prescription.entity.PrescriptionItem;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "medicines",
        indexes = {
                @Index(name = "idx_medicine_name", columnList = "name"),
                @Index(name = "idx_generic_name", columnList = "generic_name"),
                @Index(name = "idx_medicine_code", columnList = "medicine_code")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Medicine extends AuditableEntity {

    @Column(name = "medicine_code",
            nullable = false,
            unique = true,
            length = 30)
    private String medicineCode;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "generic_name", length = 150)
    private String genericName;

    @Column(length = 50)
    private String strength;

    @Column(length = 100)
    private String manufacturer;

    @Column(length = 100)
    private String dosageForm;

    @Builder.Default
    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 100)
    private String unit;

    @OneToMany(mappedBy = "medicine")
    @Builder.Default
    private List<PrescriptionItem> prescriptionItems = new ArrayList<>();
}
