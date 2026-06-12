package model;

import java.io.Serializable;

/**
 * Lớp Seat - đại diện cho ghế ngồi trong phòng chiếu.
 * Là lớp cha của VIPSeat.
 * Implement Serializable để hỗ trợ lưu file nhị phân .dat.
 */
public class Seat implements Serializable {
    private static final long serialVersionUID = 4L;

    private int seatId;
    private int roomId;
    private String seatLabel;
    private String seatType;

    public Seat() {}

    public Seat(int seatId, int roomId, String seatLabel, String seatType) {
        this.seatId = seatId;
        this.roomId = roomId;
        this.seatLabel = seatLabel;
        this.seatType = seatType;
    }

    /**
     * Phương thức tính giá vé theo loại ghế.
     * Ghế thường: trả về giá gốc.
     * Lớp con VIPSeat sẽ override để nhân hệ số VIP → Đa hình.
     */
    public double calculatePrice(double basePrice) {
        return basePrice;
    }

    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getSeatLabel() { return seatLabel; }
    public void setSeatLabel(String seatLabel) { this.seatLabel = seatLabel; }
    public String getSeatType() { return seatType; }
    public void setSeatType(String seatType) { this.seatType = seatType; }

    @Override
    public String toString() { return seatLabel; }
}
