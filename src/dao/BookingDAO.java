package dao;

import model.Booking;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {
    public List<Booking> getAll() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, m.title as movie_title, c.fullname as customer_name, " +
            "u.fullname as staff_name, s.show_date, s.start_time " +
            "FROM bookings b " +
            "JOIN showtimes s ON b.showtime_id = s.showtime_id " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN customers c ON b.customer_id = c.customer_id " +
            "LEFT JOIN users u ON b.user_id = u.user_id " +
            "ORDER BY b.booking_id DESC";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean createBooking(Booking booking, List<Integer> seatIds, double pricePerSeat) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert booking
            PreparedStatement ps1 = conn.prepareStatement(
                "INSERT INTO bookings (showtime_id, customer_id, user_id, booking_date, total_amount, payment_status, payment_method) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, booking.getShowtimeId());
            ps1.setInt(2, booking.getCustomerId());
            if (booking.getUserId() > 0) {
                ps1.setInt(3, booking.getUserId());
            } else {
                ps1.setNull(3, java.sql.Types.INTEGER);
            }
            ps1.setString(4, booking.getBookingDate());
            ps1.setDouble(5, booking.getTotalAmount());
            ps1.setString(6, booking.getPaymentStatus() != null ? booking.getPaymentStatus() : "CHUA_THANH_TOAN");
            ps1.setString(7, booking.getPaymentMethod());
            ps1.executeUpdate();

            ResultSet keys = ps1.getGeneratedKeys();
            int bookingId = -1;
            if (keys.next()) bookingId = keys.getInt(1);
            ps1.close();

            // Insert tickets
            PreparedStatement ps2 = conn.prepareStatement(
                "INSERT INTO tickets (booking_id, seat_id, price) VALUES (?,?,?)");
            for (int seatId : seatIds) {
                ps2.setInt(1, bookingId);
                ps2.setInt(2, seatId);
                ps2.setDouble(3, pricePerSeat);
                ps2.addBatch();
            }
            ps2.executeBatch();
            ps2.close();

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM bookings WHERE booking_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean updatePaymentStatus(int bookingId, String status, String method) {
        String sql = "UPDATE bookings SET payment_status = ?, payment_method = ? WHERE booking_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setString(2, method);
            ps.setInt(3, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking(rs.getInt("booking_id"), rs.getInt("showtime_id"),
            rs.getInt("customer_id"), rs.getInt("user_id"),
            rs.getString("booking_date"), rs.getDouble("total_amount"));
        try { b.setMovieTitle(rs.getString("movie_title")); } catch (SQLException ignored) {}
        try { b.setCustomerName(rs.getString("customer_name")); } catch (SQLException ignored) {}
        try { b.setStaffName(rs.getString("staff_name")); } catch (SQLException ignored) {}
        try { b.setShowDate(rs.getString("show_date")); } catch (SQLException ignored) {}
        try { b.setStartTime(rs.getString("start_time")); } catch (SQLException ignored) {}
        try { b.setPaymentStatus(rs.getString("payment_status")); } catch (SQLException ignored) {}
        try { b.setPaymentMethod(rs.getString("payment_method")); } catch (SQLException ignored) {}
        return b;
    }
}
