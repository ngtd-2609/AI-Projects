package dao;

import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    public List<Integer> getBookedSeatIds(int showtimeId) {
        List<Integer> list = new ArrayList<>();
        String sql = "SELECT t.seat_id FROM tickets t " +
            "JOIN bookings b ON t.booking_id = b.booking_id " +
            "WHERE b.showtime_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, showtimeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getInt("seat_id"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<String> getSeatLabelsByBookingId(int bookingId) {
        List<String> list = new ArrayList<>();
        String sql = "SELECT s.seat_label FROM tickets t " +
            "JOIN seats s ON t.seat_id = s.seat_id WHERE t.booking_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(rs.getString("seat_label"));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}
