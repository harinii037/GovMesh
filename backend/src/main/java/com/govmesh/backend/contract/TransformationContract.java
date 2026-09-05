package com.govmesh.backend.contract;

public class TransformationContract {
    private String contractId;
    private String status;

    public TransformationContract() {}
    public TransformationContract(String contractId, String status) {
        this.contractId = contractId;
        this.status = status;
    }
    public String getContractId() { return contractId; }
    public String getStatus() { return status; }
}