package com.govmesh.governance;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/test/audit")
public class AuditTestController {
    private final AuditService auditService;

    public AuditTestController(AuditService auditService) {
        this.auditService = auditService;
    }

    @PostMapping
    public AuditLog createTestLog() {
        return auditService.log(
                "test-user",
                "SCHEMA_DISCOVERED",
                "Test schema discovery",
                "SUCCESS"
        );
    }

    @GetMapping
    public List<AuditLog> getLogs() {
        return auditService.getAllLogs();
    }
}
