package com.govmesh.backend.transaction;

public enum TransactionStatus {
    PENDING,
    COMPLETED,
    DENIED,
    FAILED,
    RETRY_PENDING
}
