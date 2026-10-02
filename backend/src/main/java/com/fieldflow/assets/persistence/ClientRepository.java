package com.fieldflow.assets.persistence;

import com.fieldflow.assets.api.dto.ClientListItemResponse;
import com.fieldflow.assets.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

	@Query("""
				SELECT new com.fieldflow.assets.api.dto.ClientListItemResponse(
			    	c.id,
			    	c.name,
			    	COUNT(DISTINCT s.id),
			    	COUNT(DISTINCT e.id)
				)
				FROM Client c
				LEFT JOIN c.sites s
				LEFT JOIN s.equipments e
				GROUP BY c.id, c.name
				ORDER BY c.name ASC
			""")
	List<ClientListItemResponse> findAllWithSummary();
}
