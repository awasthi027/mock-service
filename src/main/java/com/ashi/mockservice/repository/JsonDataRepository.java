package com.ashi.mockservice.repository;

import com.ashi.mockservice.model.JsonData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JsonDataRepository extends JpaRepository<JsonData, UUID> {
}

