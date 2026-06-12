package controller;

import dao.MovieDAO;
import model.Movie;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * MovieController implements Manageable và Searchable interface.
 * Đây là ví dụ về Interface và Comparator trong OOP.
 */
public class MovieController implements Manageable<Movie>, Searchable<Movie> {
    private MovieDAO movieDAO = new MovieDAO();

    @Override
    public ArrayList<Movie> getAll() { return new ArrayList<>(movieDAO.getAll()); }

    @Override
    public ArrayList<Movie> search(String keyword) { return new ArrayList<>(movieDAO.search(keyword)); }

    @Override
    public boolean add(Movie m) { return movieDAO.insert(m); }

    // Giữ method cũ để code View không bị lỗi
    public boolean insert(Movie m) { return add(m); }

    @Override
    public boolean update(Movie m) { return movieDAO.update(m); }

    @Override
    public boolean delete(int id) { return movieDAO.delete(id); }

    /** Sắp xếp phim theo tên A-Z bằng Comparator */
    public ArrayList<Movie> getAllSortedByTitle() {
        ArrayList<Movie> list = getAll();
        list.sort(Comparator.comparing(Movie::getTitle));
        return list;
    }

    /** Sắp xếp phim theo thời lượng tăng dần */
    public ArrayList<Movie> getAllSortedByDuration() {
        ArrayList<Movie> list = getAll();
        list.sort(Comparator.comparingInt(Movie::getDurationMin));
        return list;
    }

    /** Sắp xếp phim theo thể loại rồi theo tên */
    public ArrayList<Movie> getAllSortedByGenreThenTitle() {
        ArrayList<Movie> list = getAll();
        list.sort(Comparator.comparing(Movie::getGenre, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Movie::getTitle));
        return list;
    }
}
