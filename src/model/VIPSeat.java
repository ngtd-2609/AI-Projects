package model;

/**
 * Lớp VIPSeat kế thừa từ Seat.
 * Ghế VIP có giá cao hơn ghế thường (nhân hệ số 1.5).
 * Đây là ví dụ về tính kế thừa (Inheritance) và đa hình (Polymorphism).
 */
public class VIPSeat extends Seat {
    private static final long serialVersionUID = 5L;

    private double vipMultiplier = 1.5;

    public VIPSeat() { super(); }

    public VIPSeat(int seatId, int roomId, String seatLabel) {
        super(seatId, roomId, seatLabel, "VIP");
    }

    /**
     * Override phương thức tính giá - ghế VIP = giá gốc × hệ số VIP (1.5)
     * Đây là ví dụ về đa hình (Polymorphism): cùng tên phương thức,
     * nhưng VIPSeat cho kết quả khác Seat.
     */
    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * vipMultiplier;
    }

    public double getVipMultiplier() { return vipMultiplier; }
    public void setVipMultiplier(double vipMultiplier) { this.vipMultiplier = vipMultiplier; }

    @Override
    public String toString() { return getSeatLabel() + " [VIP]"; }
}
