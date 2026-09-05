package com.govmesh.welfare.controller;

import com.govmesh.welfare.model.Beneficiary;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class WelfareController {

    private final List<Beneficiary> beneficiaries = List.of(
            new Beneficiary("C101", "Rahul Sharma", 28, "WORKING", 65000, false),
            new Beneficiary("C102", "Priya Nair", 32, "NOT_WORKING", 0, true),
            new Beneficiary("C103", "Arjun Kumar", 25, "WORKING", 55000, false)
    );

    @GetMapping("/schema")
    public List<String> getSchema() {
        return List.of(
                "beneficiaryId",
                "beneficiaryName",
                "ageYears",
                "workStatus",
                "incomePerMonth",
                "benefitEligible"
        );
    }

    @PostMapping("/applications")
    public Map<String, Object> receiveApplication(
            @RequestBody Map<String, Object> data) {

        return Map.of(
                "received", true,
                "data", data
        );
    }
}