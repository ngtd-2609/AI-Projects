package controller;

import dao.*;
import model.*;
import java.util.List;

public class BookingController {
    private BookingDAO bookingDAO = new BookingDAO();
    private TicketDAO ticketDAO = new TicketDAO();
    private SeatDAO seatDAO = new SeatDAO();

    public List<Booking> getAll() { return bookingDAO.getAll(); }
    public List<Seat> getSeatsByRoom(int roomId) { return seatDAO.getByRoomId(roomId); }
    public List<Integer> getBookedSeatIds(int showtimeId) { return ticketDAO.getBookedSeatIds(showtimeId); }

    public boolean createBooking(Booking booking, List<Integer> seatIds, double pricePerSeat) {
        return bookingDAO.createBooking(booking, seatIds, pricePerSeat);
    }

    public boolean deleteBooking(int id) { return bookingDAO.delete(id); }
    public List<String> getSeatLabels(int bookingId) { return ticketDAO.getSeatLabelsByBookingId(bookingId); }
    public boolean updatePaymentStatus(int bookingId, String status, String method) {
        return bookingDAO.updatePaymentStatus(bookingId, status, method);
    }
}
