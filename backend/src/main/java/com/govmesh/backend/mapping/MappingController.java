package com.govmesh.backend.mapping;

import com.govmesh.backend.contract.ApprovedMapping;
import com.govmesh.backend.contract.ContractService;
import com.govmesh.backend.contract.TransformationContract;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mapping")
public class MappingController {

    private final MappingService mappingService;
    private final ContractService contractService;

    public MappingController(MappingService mappingService, ContractService contractService) {
        this.mappingService = mappingService;
        this.contractService = contractService;
    }

    @PostMapping("/discover")
    public ResponseEntity<SemanticMapResponse> discoverMapping(
            @RequestParam Long sourceDeptId,
            @RequestParam Long targetDeptId) {

        return ResponseEntity.ok(mappingService.discoverMapping(sourceDeptId, targetDeptId));
    }

    @PostMapping("/approve")
    public ResponseEntity<TransformationContract> approveMapping(
            @RequestParam Long sourceDeptId,
            @RequestParam Long targetDeptId,
            @RequestBody List<ApprovedMapping> approvedMappings) {

        TransformationContract contract = contractService.createContract(sourceDeptId, targetDeptId, approvedMappings);
        return ResponseEntity.ok(contract);
    }
}