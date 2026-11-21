# Hospital Management System (Java 21)

Educational full-stack mini-project using Spring Boot 3.3.5, Java 21, Spring Data JPA, MySQL 8, Bootstrap, and vanilla JavaScript.

## Features
- Dashboard summary and statistics
- CRUD for patients, doctors, departments, appointments, rooms, room allocations, and medical records
- Patient admission/ailment/doctor reports
- Doctor specialization and department filters
- Appointment filters by doctor, patient, and date
- Room availability endpoint
- DTO-based request/response mapping for CRUD and filter endpoints
- Validation, centralized error responses, duplicate checks, and relationship checks in service layer
- Transactional appointment conflict prevention and bed allocation/release rules

## Requirements
- Java JDK 21
- Apache Maven 3.9+
- MySQL 8+ for normal application use
- Eclipse IDE (or another Java IDE)

## Configure MySQL (normal app profile)
1. Start MySQL Server and open MySQL Workbench.
2. Run `database/schema.sql`.
3. (Optional) Run `database/sample_data.sql` to insert demo records.
4. In Windows Command Prompt, set the environment variables used by `application.properties` (not PowerShell syntax):

```bat
set DB_URL=jdbc:mysql://localhost:3306/hospital_management?createDatabaseIfNotExist=true
set DB_USERNAME=root
set DB_PASSWORD=your_mysql_password
```

The exact application variable names are `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`. The defaults are defined in `src/main/resources/application.properties`. Never commit credentials.

## Run in Eclipse
1. Install/configure JDK 21: **Window → Preferences → Java → Installed JREs**.
2. Install Maven integration (m2e) if not present.
3. **File → Import → Maven → Existing Maven Projects**.
4. Select this project directory and Finish.
5. Right-click the project → **Maven → Update Project**; allow dependency downloads to finish.
6. Run tests: right-click project → **Run As → Maven test** (the integration test uses the isolated H2 test profile).
7. Run `HospitalManagementSystemApplication.java` as **Java Application** for the regular MySQL profile after MySQL is configured.
8. Open `http://localhost:8085`.

## Run from Windows Command Prompt
Open CMD (not PowerShell), change directory to the project root, and verify tools:

```bat
java -version
mvn -version
```

Run unit tests and the H2-backed REST controller integration test:

```bat
mvn clean test
```

Run tests with verbose output:
```bat
mvn test -DtrimStackTrace=false
```

Build the executable JAR after tests:
```bat
mvn clean package
```

Run the application using the regular MySQL profile:
```bat
mvn spring-boot:run
```

If Maven isn't recognized, install Apache Maven, add its `bin` directory to PATH, restart CMD, and retry. Eclipse may use its own embedded Maven, but command-line testing requires Maven on PATH.

## Test database safety
- `src/test/resources/application-test.properties` points to an in-memory H2 database named `hospital_test`.
- It uses `spring.jpa.hibernate.ddl-auto=create-drop`; the schema is temporary and discarded after the test JVM exits.
- The integration test is annotated `@ActiveProfiles("test")`; it does not use the normal MySQL `DB_URL` configuration.
- Never add a real patient dataset to the test profile.
- The test profile does not delete, alter, or connect to the production MySQL database.

## API base
`http://localhost:8085/api/v1`

| Module | Base endpoint |
|---|---|
| Patients | `/patients` |
| Doctors | `/doctors` |
| Departments | `/departments` |
| Appointments | `/appointments` |
| Rooms | `/rooms` |
| Room Allocations | `/room-allocations` |
| Medical Records | `/medical-records` |
| Reports | `/reports` |

Patient report endpoints:
- `GET /patients/reports/admissions?start=2026-01-01&end=2026-12-31`
- `GET /patients/reports/ailment?ailment=Fever`
- `GET /patients/reports/doctor?doctor=Dr.%20Kumar`
- `GET /patients/reports/doctor-count`

Other filters:
- `GET /doctors/specialization?name=Cardiology`
- `GET /doctors/department/{departmentId}`
- `GET /appointments/doctor/{doctorId}`
- `GET /appointments/patient/{patientId}`
- `GET /appointments/date?date=2026-11-21`
- `GET /rooms/available`
- `GET /medical-records/patient/{patientId}`
- `GET /medical-records/doctor/{doctorId}`
- `GET /reports/dashboard`
- `GET /reports/doctor-statistics`
- `GET /reports/department-statistics`

## Relationship and database constraints
Entity ID fields remain scalar integers to preserve the existing JSON/UI contract. Service-layer checks validate references for appointments, doctors, medical records, room allocations, and deletion dependencies. JPA uniqueness constraints cover department names, room numbers, and doctor emails. Database foreign keys and generated unique active-slot/bed keys are documented in the optional one-time `database/relationship_constraints.sql` migration. Back up the MySQL database and resolve the preflight duplicate/orphan queries before applying it. It is not run automatically.

## Security limitations — do not use real patient data
- Authentication and authorization are **not implemented**.
- No role-based access control, user sessions, account lifecycle, audit logging, or production privacy/security controls.
- API requests can access or modify data without an identity check.
- No HTTPS is configured by this scaffold.
- The project is for coursework/demo data only until a security design is implemented and independently reviewed.

## Current verification status
See `AUDIT_REPORT.md` and `TEST_REPORT.md`. Static checks can be run in this environment, but Maven/MySQL runtime verification is blocked here. The test suite is prepared but **has not been executed in this environment**. Do not describe the project as fully compiled, fully tested, secure, or production ready until the listed commands have been run successfully in a Maven/MySQL/browser-capable environment.

SCREENSHOT

#BACKEND CONSOLE
<img width="1522" height="800" alt="Screenshot 2026-11-22 002331" src="https://github.com/user-attachments/assets/886f66f0-0ed6-4ac6-b6ab-edc0c0003975" />

#FRONTEND RUNNING 
<img width="1062" height="532" alt="image" src="https://github.com/user-attachments/assets/5ad671ee-be0e-43a0-a4b7-4fea0c86f5a7" />
<img width="1913" height="916" alt="Screenshot 2026-11-22 002155" src="https://github.com/user-attachments/assets/4090a913-4c93-4b95-9cda-2d98518bef05" />
<img width="1912" height="906" alt="Screenshot 2026-11-22 002220" src="https://github.com/user-attachments/assets/afb43788-cd1f-4fd7-a4af-05493bab5963" />
<img width="1907" height="912" alt="Screenshot 2026-11-22 002241" src="https://github.com/user-attachments/assets/8f8f729f-99e4-468f-9c98-a245798f9c1d" />
<img width="1916" height="912" alt="Screenshot 2026-11-22 002352" src="https://github.com/user-attachments/assets/29f30b25-a5a1-439e-9810-0d7d650bdae3" />
<img width="1916" height="912" alt="Screenshot 2026-11-22 002407" src="https://github.com/user-attachments/assets/32604654-e259-4f47-8479-b9a0ea6c65f9" />
<img width="1071" height="552" alt="Screenshot 2026-11-22 002420" src="https://github.com/user-attachments/assets/e4197283-1fdd-48be-9be9-e0576e7a74cb" />
<img width="1105" height="548" alt="Screenshot 2026-11-22 002446" src="https://github.com/user-attachments/assets/614a1013-f5ba-4409-b791-c5cd2ff5f411" />
<img width="1062" height="532" alt="Screenshot 2026-11-22 002459" src="https://github.com/user-attachments/assets/2d24c30f-3c8f-4b72-922d-a3ffb5b48637" />






