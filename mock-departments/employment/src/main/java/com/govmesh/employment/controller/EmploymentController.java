package com.govmesh.employment.controller;

import com.govmesh.employment.model.Citizen;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmploymentController {

    // Internal mock data.
    // This exists inside the department but is NOT exposed through an endpoint.
    private final List<Citizen> citizens = List.of(
            new Citizen(
                    "C101",
                    "Rahul Sharma",
                    28,
                    "EMPLOYED",
                    "Software Engineer",
                    65000
            ),
            new Citizen(
                    "C102",
                    "Priya Nair",
                    32,
                    "UNEMPLOYED",
                    "NONE",
                    0
            ),
            new Citizen(
                    "C103",
                    "Arjun Kumar",
                    25,
                    "EMPLOYED",
                    "Data Analyst",
                    55000
            )
    );

    // Only expose the schema
    @GetMapping("/schema")
    public List<String> getSchema() {
        return List.of(
                "citizenId",
                "fullName",
                "age",
                "employmentStatus",
                "occupation",
                "monthlyIncome"
        );
    }
}