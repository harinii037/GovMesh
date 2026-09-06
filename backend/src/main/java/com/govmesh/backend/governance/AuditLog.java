package com.govmesh.backend.governance;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false, length = 100)
    private String actor;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Column(nullable = false, length = 20)
    private String result;

    protected AuditLog() {}

    public AuditLog(LocalDateTime timestamp, String actor, String action,
                     String detail, String result) {
        this.timestamp = timestamp;
        this.actor = actor;
        this.action = action;
        this.detail = detail;
        this.result = result;
    }

    public Long getId() { return id; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getActor() { return actor; }
    public String getAction() { return action; }
    public String getDetail() { return detail; }
    public String getResult() { return result; }
}
