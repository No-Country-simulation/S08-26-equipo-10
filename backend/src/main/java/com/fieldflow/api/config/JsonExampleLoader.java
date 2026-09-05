package com.fieldflow.api.config;

import com.fasterxml.jackson.databind.JsonNode;

public interface JsonExampleLoader {

	JsonNode load(String path);
}
