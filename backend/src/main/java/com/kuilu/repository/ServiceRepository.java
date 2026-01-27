package com.kuilu.repository;

import com.kuilu.model.Service;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface ServiceRepository extends ReactiveCrudRepository<Service, UUID> {

    @Query("SELECT * FROM services WHERE institution_id = :institutionId")
    Flux<Service> findByInstitutionId(UUID institutionId);
}
