package com.govmesh.backend.transformation;

import com.govmesh.backend.contract.ApprovedMapping;
import com.govmesh.backend.contract.TransformationContract;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransformationService {

    public Map<String, Object> transform(
            Map<String, Object> sourceData,
            TransformationContract contract) {

        Map<String, Object> transformedData = new HashMap<>();

        if (sourceData == null) {
            throw new IllegalArgumentException(
                    "Source data cannot be null"
            );
        }

        if (contract == null) {
            throw new IllegalArgumentException(
                    "Transformation contract cannot be null"
            );
        }

        List<ApprovedMapping> mappings =
                contract.getApprovedMappings();

        if (mappings == null || mappings.isEmpty()) {
            throw new IllegalArgumentException(
                    "Transformation contract contains no approved mappings"
            );
        }

        for (ApprovedMapping mapping : mappings) {

            String sourceField = mapping.source();
            String targetField = mapping.target();

            if (sourceData.containsKey(sourceField)) {

                transformedData.put(
                        targetField,
                        sourceData.get(sourceField)
                );
            }
        }

        return transformedData;
    }
}