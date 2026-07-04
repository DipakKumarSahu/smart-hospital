package com.hospital.smart_hospital.role.repositories;

import com.hospital.smart_hospital.role.Role;
import com.hospital.smart_hospital.role.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository
        extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleType name);
}
