package com.govmesh.backend.contract;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ContractService {

    public TransformationContract createContract(Long sourceDeptId, Long targetDeptId, List<ApprovedMapping> approvedMappings) {
        // TEMPORARY STUB — replace with M2's real implementation on merge
        String fakeId = "CONTRACT_" + sourceDeptId + "_" + targetDeptId;
        return new TransformationContract(fakeId, "APPROVED");
    }
}