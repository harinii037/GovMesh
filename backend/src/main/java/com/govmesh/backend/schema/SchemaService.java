package com.govmesh.backend.schema;

import com.govmesh.backend.department.Department;
import com.govmesh.backend.department.DepartmentRepository;
import tools.jackson.databind.ObjectMapper;import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class SchemaService {

    private final SchemaRepository schemaRepository;
    private final DepartmentRepository departmentRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public SchemaService(SchemaRepository schemaRepository, DepartmentRepository departmentRepository) {
        this.schemaRepository = schemaRepository;
        this.departmentRepository = departmentRepository;
    }

    public Schema registerSchema(Long departmentId, Object fields) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with id: " + departmentId));

        if (fields == null || (fields instanceof Map && ((Map<?, ?>) fields).isEmpty())) {
            throw new RuntimeException("Schema must contain at least one field");
        }

        String fieldsJson;
        try {
            fieldsJson = objectMapper.writeValueAsString(fields);
        } catch (Exception e) {
            throw new RuntimeException("Invalid schema fields format", e);
        }

        // If this department already has a schema, update it instead of creating a duplicate.
        Schema schema = schemaRepository.findByDepartmentId(departmentId)
                .orElse(new Schema());
        schema.setDepartment(department);
        schema.setFieldsJson(fieldsJson);

        return schemaRepository.save(schema);
    }

    public Schema getSchemaByDepartmentId(Long departmentId) {
        return schemaRepository.findByDepartmentId(departmentId)
                .orElseThrow(() -> new RuntimeException("No schema registered for department id: " + departmentId));
    }
}