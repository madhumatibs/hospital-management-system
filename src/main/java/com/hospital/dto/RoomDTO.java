package com.hospital.dto;
import jakarta.validation.constraints.*;

public class RoomDTO {
    private Integer roomId;
    @NotBlank private String roomNumber;
    private String roomType;
    @NotNull @Positive private Integer totalBeds;
    private Integer availableBeds;
    @Pattern(regexp="(?i)AVAILABLE|FULL|MAINTENANCE") private String roomStatus;
    public RoomDTO() {}
    public Integer getRoomId() { return roomId; }
    public void setRoomId(Integer roomId) { this.roomId = roomId; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }
    public Integer getTotalBeds() { return totalBeds; }
    public void setTotalBeds(Integer totalBeds) { this.totalBeds = totalBeds; }
    public Integer getAvailableBeds() { return availableBeds; }
    public void setAvailableBeds(Integer availableBeds) { this.availableBeds = availableBeds; }
    public String getRoomStatus() { return roomStatus; }
    public void setRoomStatus(String roomStatus) { this.roomStatus = roomStatus; }
}
