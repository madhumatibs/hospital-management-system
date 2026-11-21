# Architecture & Diagrams

## Logical architecture

```mermaid
flowchart TD
  UI[HTML / Bootstrap / JavaScript] --> API[Spring REST Controllers]
  API --> SVC[Service Layer]
  SVC --> REPO[Spring Data JPA Repositories]
  REPO --> DB[(MySQL hospital_management)]
```

## Data model (planned relational associations)

```mermaid
erDiagram
  DEPARTMENTS ||--o{ DOCTORS : contains
  PATIENTS ||--o{ APPOINTMENTS : books
  DOCTORS ||--o{ APPOINTMENTS : attends
  PATIENTS ||--o{ MEDICAL_RECORDS : has
  DOCTORS ||--o{ MEDICAL_RECORDS : authors
  PATIENTS ||--o{ ROOM_ALLOCATIONS : assigned
  ROOMS ||--o{ ROOM_ALLOCATIONS : contains
```

**Current scaffold note:** the current entity classes store relationship IDs as integer fields to keep the generic admin UI and APIs easy to test. The ER relationships above are the target design and must be converted to entity associations/foreign-key constraints before production-like use.

## Use case overview

```mermaid
flowchart LR
  Admin[Hospital Administrator] --> P[Manage Patients]
  Admin --> D[Manage Doctors]
  Admin --> DP[Manage Departments]
  Admin --> A[Manage Appointments]
  Admin --> R[Manage Rooms & Beds]
  Admin --> M[Manage Medical Records]
  Admin --> RP[View Reports]
```

## Appointment activity flow

```mermaid
flowchart TD
  S([Start]) --> Select[Select patient and doctor]
  Select --> Details[Enter date, time and reason]
  Details --> Validate{Valid request?}
  Validate -- No --> Fix[Show validation error]
  Fix --> Details
  Validate -- Yes --> Save[Save appointment]
  Save --> Confirm[Show confirmation]
  Confirm --> E([End])
```
