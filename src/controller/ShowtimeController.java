package controller;

import dao.ShowtimeDAO;
import model.Showtime;
import java.util.List;

public class ShowtimeController {
    private ShowtimeDAO showtimeDAO = new ShowtimeDAO();

    public List<Showtime> getAll() { return showtimeDAO.getAll(); }
    public List<Showtime> getByDate(String date) { return showtimeDAO.getByDate(date); }

    public boolean insert(Showtime s) {
        if (showtimeDAO.hasConflict(s.getRoomId(), s.getShowDate(), s.getStartTime(), s.getEndTime(), 0)) {
            return false;
        }
        return showtimeDAO.insert(s);
    }

    public boolean update(Showtime s) {
        if (showtimeDAO.hasConflict(s.getRoomId(), s.getShowDate(), s.getStartTime(), s.getEndTime(), s.getShowtimeId())) {
            return false;
        }
        return showtimeDAO.update(s);
    }

    public boolean delete(int id) { return showtimeDAO.delete(id); }
}
