package com.fieldflow.workorders.api;

import com.fieldflow.workorders.api.dto.ServiceTypeResponse;
import com.fieldflow.workorders.application.ServiceTypeService;
import com.fieldflow.workorders.domain.ServiceType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/service-types")
public class ServiceTypeController {

    private final ServiceTypeService service;

    public ServiceTypeController(ServiceTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponse>> getAllServiceTypes() {
        List<ServiceType> types = service.getAllServiceTypes();
        
        List<ServiceTypeResponse> response = types.stream()
                .map(t -> new ServiceTypeResponse(t.getId(), t.getName()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}