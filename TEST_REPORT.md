# Hospital Management System – Test & Verification Report

**Date:** 2026-11-21  
**Result:** STATIC CHECKS PASS; BUILD/UNIT/INTEGRATION/RUNTIME TESTS NOT EXECUTED (BLOCKED).

## Executed checks and exact outcomes

| Check | Status | Exact result |
|---|---|---|
| Java runtime | PASS | OpenJDK 21.0.12.1 reported by `java -version` |
| Maven availability | BLOCKED | `mvn -version` returned `mvn: command not found` |
| Java compilation | BLOCKED | `javac` attempted; unresolved Spring/Jakarta symbols because dependencies are unavailable. Not a successful compile. |
| Frontend JavaScript syntax | PASS | `node --check src/main/resources/static/js/app.js` exited 0 |
| POM XML | PASS | Python XML parser accepted `pom.xml` |
| Postman collection JSON | PASS | Python JSON parser accepted `docs/HospitalManagementSystem.postman_collection.json` |
| H2 test profile | STATIC REVIEW ONLY | `application-test.properties` uses `jdbc:h2:mem:hospital_test`, `create-drop`, SQL init disabled; not launched |
| MySQL connectivity | BLOCKED | MySQL server/client unavailable; no connection attempted successfully |
| Unit tests | CREATED / NOT RUN | JUnit/Mockito service tests authored for 7 services |
| REST integration tests | CREATED / NOT RUN | MockMvc integration test authored with `@ActiveProfiles("test")`; Maven dependencies unavailable |
| Browser/E2E | BLOCKED | App could not be started; browser verification not performed |
| ZIP integrity | Pending packaging check | Will be verified after ZIP creation |

## Tests authored (not passed)

### Service unit tests
- `AppointmentServiceImplTest`: free slot success, doctor collision, patient collision, cancelled appointment behavior, missing doctor.
- `RoomAllocationServiceImplTest`: successful allocation and available-bed recalculation, occupied bed, patient already allocated, out-of-capacity bed, active allocation deletion restriction, release recalculation.
- `DepartmentServiceImplTest`: unique department creation, duplicate name rejection, deletion blocked with doctors.
- `DoctorServiceImplTest`: doctor creation, invalid department rejection, deletion blocked with records/appointments.
- `MedicalRecordServiceImplTest`: valid record creation, missing patient rejection.
- `PatientServiceImplTest`: valid patient creation, age validation, deletion blocked with appointments.
- `RoomServiceImplTest`: room creation and available-bed initialization, duplicate room number, capacity cannot be reduced below occupancy, deletion blocked with allocation history.

### REST controller integration test
- `RestControllerIntegrationTest` covers representative create/list/detail/filter/report routes across patients, doctors, departments, appointments, rooms, room allocations, medical records, and reports.
- It also asserts an appointment duplicate returns HTTP 400, a duplicate active allocation returns HTTP 400, and allocation release returns the updated DTO.
- It uses an in-memory H2 test database via `application-test.properties` and does not connect to the configured MySQL database.

## Required Windows test commands

From Command Prompt (`cmd.exe`) in the project root:

```bat
mvn -version
mvn clean test
mvn test -DtrimStackTrace=false
mvn clean package
```

Expected outcomes after Maven is installed: the first command reports Maven version; `mvn clean test` runs service tests and H2 integration tests; `mvn clean package` compiles and packages only if tests pass. **No expected result is claimed as observed here.**

## Acceptance status

The source test suite is prepared, but no test is marked passed based on code inspection. All Maven-dependent tests, compilation, database runtime, and browser/E2E testing are currently **BLOCKED / NOT RUN**. The project is not accepted as fully functional, secure, or production ready.

## Final static checks added to the exact results
- `javac -proc:none -Xmaxerrs 3000 ...`: **BLOCKED / exit 1**; 728 unresolved external-package/symbol diagnostics were counted; **0 syntax-pattern diagnostics** were found. This is only a syntax-pattern scan, not a Java compile pass.
- Main Java source count: **50**; DTO count: **8**.
- Test source count: **8**; service test classes: **7**; service test methods: **26**; REST integration test class: **1**, integration test method: **1**.
- Test H2 profile location and URL statically verified: `src/test/resources/application-test.properties`, `jdbc:h2:mem:hospital_test`.
- No `*.tmp`, `*.bak`, or editor backup files found.
- ZIP integrity: **PASS** (`unzip -t` reported no errors after archive creation).
