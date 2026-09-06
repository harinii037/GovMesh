package com.govmesh.backend.transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final TransactionService transactionService;

    public TransactionController(TransactionRepository transactionRepository,
                                  TransactionService transactionService) {
        this.transactionRepository = transactionRepository;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        return transactionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Transaction not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(
            @RequestParam Long contractId,
            @RequestParam String sourceRef) {

        return ResponseEntity.ok(transactionService.executeTransaction(contractId, sourceRef));
    }
}