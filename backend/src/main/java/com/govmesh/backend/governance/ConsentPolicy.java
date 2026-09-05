package com.govmesh.backend.governance;

import jakarta.persistence.*;

@Entity
@Table(name = "consent_policy",
       uniqueConstraints = @UniqueConstraint(
               columnNames = {"source_dept", "target_dept", "data_category"}))
public class ConsentPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_dept", nullable = false, length = 100)
    private String sourceDept;

    @Column(name = "target_dept", nullable = false, length = 100)
    private String targetDept;

    @Column(name = "data_category", nullable = false, length = 100)
    private String dataCategory;

    @Column(nullable = false)
    private boolean allowed;

    protected ConsentPolicy() {}

    public ConsentPolicy(String sourceDept, String targetDept,
                         String dataCategory, boolean allowed) {
        this.sourceDept = sourceDept;
        this.targetDept = targetDept;
        this.dataCategory = dataCategory;
        this.allowed = allowed;
    }

    public Long getId() { return id; }
    public String getSourceDept() { return sourceDept; }
    public String getTargetDept() { return targetDept; }
    public String getDataCategory() { return dataCategory; }
    public boolean isAllowed() { return allowed; }
}
