package com.govmesh.backend.department;

public record CreateDepartmentRequest(String name, DepartmentType type, String baseUrl) {
}