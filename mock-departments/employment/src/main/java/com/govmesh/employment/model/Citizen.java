package com.govmesh.employment.model;

public record Citizen(
        String citizenId,
        String fullName,
        int age,
        String employmentStatus,
        String occupation,
        double monthlyIncome
) {
}