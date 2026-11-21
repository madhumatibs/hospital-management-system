package com.hospital.service.impl;
import com.hospital.entity.Room; import com.hospital.repository.*; import org.junit.jupiter.api.Test; import org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.junit.jupiter.MockitoExtension; import java.util.Optional; import static org.junit.jupiter.api.Assertions.*; import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class) class RoomServiceImplTest {
 @Mock RoomRepository rooms; @Mock RoomAllocationRepository allocations; @InjectMocks RoomServiceImpl service;
 @Test void createsRoomAndSetsAvailableBedsToTotal(){Room r=new Room();r.setRoomNumber("A-1");r.setTotalBeds(4);when(rooms.findByRoomNumberIgnoreCase("A-1")).thenReturn(Optional.empty());when(rooms.save(r)).thenReturn(r);assertEquals(r,service.create(r));assertEquals(4,r.getAvailableBeds());}
 @Test void rejectsDuplicateRoomNumber(){Room r=new Room();r.setRoomNumber("A-1");r.setTotalBeds(2);Room existing=new Room();existing.setRoomId(2);when(rooms.findByRoomNumberIgnoreCase("A-1")).thenReturn(Optional.of(existing));assertThrows(IllegalArgumentException.class,()->service.create(r));}
 @Test void rejectsCapacityBelowOccupancy(){Room r=new Room();r.setRoomNumber("A-1");r.setTotalBeds(1);when(rooms.lockById(1)).thenReturn(Optional.of(new Room()));when(rooms.findByRoomNumberIgnoreCase("A-1")).thenReturn(Optional.empty());when(allocations.countByRoomIdAndReleaseDateIsNull(1)).thenReturn(2L);assertThrows(IllegalArgumentException.class,()->service.update(1,r));}
 @Test void preventsDeletingOccupiedRoom(){when(rooms.lockById(1)).thenReturn(Optional.of(new Room()));when(allocations.countByRoomId(1)).thenReturn(1L);assertThrows(IllegalArgumentException.class,()->service.delete(1));}
}
