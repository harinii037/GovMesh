package com.govmesh.legacydept.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LegacyController {

    // Internal legacy data.
    private final List<String> citizenData = List.of(
            "C101|Rahul Sharma|EMPLOYED|65000",
            "C102|Priya Nair|UNEMPLOYED|0",
            "C103|Arjun Kumar|EMPLOYED|55000"
    );

    @GetMapping("/schema")
    public List<String> getSchema() {
        return List.of(
                "citizen_id",
                "name",
                "employment",
                "income"
        );
    }

    // Runtime endpoint.
    // Simulates a legacy system exporting pipe-delimited data.
    @GetMapping(value = "/export", produces = "text/plain")
    public String exportData() {
        return citizenData.get(0);
    }
}