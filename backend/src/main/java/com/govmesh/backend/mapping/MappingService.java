package com.govmesh.backend.mapping;

import com.govmesh.backend.schema.Schema;
import com.govmesh.backend.schema.SchemaService;
import tools.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class MappingService {

    private final SchemaService schemaService;
    private final SemanticMapperClient semanticMapperClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MappingService(SchemaService schemaService, SemanticMapperClient semanticMapperClient) {
        this.schemaService = schemaService;
        this.semanticMapperClient = semanticMapperClient;
    }

    public SemanticMapResponse discoverMapping(Long sourceDeptId, Long targetDeptId) {
        Schema sourceSchema = schemaService.getSchemaByDepartmentId(sourceDeptId);
        Schema targetSchema = schemaService.getSchemaByDepartmentId(targetDeptId);

        Map<String, Object> sourceFields = parseFields(sourceSchema.getFieldsJson());
        Map<String, Object> targetFields = parseFields(targetSchema.getFieldsJson());

        return semanticMapperClient.getMappingSuggestions(sourceFields, targetFields);
    }

    private Map<String, Object> parseFields(String fieldsJson) {
        try {
            return objectMapper.readValue(fieldsJson, Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse stored schema fields", e);
        }
    }
}