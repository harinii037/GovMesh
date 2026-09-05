# GovMesh M5 — Final Day 3 Package

This package completes the M5 Security/Governance Day 3 work:
1. Explicit DENIED consent path with a human-readable reason.
2. RBAC verification: an authenticated officer cannot act for another source department.
3. Audit trail for consent denial and transaction denial.
4. Read-only audit endpoint for M4/demo verification.
5. Demo endpoints so M5 can be tested independently until M1 wires the real TransactionService/MappingController.

IMPORTANT:
- This is an M5 verification/integration package, not a claim that the real M1 services are integrated.
- Do NOT replace M1's real TransactionService or MappingController with these demo controllers.
- When M1 integration is ready, call PolicyService and AuditService from the real orchestration flow and remove/disable the demo controllers.
- Cross-department exchange remains valid when the SOURCE department is the officer's department and policy allows the source→target flow.
- 401 = missing/invalid authentication. 403 = authenticated but not authorized.

## Files added/changed for Day 3

### governance/
- PolicyDecision.java
- PolicyService.java
- AuditController.java

### transaction/
- TransactionStatus.java
- TransactionTestController.java

### mapping/
- MappingApprovalTestController.java

### security/
- DepartmentAuthorizationService.java

## Required endpoints

POST /auth/login
GET  /departments
POST /transactions/test
POST /mappings/approve/test
GET  /audit

## Test users

emp1 / emp123       EMPLOYMENT_OFFICER
welfare1 / welfare123   WELFARE_OFFICER
admin / admin123    ADMIN

## Day 3 tests

### A. Consent DENIED path

Login as welfare1:

POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "welfare1",
  "password": "welfare123"
}

Copy the returned token.

Then:

POST http://localhost:8080/transactions/test
Authorization: Bearer <TOKEN>
Content-Type: application/json

{
  "sourceDept": "welfare",
  "targetDept": "employment",
  "dataCategory": "employment-data"
}

Expected:
- HTTP 403
- status = DENIED
- reason explains that no approved policy exists OR that the policy explicitly denies the request.
- audit entries are created.

The seeded policy intentionally allows:
employment -> welfare -> employment-data

and denies:
welfare -> employment -> employment-data

### B. RBAC verification

Login as emp1.

Try to approve a mapping whose sourceDept is welfare:

POST http://localhost:8080/mappings/approve/test
Authorization: Bearer <EMP_TOKEN>
Content-Type: application/json

{
  "mappingId": "map-001",
  "sourceDept": "welfare",
  "targetDept": "employment"
}

Expected:
HTTP 403
reason: EMPLOYMENT_OFFICER cannot act for source department welfare.

Now use employment as source:

{
  "mappingId": "map-002",
  "sourceDept": "employment",
  "targetDept": "welfare"
}

Expected:
HTTP 200.

### C. Allowed consent path

Login as emp1 and call:

POST http://localhost:8080/transactions/test
Authorization: Bearer <EMP_TOKEN>
Content-Type: application/json

{
  "sourceDept": "employment",
  "targetDept": "welfare",
  "dataCategory": "employment-data"
}

Expected:
HTTP 200
status = COMPLETED
consent = ALLOWED.

### D. Audit verification

GET http://localhost:8080/audit

Expected to see chronological records including:
- MAPPING_APPROVED
- CONSENT_CHECKED
- TRANSACTION_COMPLETED
- CONSENT_CHECKED / DENIED
- TRANSACTION_DENIED

PostgreSQL:
SELECT * FROM audit_logs ORDER BY timestamp DESC;

## Day 3 judge explanation

Authentication:
"JWT proves who is calling the API."

Authorization:
"RBAC decides whether that authenticated role is allowed to perform the operation."

Consent:
"Even an authorized officer cannot exchange data when the source→target data category has no approved policy."

Audit:
"Every meaningful governance decision is persisted with actor, action, detail, result and timestamp."

Security result:
- 401 → identity/authentication problem.
- 403 → authenticated user lacks permission OR governance policy denies the transaction.

## Final integration with M1

M1 should call approximately:

boolean allowed = policyService.checkConsent(sourceDept, targetDept, dataCategory);

if (!allowed) {
    auditService.log(actor, "TRANSACTION_DENIED",
        "No approved policy for " + sourceDept + " -> " + targetDept,
        "DENIED");
    // set Transaction status DENIED and stop orchestration
}

For successful flow, audit at:
Schema discovered
Mapping approved
Contract created
Consent checked
Transformation executed
Target invoked
Transaction completed

Do not create duplicate audit entries if M1 already logs the same event.
