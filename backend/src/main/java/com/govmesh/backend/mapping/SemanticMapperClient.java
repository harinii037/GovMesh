package com.govmesh.backend.mapping;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class SemanticMapperClient {

    private final RestClient restClient;

    public SemanticMapperClient(@Value("${semantic-mapper.url}") String semanticMapperUrl) {
        this.restClient = RestClient.create(semanticMapperUrl);
    }

    public SemanticMapResponse getMappingSuggestions(Map<String, Object> sourceSchema, Map<String, Object> targetSchema) {
        Map<String, Object> requestBody = Map.of(
                "sourceSchema", sourceSchema,
                "targetSchema", targetSchema
        );

        return restClient.post()
                .uri("/semantic-map")
                .body(requestBody)
                .retrieve()
                .body(SemanticMapResponse.class);
    }
}