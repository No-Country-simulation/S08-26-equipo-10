package com.fieldflow.workorders.api.dto;

import java.util.UUID;

public class ServiceTypeResponse {
    private UUID id;
    private String name;

    public ServiceTypeResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}