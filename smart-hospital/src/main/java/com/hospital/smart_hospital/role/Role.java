package com.hospital.smart_hospital.role;

import com.hospital.smart_hospital.common.entity.AuditableEntity;
import com.hospital.smart_hospital.common.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "roles")
public class Role extends AuditableEntity {

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private RoleType name;

}
