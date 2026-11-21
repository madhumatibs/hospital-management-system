package com.hospital.service.impl;

import com.hospital.entity.Appointment;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.AppointmentRepository;
import com.hospital.repository.DoctorRepository;
import com.hospital.repository.PatientRepository;
import com.hospital.service.AppointmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Locale;

@Service
public class AppointmentServiceImpl extends AbstractCrudService<Appointment> implements AppointmentService {
    private final AppointmentRepository appointments;
    private final DoctorRepository doctors;
    private final PatientRepository patients;
    public AppointmentServiceImpl(AppointmentRepository appointments, DoctorRepository doctors, PatientRepository patients) {
        super(appointments); this.appointments=appointments; this.doctors=doctors; this.patients=patients;
    }

    @Override @Transactional
    public Appointment create(Appointment a) {
        a.setAppointmentId(null);
        validateRequired(a);
        if (a.getStatus()==null || a.getStatus().isBlank()) a.setStatus("SCHEDULED");
        validateStatus(a.getStatus());
        lockReferences(a);
        validateConflict(a, null);
        return appointments.save(a);
    }

    @Override @Transactional
    public Appointment update(Integer id, Appointment a) {
        Appointment existing = appointments.findById(id).orElseThrow(() -> new ResourceNotFoundException("Appointment not found: " + id));
        validateRequired(a);
        if (a.getStatus()==null || a.getStatus().isBlank()) a.setStatus(existing.getStatus()==null ? "SCHEDULED" : existing.getStatus());
        validateStatus(a.getStatus());
        lockReferences(a);
        validateConflict(a, id);
        a.setAppointmentId(id);
        return appointments.save(a);
    }

    private void validateRequired(Appointment a) {
        if (a.getPatientId()==null || a.getDoctorId()==null || a.getAppointmentDate()==null || a.getAppointmentTime()==null)
            throw new IllegalArgumentException("Patient, doctor, date, and time are required.");
        if (a.getPatientId()<=0 || a.getDoctorId()<=0) throw new IllegalArgumentException("Patient and doctor IDs must be positive.");
    }

    /** Lock doctor and patient rows so concurrent requests for the same slot serialize before checking. */
    private void lockReferences(Appointment a) {
        doctors.lockById(a.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor not found: " + a.getDoctorId()));
        patients.lockById(a.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + a.getPatientId()));
    }

    private void validateConflict(Appointment a, Integer excludeId) {
        if (isInactive(a.getStatus())) return;
        if (!appointments.findDoctorSlotConflicts(a.getDoctorId(), a.getAppointmentDate(), a.getAppointmentTime(), excludeId).isEmpty())
            throw new IllegalArgumentException("Doctor already has an active appointment at this date and time.");
        if (!appointments.findPatientSlotConflicts(a.getPatientId(), a.getAppointmentDate(), a.getAppointmentTime(), excludeId).isEmpty())
            throw new IllegalArgumentException("Patient already has an active appointment at this date and time.");
    }
    private void validateStatus(String status) {
        String normalized=status.trim().toUpperCase(Locale.ROOT);
        if (!normalized.equals("SCHEDULED") && !normalized.equals("COMPLETED") && !normalized.equals("CANCELLED") && !normalized.equals("CANCELED") && !normalized.equals("CONFIRMED")) throw new IllegalArgumentException("Appointment status must be Scheduled, Confirmed, Completed, or Cancelled.");
    }
    private boolean isInactive(String status) {
        if (status==null) return false;
        String normalized=status.trim().toLowerCase(Locale.ROOT);
        return normalized.equals("cancelled") || normalized.equals("canceled") || normalized.equals("completed");
    }
}
