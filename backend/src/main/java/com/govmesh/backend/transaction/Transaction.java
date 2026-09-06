package com.govmesh.backend.transaction;

import com.govmesh.backend.department.Department;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "source_department_id", nullable = false)
    private Department sourceDepartment;

    @ManyToOne
    @JoinColumn(name = "target_department_id", nullable = false)
    private Department targetDepartment;

    private String contractId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    private Instant createdAt;
    private Instant updatedAt;

    public Transaction() {}

    public Transaction(Department sourceDepartment, Department targetDepartment, String contractId) {
        this.sourceDepartment = sourceDepartment;
        this.targetDepartment = targetDepartment;
        this.contractId = contractId;
        this.status = TransactionStatus.PENDING;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public Department getSourceDepartment() { return sourceDepartment; }
    public Department getTargetDepartment() { return targetDepartment; }
    public String getContractId() { return contractId; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; this.updatedAt = Instant.now(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}