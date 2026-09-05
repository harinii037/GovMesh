package com.govmesh.governance;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {
    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public AuditLog log(String actor, String action, String detail, String result) {
        AuditLog log = new AuditLog(
                LocalDateTime.now(), actor, action, detail, result);
        return auditRepository.save(log);
    }

    public List<AuditLog> getAllLogs() {
        return auditRepository.findAll();
    }
}
