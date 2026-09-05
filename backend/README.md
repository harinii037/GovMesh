# GovMesh M5 — Day 1 + Day 2

This package implements the M5 Security/Governance work from the team's Day 1 and Day 2 plan.

## Day 1
- User + Role entities
- 3 seeded users
- Password hashing
- JWT generation
- POST /auth/login
- AuditLog + repository + service
- PostgreSQL persistence

## Day 2
- JWT authentication filter
- Spring Security integration
- /auth/login remains public
- GET /departments remains public
- POST /transactions protected
- POST /mappings/approve protected
- RBAC with ADMIN and department officers
- ConsentPolicy entity/repository
- PolicyService.checkConsent(sourceDept, targetDept, dataCategory)
- Seeded policies:
  employment -> welfare = ALLOWED
  welfare -> employment = DENIED
- Denied policy creates an audit entry and returns DENIED
- Temporary integration/test controllers demonstrate the security and governance flow

## Important integration note

Your team's M1 transaction, mapping, department, contract, transformation and connector code is not included here because it was not provided in this package.

The M5 services are deliberately exposed as reusable services:
- PolicyService.checkConsent(...)
- AuditService.log(...)

M1 should call these from its real orchestration:
1. authenticate / authorize
2. check consent
3. if denied: transaction status DENIED + audit
4. otherwise continue
5. call AuditService.log(...) at meaningful stages

The temporary /test endpoints exist only to prove M5 Day 2 works before M1 wiring is merged. Remove or restrict them for the final SIH build.

## PostgreSQL

Create the database first:

CREATE DATABASE govmesh;

Then update src/main/resources/application.properties with the actual PostgreSQL username/password.

## Run

mvn spring-boot:run

## Test users

emp1 / emp123 -> EMPLOYMENT_OFFICER
wel1 / wel123 -> WELFARE_OFFICER
admin / admin123 -> ADMIN

## Login

POST http://localhost:8080/auth/login

{
  "username": "emp1",
  "password": "emp123"
}

Copy the returned token.

## Day 2 protected tests

POST /transactions

Header:
Authorization: Bearer <token>

Body:
{
  "sourceDept": "employment",
  "targetDept": "welfare",
  "dataCategory": "employment-data"
}

POST /mappings/approve

Header:
Authorization: Bearer <token>

Body:
{
  "sourceDept": "employment",
  "targetDept": "welfare",
  "dataCategory": "employment-data"
}

A request without a token should return 401.

## RBAC examples

EMPLOYMENT_OFFICER can act when sourceDept=employment.

WELFARE_OFFICER can act when sourceDept=welfare.

ADMIN can act for any department.

The authorization rule is implemented in DepartmentAuthorizationService and is used by the protected test controllers.

## Consent examples

employment -> welfare -> ALLOWED
welfare -> employment -> DENIED

For a denied transaction:
- response contains status DENIED
- AuditService.log(..., "CONSENT_CHECKED", ..., "DENIED") is called

## Audit

POST /test/audit creates a test audit record.
GET /test/audit returns records.

GET /test/policies returns seeded policies.

## Day 2 audit integration

M1 should call:

auditService.log(actor, "SCHEMA_DISCOVERED", detail, "SUCCESS");
auditService.log(actor, "MAPPING_APPROVED", detail, "SUCCESS");
auditService.log(actor, "CONTRACT_CREATED", detail, "SUCCESS");
auditService.log(actor, "CONSENT_CHECKED", detail, "SUCCESS");
auditService.log(actor, "TRANSFORMATION_EXECUTED", detail, "SUCCESS");
auditService.log(actor, "TARGET_INVOKED", detail, "SUCCESS");
auditService.log(actor, "TRANSACTION_COMPLETED", detail, "SUCCESS");
auditService.log(actor, "TRANSACTION_DENIED", detail, "DENIED");

The exact actor/detail values should be populated by M1's real orchestration context.

## Security note

The JWT secret in application.properties is development-only. Move it to an environment variable/secret manager before deployment.
