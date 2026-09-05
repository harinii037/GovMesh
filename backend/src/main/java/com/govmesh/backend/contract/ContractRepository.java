package com.govmesh.backend.contract;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository
        extends JpaRepository<TransformationContract, Long> {
}