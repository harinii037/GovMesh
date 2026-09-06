package com.govmesh.backend.transaction;

public enum TransactionStatus {
    PENDING, TRANSFORMING, GOVERNANCE_CHECK, SENDING, RETRY_PENDING, SUCCESS, FAILED, DENIED
}