package com.govmesh.backend.department;

import jakarta.persistence.*;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DepartmentType type;

    private String baseUrl;

    // JPA requires a no-argument constructor — it uses this internally
    // when loading rows back out of the database.
    public Department() {}

    public Department(String name, DepartmentType type, String baseUrl) {
        this.name = name;
        this.type = type;
        this.baseUrl = baseUrl;
    }

    // Getters and setters — Spring/JPA need these to read and write fields
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public DepartmentType getType() { return type; }
    public void setType(DepartmentType type) { this.type = type; }

    public String getBaseUrl() { return baseUrl; }
    public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
}