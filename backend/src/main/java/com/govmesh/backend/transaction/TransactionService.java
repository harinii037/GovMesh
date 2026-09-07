package com.govmesh.backend.transaction;

import com.govmesh.backend.connector.Connector;
import com.govmesh.backend.connector.LegacyConnector;
import com.govmesh.backend.connector.RestConnector;
import com.govmesh.backend.contract.ContractService;
import com.govmesh.backend.contract.TransformationContract;
import com.govmesh.backend.department.Department;
import com.govmesh.backend.department.DepartmentRepository;
import com.govmesh.backend.governance.PolicyDecision;
import com.govmesh.backend.governance.PolicyService;
import com.govmesh.backend.transformation.TransformationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final DepartmentRepository departmentRepository;
    private final ContractService contractService;
    private final TransformationService transformationService;
    private final PolicyService policyService;

    public TransactionService(TransactionRepository transactionRepository,
                               DepartmentRepository departmentRepository,
                               ContractService contractService,
                               TransformationService transformationService,
                               PolicyService policyService) {
        this.transactionRepository = transactionRepository;
        this.departmentRepository = departmentRepository;
        this.contractService = contractService;
        this.transformationService = transformationService;
        this.policyService = policyService;
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
            // --- GOVERNANCE CHECK (new) ---
            transaction.setStatus(TransactionStatus.GOVERNANCE_CHECK);
            transactionRepository.save(transaction);

            PolicyDecision decision = policyService.checkConsentDetailed(
                    sourceDept.getName().toLowerCase(),
                    targetDept.getName().toLowerCase(),
                    "employment-data" // hardcoded for MVP — only category M5 seeded
            );

            if (!decision.allowed()) {
                transaction.setStatus(TransactionStatus.DENIED);
                transactionRepository.save(transaction);
                return transaction; // not an error — a valid denied outcome
            }
            // --- end governance check ---

            transaction.setStatus(TransactionStatus.TRANSFORMING);
            transactionRepository.save(transaction);

            Connector sourceConnector = buildConnector(sourceDept);
            Map<String, Object> sourceData = sourceConnector.fetchData(sourceRef);

            Map<String, Object> transformedData = transformationService.transform(sourceData, contract);

            Connector targetConnector = buildConnector(targetDept);

            int maxAttempts = 3;
            int attempt = 0;
            boolean sent = false;
            Exception lastError = null;

            while (attempt < maxAttempts && !sent) {
                attempt++;
                try {
                    transaction.setStatus(attempt == 1 ? TransactionStatus.SENDING : TransactionStatus.RETRY_PENDING);
                    transaction.setAttemptCount(attempt);
                    transactionRepository.save(transaction);

                    targetConnector.sendData(transformedData);
                    sent = true;

                } catch (Exception e) {
                    lastError = e;
                    if (attempt < maxAttempts) {
                        Thread.sleep(2000);
                    }
                }
            }

            if (!sent) {
                throw new RuntimeException("Target unreachable after " + maxAttempts + " attempts", lastError);
            }

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