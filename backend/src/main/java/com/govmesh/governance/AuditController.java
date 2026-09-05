package com.govmesh.governance;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/audit")
public class AuditController {

    private final AuditRepository auditRepository;

    public AuditController(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping
    public List<AuditLog> getAuditLogs() {
        return auditRepository.findAllByOrderByTimestampDesc();
    }
}
