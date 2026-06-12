package dao;

import model.Movie;
import util.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
    public List<Movie> getAll() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies ORDER BY movie_id ASC";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public Movie getById(int id) {
        String sql = "SELECT * FROM movies WHERE movie_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<Movie> search(String keyword) {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE title LIKE ? OR genre LIKE ? OR director LIKE ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            String kw = "%" + keyword + "%";
            ps.setString(1, kw); ps.setString(2, kw); ps.setString(3, kw);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public boolean insert(Movie m) {
        String sql = "INSERT INTO movies (title, genre, director, actors, duration_min, description, poster_path, status) VALUES (?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            setParams(ps, m);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean update(Movie m) {
        String sql = "UPDATE movies SET title=?, genre=?, director=?, actors=?, duration_min=?, description=?, poster_path=?, status=? WHERE movie_id=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            setParams(ps, m);
            ps.setInt(9, m.getMovieId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM movies WHERE movie_id = ?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    private void setParams(PreparedStatement ps, Movie m) throws SQLException {
        ps.setString(1, m.getTitle());
        ps.setString(2, m.getGenre());
        ps.setString(3, m.getDirector());
        ps.setString(4, m.getActors());
        ps.setInt(5, m.getDurationMin());
        ps.setString(6, m.getDescription());
        ps.setString(7, m.getPosterPath());
        ps.setString(8, m.getStatus());
    }

    private Movie mapRow(ResultSet rs) throws SQLException {
        return new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("genre"),
            rs.getString("director"), rs.getString("actors"), rs.getInt("duration_min"),
            rs.getString("description"), rs.getString("poster_path"), rs.getString("status"));
    }
}
