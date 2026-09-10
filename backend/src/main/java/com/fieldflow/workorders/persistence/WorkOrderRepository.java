package com.fieldflow.workorders.persistence;

import com.fieldflow.workorders.domain.WorkOrder;
import com.fieldflow.workorders.domain.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, UUID> {

	@Query("""
			    SELECT wo
			    FROM WorkOrder wo
			    JOIN FETCH wo.equipment e
			    JOIN FETCH wo.serviceType st
			    WHERE (:status IS NULL OR wo.status = :status)
			      AND (:equipmentId IS NULL OR e.id = :equipmentId)
			""")
	List<WorkOrder> findAllWithContext(@Param("status") WorkOrderStatus status, @Param("equipmentId") UUID equipmentId);
}
