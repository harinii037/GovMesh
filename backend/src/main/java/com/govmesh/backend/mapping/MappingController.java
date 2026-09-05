package com.govmesh.backend.mapping;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping")
public class MappingController {

    private final MappingService mappingService;

    public MappingController(MappingService mappingService) {
        this.mappingService = mappingService;
    }

    @PostMapping("/discover")
    public ResponseEntity<SemanticMapResponse> discoverMapping(
            @RequestParam Long sourceDeptId,
            @RequestParam Long targetDeptId) {

        return ResponseEntity.ok(mappingService.discoverMapping(sourceDeptId, targetDeptId));
    }
}