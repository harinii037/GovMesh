package com.govmesh.welfare.model;

public record Beneficiary(
        String beneficiaryId,
        String beneficiaryName,
        int ageYears,
        String workStatus,
        double incomePerMonth,
        boolean benefitEligible
) {
}