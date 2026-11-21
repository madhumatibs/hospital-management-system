-- OPTIONAL, ONE-TIME MYSQL 8 HARDENING SCRIPT.
-- BACK UP the database and run the preflight queries first. Resolve any returned rows
-- before applying the ALTER statements. This script is intentionally not auto-run by
-- the application and does not delete/modify user rows.

-- Preflight: duplicates for unique fields (each query should return zero rows).
SELECT LOWER(TRIM(department_name)) AS normalized_name, COUNT(*) AS duplicate_count
FROM departments GROUP BY LOWER(TRIM(department_name)) HAVING COUNT(*) > 1;
SELECT LOWER(TRIM(room_number)) AS normalized_room_number, COUNT(*) AS duplicate_count
FROM rooms GROUP BY LOWER(TRIM(room_number)) HAVING COUNT(*) > 1;
SELECT LOWER(TRIM(email)) AS normalized_email, COUNT(*) AS duplicate_count
FROM doctors WHERE email IS NOT NULL AND TRIM(email)<>''
GROUP BY LOWER(TRIM(email)) HAVING COUNT(*) > 1;

-- Preflight: orphaned logical references (each query should return zero rows).
SELECT a.* FROM appointments a LEFT JOIN patients p ON p.patient_id=a.patient_id WHERE p.patient_id IS NULL;
SELECT a.* FROM appointments a LEFT JOIN doctors d ON d.doctor_id=a.doctor_id WHERE d.doctor_id IS NULL;
SELECT m.* FROM medical_records m LEFT JOIN patients p ON p.patient_id=m.patient_id WHERE p.patient_id IS NULL;
SELECT m.* FROM medical_records m LEFT JOIN doctors d ON d.doctor_id=m.doctor_id WHERE d.doctor_id IS NULL;
SELECT d.* FROM doctors d LEFT JOIN departments dep ON dep.department_id=d.department_id WHERE d.department_id IS NOT NULL AND dep.department_id IS NULL;
SELECT ra.* FROM room_allocations ra LEFT JOIN patients p ON p.patient_id=ra.patient_id WHERE p.patient_id IS NULL;
SELECT ra.* FROM room_allocations ra LEFT JOIN rooms r ON r.room_id=ra.room_id WHERE r.room_id IS NULL;

-- Preflight: active duplicate appointment slots / active bed allocations (must return zero rows).
SELECT doctor_id, appointment_date, appointment_time, COUNT(*) AS duplicate_count
FROM appointments WHERE UPPER(COALESCE(status,'')) NOT IN ('CANCELLED','CANCELED','COMPLETED')
GROUP BY doctor_id, appointment_date, appointment_time HAVING COUNT(*) > 1;
SELECT patient_id, appointment_date, appointment_time, COUNT(*) AS duplicate_count
FROM appointments WHERE UPPER(COALESCE(status,'')) NOT IN ('CANCELLED','CANCELED','COMPLETED')
GROUP BY patient_id, appointment_date, appointment_time HAVING COUNT(*) > 1;
SELECT room_id, bed_number, COUNT(*) AS duplicate_count FROM room_allocations WHERE release_date IS NULL
GROUP BY room_id, bed_number HAVING COUNT(*) > 1;
SELECT patient_id, COUNT(*) AS duplicate_count FROM room_allocations WHERE release_date IS NULL
GROUP BY patient_id HAVING COUNT(*) > 1;

-- Optional referential constraints. Use after preflight returns no orphan rows.
ALTER TABLE doctors ADD CONSTRAINT fk_doctor_department FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE RESTRICT;
ALTER TABLE appointments ADD CONSTRAINT fk_appointment_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE RESTRICT;
ALTER TABLE appointments ADD CONSTRAINT fk_appointment_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE RESTRICT;
ALTER TABLE medical_records ADD CONSTRAINT fk_record_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE RESTRICT;
ALTER TABLE medical_records ADD CONSTRAINT fk_record_doctor FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE RESTRICT;
ALTER TABLE room_allocations ADD CONSTRAINT fk_allocation_patient FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE RESTRICT;
ALTER TABLE room_allocations ADD CONSTRAINT fk_allocation_room FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT;

-- DB-level active-slot and active-bed protection (MySQL 8 generated columns).
-- NULL keys are allowed for inactive/cancelled/completed appointments and released allocations.
ALTER TABLE appointments
  ADD COLUMN active_doctor_slot_key VARCHAR(100)
    GENERATED ALWAYS AS (CASE WHEN UPPER(COALESCE(status,'')) NOT IN ('CANCELLED','CANCELED','COMPLETED')
      THEN CONCAT(doctor_id, ':', appointment_date, ':', appointment_time) ELSE NULL END) STORED,
  ADD COLUMN active_patient_slot_key VARCHAR(100)
    GENERATED ALWAYS AS (CASE WHEN UPPER(COALESCE(status,'')) NOT IN ('CANCELLED','CANCELED','COMPLETED')
      THEN CONCAT(patient_id, ':', appointment_date, ':', appointment_time) ELSE NULL END) STORED,
  ADD UNIQUE KEY uk_appointment_doctor_active_slot (active_doctor_slot_key),
  ADD UNIQUE KEY uk_appointment_patient_active_slot (active_patient_slot_key);

ALTER TABLE room_allocations
  ADD COLUMN active_room_bed_key VARCHAR(100)
    GENERATED ALWAYS AS (CASE WHEN release_date IS NULL THEN CONCAT(room_id, ':', bed_number) ELSE NULL END) STORED,
  ADD COLUMN active_patient_allocation_key VARCHAR(100)
    GENERATED ALWAYS AS (CASE WHEN release_date IS NULL THEN patient_id ELSE NULL END) STORED,
  ADD UNIQUE KEY uk_active_room_bed (active_room_bed_key),
  ADD UNIQUE KEY uk_active_patient_allocation (active_patient_allocation_key);

-- This is a manual, non-idempotent migration. Do not run it twice.
