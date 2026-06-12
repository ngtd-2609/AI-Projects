package model;

import java.io.Serializable;

public class Room implements Serializable {
    private static final long serialVersionUID = 7L;

    private int roomId;
    private String roomName;
    private int totalSeats;
    private String roomType;

    public Room() {}

    public Room(int roomId, String roomName, int totalSeats, String roomType) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.totalSeats = totalSeats;
        this.roomType = roomType;
    }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public String getRoomType() { return roomType; }
    public void setRoomType(String roomType) { this.roomType = roomType; }

    @Override
    public String toString() { return roomName + " (" + roomType + ")"; }
}
