package com.govmesh.backend.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateDepartmentRequest(
        @NotBlank String name,
        @NotNull DepartmentType type,
        String baseUrl
) {}