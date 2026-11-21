package com.hospital.controller;
import com.hospital.dto.RoomAllocationDTO; import com.hospital.entity.RoomAllocation; import com.hospital.service.RoomAllocationService; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/room-allocations")
public class RoomAllocationController extends AbstractCrudController<RoomAllocation, RoomAllocationDTO> { public RoomAllocationController(RoomAllocationService service){super(service, RoomAllocation.class, RoomAllocationDTO.class);} }
