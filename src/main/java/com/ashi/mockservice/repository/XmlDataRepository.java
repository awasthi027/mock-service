package com.ashi.mockservice.repository;

import com.ashi.mockservice.model.XmlData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface XmlDataRepository extends JpaRepository<XmlData, UUID> {
}

