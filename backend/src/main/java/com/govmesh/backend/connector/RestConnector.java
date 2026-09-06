package com.govmesh.backend.connector;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class RestConnector implements Connector {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public RestConnector(String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Override
    public Map<String, Object> fetchData(String ref) {

        String url = baseUrl + "/citizen/" + ref;

        ResponseEntity<Map> response =
                restTemplate.getForEntity(url, Map.class);

        return response.getBody();
    }

    @Override
    public Map<String, Object> sendData(Map<String, Object> data) {
        throw new UnsupportedOperationException(
                "sendData() is not implemented yet"
        );
    }
}