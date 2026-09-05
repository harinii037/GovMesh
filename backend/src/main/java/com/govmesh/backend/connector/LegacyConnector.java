package com.govmesh.backend.connector;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class LegacyConnector implements Connector {

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public LegacyConnector(String baseUrl) {
        this.restTemplate = new RestTemplate();
        this.baseUrl = baseUrl;
    }

    @Override
    public Map<String, Object> fetchData(String ref) {

        String url = baseUrl + "/export";

        ResponseEntity<String> response =
                restTemplate.getForEntity(url, String.class);

        String rawData = response.getBody();

        String[] fields = rawData.split("\\|");

        Map<String, Object> data = new HashMap<>();

        data.put("CITIZEN_ID", fields[0]);
        data.put("NAME", fields[1]);
        data.put("EMPLOYMENT", fields[2]);
        data.put("INCOME", Double.parseDouble(fields[3]));

        return data;
    }

    @Override
    public Map<String, Object> sendData(Map<String, Object> data) {
        throw new UnsupportedOperationException(
                "sendData() is not implemented yet"
        );
    }
}