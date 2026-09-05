package com.govmesh.backend.schema;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schemas")
public class SchemaController {

    private final SchemaService schemaService;

    public SchemaController(SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @PostMapping("/{departmentId}")
    public ResponseEntity<Schema> registerSchema(
            @PathVariable Long departmentId,
            @RequestBody SchemaRequest request) {

        Schema schema = schemaService.registerSchema(
                departmentId,
                request.fields()
        );

        return ResponseEntity.ok(schema);
    }

    @GetMapping("/{departmentId}")
    public ResponseEntity<Schema> getSchema(
            @PathVariable Long departmentId) {

        return ResponseEntity.ok(
                schemaService.getSchemaByDepartmentId(departmentId)
        );
    }
}