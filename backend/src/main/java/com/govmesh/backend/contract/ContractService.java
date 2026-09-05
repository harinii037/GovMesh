package com.govmesh.backend.contract;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    public TransformationContract createContract(
            Long sourceDeptId,
            Long targetDeptId,
            List<ApprovedMapping> approvedMappings) {

        String mappingsJson =
                TransformationContract.mappingsToJson(
                        approvedMappings
                );

        TransformationContract contract =
                new TransformationContract(
                        sourceDeptId,
                        targetDeptId,
                        "APPROVED",
                        mappingsJson
                );

        // Keep mappings available immediately in memory
        contract.setApprovedMappings(approvedMappings);

        return contractRepository.save(contract);
    }

    public TransformationContract getContract(Long contractId) {

        return contractRepository.findById(contractId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Contract not found with id: " + contractId
                        )
                );
    }
}