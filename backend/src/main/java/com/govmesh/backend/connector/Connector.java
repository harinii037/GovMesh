package com.govmesh.backend.connector;

import java.util.Map;

public interface Connector {

    Map<String, Object> fetchData(String ref);

    Map<String, Object> sendData(Map<String, Object> data);
}