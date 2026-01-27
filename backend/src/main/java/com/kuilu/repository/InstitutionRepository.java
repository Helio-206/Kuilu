package com.kuilu.repository;

import com.kuilu.model.Institution;
import org.springframework.data.r2dbc.repository.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InstitutionRepository extends ReactiveCrudRepository<Institution, UUID> {
}
