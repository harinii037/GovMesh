package com.govmesh.backend.governance;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConsentPolicyRepository extends JpaRepository<ConsentPolicy, Long> {
    Optional<ConsentPolicy> findBySourceDeptAndTargetDeptAndDataCategory(
            String sourceDept, String targetDept, String dataCategory);
}
