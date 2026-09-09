package com.fieldflow.assets.persistense;

import com.fieldflow.assets.domain.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, UUID> {

	@Query("""
			    SELECT e
			    FROM Equipment e
			    JOIN FETCH e.site s
			    JOIN FETCH s.client c
			    LEFT JOIN FETCH e.installation i
			    WHERE (:siteId IS NULL OR s.id = :siteId)
			      AND (:clientId IS NULL OR c.id = :clientId)
			""")
	List<Equipment> findAllWithContext(@Param("siteId") UUID siteId, @Param("clientId") UUID clientId);
}
