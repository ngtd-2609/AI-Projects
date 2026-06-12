package dao;

import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatisticsDAO {
    // Returns: [date, revenue, ticketCount]
    public List<Object[]> getRevenueByDateRange(String from, String to) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT b.booking_date, SUM(t.price) as revenue, COUNT(t.ticket_id) as tickets " +
            "FROM bookings b JOIN tickets t ON b.booking_id = t.booking_id " +
            "WHERE b.booking_date BETWEEN ? AND ? " +
            "GROUP BY b.booking_date ORDER BY b.booking_date";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, from);
            ps.setString(2, to);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Object[]{rs.getString("booking_date"),
                    rs.getDouble("revenue"), rs.getInt("tickets")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // Returns: [movieTitle, revenue, ticketCount]
    public List<Object[]> getRevenueByMovie() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT m.title, SUM(t.price) as revenue, COUNT(t.ticket_id) as tickets " +
            "FROM bookings b JOIN showtimes s ON b.showtime_id = s.showtime_id " +
            "JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN tickets t ON b.booking_id = t.booking_id " +
            "GROUP BY m.movie_id ORDER BY revenue DESC";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Object[]{rs.getString("title"),
                    rs.getDouble("revenue"), rs.getInt("tickets")});
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public double getTotalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_amount), 0) FROM bookings";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getTotalTickets() {
        String sql = "SELECT COUNT(*) FROM tickets";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}
