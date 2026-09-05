package com.govmesh.backend.schema;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SchemaRepository extends JpaRepository<Schema, Long> {
    Optional<Schema> findByDepartmentId(Long departmentId);
}