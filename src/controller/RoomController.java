package controller;

import dao.RoomDAO;
import dao.SeatDAO;
import model.Room;
import java.util.List;

public class RoomController {
    private RoomDAO roomDAO = new RoomDAO();
    private SeatDAO seatDAO = new SeatDAO();

    public List<Room> getAll() { return roomDAO.getAll(); }

    public boolean insert(Room r) {
        int roomId = roomDAO.insert(r);
        if (roomId > 0) {
            seatDAO.generateSeats(roomId, r.getTotalSeats());
            return true;
        }
        return false;
    }

    public boolean update(Room r) {
        boolean ok = roomDAO.update(r);
        if (ok) {
            seatDAO.deleteByRoomId(r.getRoomId());
            seatDAO.generateSeats(r.getRoomId(), r.getTotalSeats());
        }
        return ok;
    }

    public boolean delete(int id) {
        seatDAO.deleteByRoomId(id);
        return roomDAO.delete(id);
    }
}
