# Hospital Management System – Engineering Audit (Continuation)

**Audit date:** 2026-11-21  
**Scope:** Continue the initial static audit and implement safe code-level hardening.  
**Runtime verification status:** BLOCKED. Maven is not installed and MySQL is not installed in the audit environment. No blocked test is marked passed.

## Environment — exact checks
- Java 21: **PASS / AVAILABLE** (`java -version` returned OpenJDK 21.0.12.1).
- Maven: **BLOCKED** (`mvn -version` → `mvn: command not found`).
- MySQL: **BLOCKED** (no server/client available in this environment; no connection test executed).
- Node syntax: **PASS** (`node --check src/main/resources/static/js/app.js` exited successfully).
- POM XML: **PASS** (parsed by Python XML parser).
- Postman collection JSON: **PASS** (parsed by Python JSON parser).
- Java compile: **BLOCKED** (`javac` attempted, but Spring/Jakarta/Maven dependencies are absent; output contains unresolved packages/symbols). This is not a successful compilation.
- JUnit/Mockito tests: **CREATED, NOT EXECUTED** because Maven/dependencies are unavailable.
- REST integration tests: **CREATED, NOT EXECUTED**; isolated H2 test configuration added, but dependencies/runtime are unavailable.
- Browser/E2E: **BLOCKED** (no application runtime/browser verification).

## Engineering changes made

### 1. Appointments
- Required patient, doctor, date, and time validation added.
- Doctor and patient references are checked for existence.
- Status values are restricted to Scheduled, Confirmed, Completed, Cancelled/Canceled.
- Exact doctor/date/time and patient/date/time conflict checks added; cancelled and completed records do not block a slot.
- `@Transactional` create/update plus pessimistic write locks on the doctor and patient rows serialize competing requests before conflict checks.
- Updates exclude the current appointment ID from collision checks and force the URL ID.
- New records ignore client-supplied IDs.
- **Known model limitation:** Appointment has no duration field. Conflict prevention currently detects identical start-time slots only; it cannot detect interval overlaps such as a 10:00 30-minute appointment conflicting with a 10:15 appointment.
- A manual MySQL migration script includes generated unique active doctor/patient slot keys as an additional database-level safeguard. It is opt-in and must be reviewed against existing data before running.

### 2. Room allocation and release
- Allocation validates positive identifiers/bed number, room and patient existence, bed number within room capacity, active-bed uniqueness, and one active allocation per patient.
- Room row pessimistic locking serializes allocations for the same room; patient row locking serializes simultaneous allocation attempts for the same patient across rooms.
- `availableBeds` is recalculated from active allocations after create/release rather than incremented/decremented blindly.
- Release date cannot precede admission date.
- Allocation identity fields cannot be changed through update; update is restricted to release date. Released allocations cannot be reactivated.
- Active allocations cannot be deleted; released allocation history is retained.
- Rooms cannot be reduced below occupied-bed count or deleted while allocation history exists.
- Manual MySQL migration includes active bed and active patient unique keys; not applied in this environment.

### 3. Entity and relationship review
- Required DTO/entity validations strengthened for appointments, records, room allocations, room capacity, doctor name/email, department name, and patient fields.
- Unique constraints added for department name, room number, and doctor email.
- Logical references remain integer IDs to preserve the existing frontend/API payload shape. Service checks exist for doctor-department, appointment-patient/doctor, medical-record-patient/doctor, and allocation-patient/room.
- Deletion checks now prevent deleting departments with doctors, doctors with appointments/records, patients with appointments/records/room allocation history, and rooms with allocation history.
- **Database FK caveat:** These scalar IDs are not mapped as JPA associations, so foreign keys are not created automatically by Hibernate. An optional one-time `database/relationship_constraints.sql` script provides FK and generated unique constraints, including preflight queries. It is not automatically executed and has not been applied. Back up the database and verify preflight queries before manual application.
- Patient `assignedDoctor` remains a text field for compatibility; it is not a relational doctor FK and may not remain consistent after a doctor name changes.

### 4. DTOs
- CRUD request/response controllers now use DTOs for Patients, Doctors, Departments, Appointments, Rooms, Room Allocations, and Medical Records.
- Filter/report endpoints return DTOs instead of entities where they return patient/doctor/appointment/record/room lists.
- Dashboard `recentPatients` and `todayAppointmentList` now use DTO objects.
- DTOs use matching field names to preserve the existing frontend JSON contract.
- This reduces direct persistence entity exposure, but the API still returns sensitive patient/medical information and has no access controls.

### 5. Error handling and validation
- DTO validation runs on POST and PUT.
- `ResourceNotFoundException` → 404.
- Illegal business input → 400.
- Bean validation returns field-level messages; malformed JSON/parameter formats → 400.
- Database constraint violations → 409 with a generic conflict message.
- Unexpected exceptions → generic 500 response (no internal stack details exposed in response).

### 6. Security review
- **FAIL / NOT IMPLEMENTED:** No authentication or authorization.
- No roles/permissions, user management, session/token security, audit trail, HTTPS configuration, or production patient-data safeguards.
- All exposed API routes are currently unauthenticated. Do not use real patient data or deploy publicly.

### 7. Automated tests added
- JUnit 5 + Mockito service unit tests for Appointment, Room Allocation, Department, Doctor, Medical Record, Patient, and Room services.
- MockMvc/Spring Boot integration test exercises REST routes and representative validation/conflict workflows across controllers.
- H2 test dependency and `application-test.properties` use an isolated in-memory `hospital_test` database with `create-drop`.
- Test class uses `@ActiveProfiles("test")`. It does not connect to MySQL or read the normal `DB_URL`, `DB_USERNAME`, or `DB_PASSWORD` settings.
- Tests are authored but **not executed**.

## Files changed/added in this continuation
- Updated service implementations, repositories, entities, and controller DTO mappings.
- Added DTOs for appointment, department, doctor, medical record, room, and room allocation (existing PatientDTO strengthened).
- Updated `AbstractCrudController` to map request/response DTOs.
- Updated `GlobalExceptionHandler`.
- Added `src/test/java/com/hospital/service/impl/*Test.java` and `src/test/java/com/hospital/controller/RestControllerIntegrationTest.java`.
- Added `src/test/resources/application-test.properties` and test H2 dependency.
- Added optional `database/relationship_constraints.sql`.
- Updated README environment variables and Windows/Eclipse test instructions.

## Remaining risks / required runtime work
1. Run Maven tests and fix any compile, JPQL, H2, or assertion issues found.
2. Run the optional MySQL constraints script only after backups, duplicate/orphan preflight, and schema review; it is one-time and non-idempotent.
3. Add appointment duration and interval-overlap validation if appointments have variable lengths.
4. Implement authentication/authorization, role controls, audit logging, HTTPS, and patient-data privacy safeguards.
5. Browser and end-to-end test the full UI after the backend starts.
6. Confirm database constraints and transactions under concurrent load; the test suite does not include multi-threaded concurrency stress tests.

## Audit conclusion
The code has been further hardened and tests have been prepared. This deliverable is **not fully runtime verified**. Java is present, but Maven and MySQL are absent; the test suite and application build remain blocked/unverified until the user runs the documented Windows commands.

### Final continuation checks (after the implementation edits)
- Main Java files: **50**; DTO Java files: **8**; test Java files: **8** (7 service unit-test classes + 1 REST integration-test class).
- Test count authored: **26 service test methods + 1 integration test method**.
- Java syntax diagnostic scan: **0 syntax-pattern diagnostics** in the `javac` output. The `javac` command still exits nonzero with unresolved external packages/symbols (Spring/Jakarta/JUnit/Mockito/Jackson), so compilation remains **BLOCKED**, not PASS.
- Test profile location verified as `src/test/resources/application-test.properties`; the H2 profile file is no longer under main resources.
- Temporary-file scan: **PASS** (no `*.tmp`, `*.bak`, or editor backup files found).
- ZIP archive integrity: **PASS** (`unzip -t` reported no errors).
