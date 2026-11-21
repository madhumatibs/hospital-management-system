package com.hospital.controller;
import com.hospital.dto.RoomDTO; import com.hospital.entity.Room; import com.hospital.service.RoomService; import org.springframework.web.bind.annotation.*; import com.hospital.repository.RoomRepository; import java.util.List;
@RestController @RequestMapping("/api/v1/rooms")
public class RoomController extends AbstractCrudController<Room, RoomDTO> {
    private final RoomRepository roomRepository;
    public RoomController(RoomService service, RoomRepository repository){super(service, Room.class, RoomDTO.class);this.roomRepository=repository;}
    @GetMapping("/available") public List<RoomDTO> available(){return roomRepository.findByAvailableBedsGreaterThan(0).stream().map(this::toDto).toList();}
}
