package dao;

import model.Seat;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {
    public List<Seat> getByRoomId(int roomId) {
        List<Seat> list = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE room_id = ? ORDER BY seat_label";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void generateSeats(int roomId, int totalSeats) {
        int cols = 8;
        int rows = (int) Math.ceil((double) totalSeats / cols);
        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO seats (room_id, seat_label, seat_type) VALUES (?, ?, ?)");
            int count = 0;
            for (int r = 0; r < rows && count < totalSeats; r++) {
                for (int c = 1; c <= cols && count < totalSeats; c++) {
                    String label = String.valueOf((char)('A' + r)) + c;
                    String type = (r >= rows - 2) ? "VIP" : "THUONG";
                    ps.setInt(1, roomId);
                    ps.setString(2, label);
                    ps.setString(3, type);
                    ps.addBatch();
                    count++;
                }
            }
            ps.executeBatch();
            ps.close();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void deleteByRoomId(int roomId) {
        String sql = "DELETE FROM seats WHERE room_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private Seat mapRow(ResultSet rs) throws SQLException {
        return new Seat(rs.getInt("seat_id"), rs.getInt("room_id"),
            rs.getString("seat_label"), rs.getString("seat_type"));
    }
}
