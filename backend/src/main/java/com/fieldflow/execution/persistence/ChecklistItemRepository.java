package com.fieldflow.execution.persistence;

import com.fieldflow.execution.domain.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, UUID> {

	@Query("""
			SELECT item
			FROM ChecklistItem item
			JOIN item.checklist checklist
			WHERE checklist.workOrder.id = :workOrderId
			  AND item.id IN :itemIds
			""")
	List<ChecklistItem> findAllForWorkOrder(@Param("workOrderId") UUID workOrderId,
	                                        @Param("itemIds") Collection<UUID> itemIds);
}
