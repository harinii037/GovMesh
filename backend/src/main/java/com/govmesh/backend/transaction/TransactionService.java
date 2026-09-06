package com.govmesh.backend.transaction;

import com.govmesh.backend.connector.Connector;
import com.govmesh.backend.connector.LegacyConnector;
import com.govmesh.backend.connector.RestConnector;
import com.govmesh.backend.contract.ContractService;
import com.govmesh.backend.contract.TransformationContract;
import com.govmesh.backend.department.Department;
import com.govmesh.backend.department.DepartmentRepository;
import com.govmesh.backend.transformation.TransformationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final DepartmentRepository departmentRepository;
    private final ContractService contractService;
    private final TransformationService transformationService;

    public TransactionService(TransactionRepository transactionRepository,
                               DepartmentRepository departmentRepository,
                               ContractService contractService,
                               TransformationService transformationService) {
        this.transactionRepository = transactionRepository;
        this.departmentRepository = departmentRepository;
        this.contractService = contractService;
        this.transformationService = transformationService;
    }

    public Transaction executeTransaction(Long contractId, String sourceRef) {
        TransformationContract contract = contractService.getContract(contractId);

        Department sourceDept = departmentRepository.findById(contract.getSourceDeptId())
                .orElseThrow(() -> new RuntimeException("Source department not found: " + contract.getSourceDeptId()));
        Department targetDept = departmentRepository.findById(contract.getTargetDeptId())
                .orElseThrow(() -> new RuntimeException("Target department not found: " + contract.getTargetDeptId()));

        Transaction transaction = new Transaction(sourceDept, targetDept, contractId.toString());
        transaction = transactionRepository.save(transaction);

        try {
            transaction.setStatus(TransactionStatus.TRANSFORMING);
            transactionRepository.save(transaction);

            Connector sourceConnector = buildConnector(sourceDept);
            Map<String, Object> sourceData = sourceConnector.fetchData(sourceRef);

            Map<String, Object> transformedData = transformationService.transform(sourceData, contract);

            transaction.setStatus(TransactionStatus.SENDING);
            transactionRepository.save(transaction);

            Connector targetConnector = buildConnector(targetDept);
            targetConnector.sendData(transformedData);

            transaction.setStatus(TransactionStatus.SUCCESS);

        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            throw new RuntimeException("Transaction failed: " + e.getMessage(), e);
        } finally {
            transactionRepository.save(transaction);
        }

        return transaction;
    }

    private Connector buildConnector(Department department) {
        return switch (department.getType()) {
            case REST -> new RestConnector(department.getBaseUrl());
            case LEGACY -> new LegacyConnector(department.getBaseUrl());
        };
    }
}