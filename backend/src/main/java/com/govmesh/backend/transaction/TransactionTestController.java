package com.govmesh.backend.transaction;

import com.govmesh.backend.governance.AuditService;
import com.govmesh.backend.governance.PolicyDecision;
import com.govmesh.backend.governance.PolicyService;
import com.govmesh.backend.security.DepartmentAuthorizationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionTestController {

    private final DepartmentAuthorizationService authorizationService;
    private final PolicyService policyService;
    private final AuditService auditService;

    public TransactionTestController(
            DepartmentAuthorizationService authorizationService,
            PolicyService policyService,
            AuditService auditService) {
        this.authorizationService = authorizationService;
        this.policyService = policyService;
        this.auditService = auditService;
    }

    @PostMapping("/test")
    public ResponseEntity<?> testTransaction(
            @RequestBody TransactionRequest request,
            Authentication authentication) {

        String actor = authentication.getName();

        if (!authorizationService.canActForSourceDepartment(
                authentication, request.sourceDept())) {

            auditService.log(
                    actor,
                    "TRANSACTION_DENIED",
                    "RBAC denied: actor cannot act for source department "
                            + request.sourceDept(),
                    "DENIED"
            );

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of(
                            "status", TransactionStatus.DENIED.name(),
                            "reason", "RBAC: actor cannot act for source department "
                                    + request.sourceDept()
                    )
            );
        }

        PolicyDecision decision = policyService.checkConsentDetailed(
                request.sourceDept(),
                request.targetDept(),
                request.dataCategory()
        );

        if (!decision.allowed()) {

            auditService.log(
                    actor,
                    "TRANSACTION_DENIED",
                    decision.reason(),
                    "DENIED"
            );

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    Map.of(
                            "status", TransactionStatus.DENIED.name(),
                            "reason", decision.reason()
                    )
            );
        }

        auditService.log(
                actor,
                "TRANSACTION_COMPLETED",
                "Governance checks passed for "
                        + request.sourceDept() + " -> " + request.targetDept(),
                "SUCCESS"
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", TransactionStatus.COMPLETED.name(),
                        "consent", "ALLOWED",
                        "message", "Governance checks passed"
                )
        );
    }

    public record TransactionRequest(
            String sourceDept,
            String targetDept,
            String dataCategory
    ) {
    }
}
