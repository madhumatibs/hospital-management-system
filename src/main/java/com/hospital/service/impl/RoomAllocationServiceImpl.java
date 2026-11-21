package com.hospital.service.impl;

import com.hospital.entity.Room;
import com.hospital.entity.RoomAllocation;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.repository.PatientRepository;
import com.hospital.repository.RoomAllocationRepository;
import com.hospital.repository.RoomRepository;
import com.hospital.service.RoomAllocationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoomAllocationServiceImpl extends AbstractCrudService<RoomAllocation> implements RoomAllocationService {
    private final RoomAllocationRepository allocations;
    private final RoomRepository rooms;
    private final PatientRepository patients;
    public RoomAllocationServiceImpl(RoomAllocationRepository allocations, RoomRepository rooms, PatientRepository patients) {
        super(allocations); this.allocations=allocations; this.rooms=rooms; this.patients=patients;
    }

    @Override @Transactional
    public RoomAllocation create(RoomAllocation a) {
        a.setAllocationId(null);
        validateInput(a);
        Room room=lockRoom(a.getRoomId());
        patients.lockById(a.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient not found: " + a.getPatientId()));
        if (a.getReleaseDate()!=null) throw new IllegalArgumentException("New allocations must be active; release them through an update.");
        if ("MAINTENANCE".equalsIgnoreCase(room.getRoomStatus()) || "FULL".equalsIgnoreCase(room.getRoomStatus())) throw new IllegalArgumentException("Room is not currently available for allocation.");
        if (a.getBedNumber()>room.getTotalBeds()) throw new IllegalArgumentException("Bed number exceeds this room's bed capacity.");
        if (allocations.existsByRoomIdAndBedNumberAndReleaseDateIsNull(a.getRoomId(),a.getBedNumber())) throw new IllegalArgumentException("This bed is already occupied.");
        if (allocations.existsByPatientIdAndReleaseDateIsNull(a.getPatientId())) throw new IllegalArgumentException("Patient already has an active room allocation.");
        a.setAdmissionDate(a.getAdmissionDate()==null ? java.time.LocalDate.now() : a.getAdmissionDate());
        RoomAllocation saved=allocations.save(a);
        syncAvailableBeds(room);
        return saved;
    }

    @Override @Transactional
    public RoomAllocation update(Integer id, RoomAllocation request) {
        RoomAllocation current=allocations.lockById(id).orElseThrow(() -> new ResourceNotFoundException("Room allocation not found: " + id));
        if (!java.util.Objects.equals(current.getPatientId(),request.getPatientId()) || !java.util.Objects.equals(current.getRoomId(),request.getRoomId()) || !java.util.Objects.equals(current.getBedNumber(),request.getBedNumber()) || !java.util.Objects.equals(current.getAdmissionDate(),request.getAdmissionDate()))
            throw new IllegalArgumentException("Allocation identity fields cannot be changed. Only releaseDate may be updated.");
        if (current.getReleaseDate()!=null && request.getReleaseDate()==null) throw new IllegalArgumentException("Released allocations cannot be reactivated.");
        Room room=lockRoom(current.getRoomId());
        if (current.getReleaseDate()==null && request.getReleaseDate()!=null && request.getReleaseDate().isBefore(current.getAdmissionDate())) throw new IllegalArgumentException("Release date cannot be before admission date.");
        current.setReleaseDate(request.getReleaseDate());
        RoomAllocation saved=allocations.save(current);
        syncAvailableBeds(room);
        return saved;
    }

    @Override @Transactional
    public void delete(Integer id) {
        RoomAllocation allocation=allocations.lockById(id).orElseThrow(() -> new ResourceNotFoundException("Room allocation not found: " + id));
        if (allocation.getReleaseDate()==null) throw new IllegalArgumentException("Active allocations cannot be deleted. Release the patient first.");
        allocations.delete(allocation);
    }

    private void validateInput(RoomAllocation a) {
        if(a.getPatientId()==null || a.getRoomId()==null || a.getBedNumber()==null || a.getBedNumber()<=0) throw new IllegalArgumentException("Patient, room, and positive bed number are required.");
    }
    private Room lockRoom(Integer id) {
        Room room=rooms.lockById(id).orElseThrow(() -> new ResourceNotFoundException("Room not found: " + id));
        if(room.getTotalBeds()==null || room.getTotalBeds()<1) throw new IllegalArgumentException("Room must have a positive total bed capacity.");
        return room;
    }
    private void syncAvailableBeds(Room room) {
        long occupied=allocations.countByRoomIdAndReleaseDateIsNull(room.getRoomId());
        room.setAvailableBeds((int)Math.max(0,room.getTotalBeds()-occupied));
        if(room.getRoomStatus()!=null&&!room.getRoomStatus().equalsIgnoreCase("MAINTENANCE")){room.setRoomStatus(room.getAvailableBeds()==0?"FULL":"AVAILABLE");}
        rooms.save(room);
    }
}
