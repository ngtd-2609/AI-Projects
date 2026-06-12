package dao;

import model.Showtime;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {
    public List<Showtime> getAll() {
        List<Showtime> list = new ArrayList<>();
        String sql = "SELECT s.*, m.title as movie_title, r.room_name " +
            "FROM showtimes s JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id ORDER BY s.show_date, s.start_time";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Showtime> getByDate(String date) {
        List<Showtime> list = new ArrayList<>();
        String sql = "SELECT s.*, m.title as movie_title, r.room_name " +
            "FROM showtimes s JOIN movies m ON s.movie_id = m.movie_id " +
            "JOIN rooms r ON s.room_id = r.room_id WHERE s.show_date = ? ORDER BY s.start_time";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, date);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Showtime s) {
        String sql = "INSERT INTO showtimes (movie_id, room_id, show_date, start_time, end_time, ticket_price) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, s.getMovieId());
            ps.setInt(2, s.getRoomId());
            ps.setString(3, s.getShowDate());
            ps.setString(4, s.getStartTime());
            ps.setString(5, s.getEndTime());
            ps.setDouble(6, s.getTicketPrice());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Showtime s) {
        String sql = "UPDATE showtimes SET movie_id=?, room_id=?, show_date=?, start_time=?, end_time=?, ticket_price=? WHERE showtime_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, s.getMovieId());
            ps.setInt(2, s.getRoomId());
            ps.setString(3, s.getShowDate());
            ps.setString(4, s.getStartTime());
            ps.setString(5, s.getEndTime());
            ps.setDouble(6, s.getTicketPrice());
            ps.setInt(7, s.getShowtimeId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM showtimes WHERE showtime_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean hasConflict(int roomId, String date, String start, String end, int excludeId) {
        String sql = "SELECT COUNT(*) FROM showtimes WHERE room_id=? AND show_date=? AND showtime_id!=? " +
            "AND NOT (end_time <= ? OR start_time >= ?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setString(2, date);
            ps.setInt(3, excludeId);
            ps.setString(4, start);
            ps.setString(5, end);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private Showtime mapRow(ResultSet rs) throws SQLException {
        Showtime s = new Showtime(rs.getInt("showtime_id"), rs.getInt("movie_id"),
            rs.getInt("room_id"), rs.getString("show_date"), rs.getString("start_time"),
            rs.getString("end_time"), rs.getDouble("ticket_price"));
        try { s.setMovieTitle(rs.getString("movie_title")); } catch (SQLException ignored) {}
        try { s.setRoomName(rs.getString("room_name")); } catch (SQLException ignored) {}
        return s;
    }
}
