package com.hospital.repository;
import com.hospital.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
public interface RoomRepository extends JpaRepository<Room, Integer> {
    List<Room> findByAvailableBedsGreaterThan(Integer count);
    @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select r from Room r where r.roomId=:id") Optional<Room> lockById(@Param("id") Integer id);
    Optional<Room> findByRoomNumberIgnoreCase(String roomNumber);
}
