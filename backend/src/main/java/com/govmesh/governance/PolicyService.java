package com.govmesh.governance;

import org.springframework.stereotype.Service;

@Service
public class PolicyService {

    private final ConsentPolicyRepository policyRepository;
    private final AuditService auditService;

    public PolicyService(ConsentPolicyRepository policyRepository,
                         AuditService auditService) {
        this.policyRepository = policyRepository;
        this.auditService = auditService;
    }

    public boolean checkConsent(String sourceDept,
                                String targetDept,
                                String dataCategory) {
        return checkConsentDetailed(sourceDept, targetDept, dataCategory).allowed();
    }

    public PolicyDecision checkConsentDetailed(String sourceDept,
                                                String targetDept,
                                                String dataCategory) {

        ConsentPolicy policy = policyRepository
                .findBySourceDeptAndTargetDeptAndDataCategory(
                        sourceDept, targetDept, dataCategory)
                .orElse(null);

        String route = sourceDept + " -> " + targetDept
                + " [" + dataCategory + "]";

        if (policy == null) {
            String reason = "No approved policy exists for " + route;

            auditService.log(
                    "SYSTEM",
                    "CONSENT_CHECKED",
                    reason,
                    "DENIED"
            );

            return new PolicyDecision(false, reason);
        }

        if (!policy.isAllowed()) {
            String reason = "Policy explicitly denies " + route;

            auditService.log(
                    "SYSTEM",
                    "CONSENT_CHECKED",
                    reason,
                    "DENIED"
            );

            return new PolicyDecision(false, reason);
        }

        String reason = "Approved policy allows " + route;

        auditService.log(
                "SYSTEM",
                "CONSENT_CHECKED",
                reason,
                "ALLOWED"
        );

        return new PolicyDecision(true, reason);
    }
}
