package com.govmesh.backend.contract;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transformation_contract")
public class TransformationContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contractId;

    @Column(nullable = false)
    private Long sourceDeptId;

    @Column(nullable = false)
    private Long targetDeptId;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String mappingsJson;

    @Transient
    private List<ApprovedMapping> approvedMappings = new ArrayList<>();

    public TransformationContract() {
    }

    public TransformationContract(
            Long sourceDeptId,
            Long targetDeptId,
            String status,
            String mappingsJson) {

        this.sourceDeptId = sourceDeptId;
        this.targetDeptId = targetDeptId;
        this.status = status;
        this.mappingsJson = mappingsJson;
    }

    public Long getContractId() {
        return contractId;
    }

    public Long getSourceDeptId() {
        return sourceDeptId;
    }

    public Long getTargetDeptId() {
        return targetDeptId;
    }

    public String getStatus() {
        return status;
    }

    public String getMappingsJson() {
        return mappingsJson;
    }

    public List<ApprovedMapping> getApprovedMappings() {
        return approvedMappings;
    }

    public void setApprovedMappings(List<ApprovedMapping> approvedMappings) {
        this.approvedMappings = approvedMappings;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMappingsJson(String mappingsJson) {
        this.mappingsJson = mappingsJson;
    }

    /*
     * Convert approved mappings into JSON for database storage.
     */
    public static String mappingsToJson(List<ApprovedMapping> mappings) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < mappings.size(); i++) {

            ApprovedMapping mapping = mappings.get(i);

            json.append("{")
                    .append("\"source\":\"")
                    .append(escapeJson(mapping.source()))
                    .append("\",")
                    .append("\"target\":\"")
                    .append(escapeJson(mapping.target()))
                    .append("\"")
                    .append("}");

            if (i < mappings.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    /*
     * Convert JSON stored in the database back into ApprovedMapping objects.
     */
    public static List<ApprovedMapping> jsonToMappings(String json) {

        List<ApprovedMapping> mappings = new ArrayList<>();

        if (json == null || json.isBlank()) {
            return mappings;
        }

        String cleaned = json.trim();

        if (cleaned.equals("[]")) {
            return mappings;
        }

        // Remove [ and ]
        cleaned = cleaned.substring(1, cleaned.length() - 1);

        // Split individual mapping objects
        String[] objects = cleaned.split("\\},\\{");

        for (String object : objects) {

            object = object
                    .replace("{", "")
                    .replace("}", "")
                    .replace("\"", "");

            String[] fields = object.split(",");

            String source = null;
            String target = null;

            for (String field : fields) {

                String[] keyValue = field.split(":", 2);

                if (keyValue.length != 2) {
                    continue;
                }

                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                if (key.equals("source")) {
                    source = value;
                }

                if (key.equals("target")) {
                    target = value;
                }
            }

            if (source != null && target != null) {
                mappings.add(
                        new ApprovedMapping(source, target)
                );
            }
        }

        return mappings;
    }

    private static String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }

    /*
     * Whenever JPA loads a TransformationContract from PostgreSQL,
     * rebuild the transient approvedMappings list.
     */
    @PostLoad
    private void loadApprovedMappings() {

        this.approvedMappings =
                jsonToMappings(this.mappingsJson);
    }
}