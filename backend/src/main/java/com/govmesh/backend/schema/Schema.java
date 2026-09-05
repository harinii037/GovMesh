package com.govmesh.backend.schema;

import com.govmesh.backend.department.Department;
import jakarta.persistence.*;

@Entity
@Table(name = "schemas")
public class Schema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String fieldsJson;

    public Schema() {}

    public Schema(Department department, String fieldsJson) {
        this.department = department;
        this.fieldsJson = fieldsJson;
    }

    public Long getId() { return id; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public String getFieldsJson() { return fieldsJson; }
    public void setFieldsJson(String fieldsJson) { this.fieldsJson = fieldsJson; }
}