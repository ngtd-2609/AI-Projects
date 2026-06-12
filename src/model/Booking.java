package model;

import java.io.Serializable;

public class Booking implements Serializable {
    private static final long serialVersionUID = 9L;

    private int bookingId;
    private int showtimeId;
    private int customerId;
    private int userId;
    private String bookingDate;
    private double totalAmount;
    // Extra fields for display
    private String movieTitle;
    private String customerName;
    private String staffName;
    private String showDate;
    private String startTime;
    private String paymentStatus;
    private String paymentMethod;

    public Booking() {}

    public Booking(int bookingId, int showtimeId, int customerId, int userId,
                   String bookingDate, double totalAmount) {
        this.bookingId = bookingId;
        this.showtimeId = showtimeId;
        this.customerId = customerId;
        this.userId = userId;
        this.bookingDate = bookingDate;
        this.totalAmount = totalAmount;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentStatusDisplay() {
        if ("DA_THANH_TOAN".equals(paymentStatus)) return "Đã thanh toán";
        return "Chưa thanh toán";
    }

    public String getPaymentMethodDisplay() {
        if (paymentMethod == null) return "";
        switch (paymentMethod) {
            case "TIEN_MAT": return "Tiền mặt";
            case "CHUYEN_KHOAN": return "Chuyển khoản";
            case "THE_TIN_DUNG": return "Thẻ tín dụng";
            default: return paymentMethod;
        }
    }
}
