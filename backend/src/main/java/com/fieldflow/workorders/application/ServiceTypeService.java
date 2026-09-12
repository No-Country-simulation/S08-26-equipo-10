package com.fieldflow.workorders.application;

import java.util.List;
import com.fieldflow.workorders.api.dto.ServiceTypeResponse;

public interface ServiceTypeService {
    List<ServiceTypeResponse> getAllServiceTypes();
}