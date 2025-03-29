package org.ptithcm2021.hr_management.repository;

import org.ptithcm2021.hr_management.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, String> {
    boolean existsByName(String name);
}
