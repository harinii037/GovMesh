package com.govmesh.backend.mapping;

import com.govmesh.backend.governance.AuditService;
import com.govmesh.backend.security.DepartmentAuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/mappings")
public class MappingApprovalTestController {

    private final DepartmentAuthorizationService authorizationService;
    private final AuditService auditService;

    public MappingApprovalTestController(
            DepartmentAuthorizationService authorizationService,
            AuditService auditService) {
        this.authorizationService = authorizationService;
        this.auditService = auditService;
    }

    @PostMapping("/approve/test")
    public ResponseEntity<?> approve(
            @RequestBody ApprovalRequest request,
            Authentication authentication) {

        String actor = authentication.getName();

        if (!authorizationService.canActForSourceDepartment(
                authentication, request.sourceDept())) {

            auditService.log(
                    actor,
                    "MAPPING_APPROVAL_DENIED",
                    "RBAC denied: actor cannot approve mappings for source department "
                            + request.sourceDept(),
                    "DENIED"
            );

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of(
                            "status", "DENIED",
                            "reason", "RBAC: actor cannot act for source department "
                                    + request.sourceDept()
                    )
            );
        }

        auditService.log(
                actor,
                "MAPPING_APPROVED",
                request.mappingId() + ": "
                        + request.sourceDept() + " -> " + request.targetDept(),
                "SUCCESS"
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "APPROVED",
                        "mappingId", request.mappingId()
                )
        );
    }

    public record ApprovalRequest(
            String mappingId,
            String sourceDept,
            String targetDept
    ) {
    }
}
