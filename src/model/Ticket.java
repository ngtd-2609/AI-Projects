package model;

import java.io.Serializable;

public class Ticket implements Serializable {
    private static final long serialVersionUID = 10L;

    private int ticketId;
    private int bookingId;
    private int seatId;
    private double price;
    // Extra for display
    private String seatLabel;

    public Ticket() {}

    public Ticket(int ticketId, int bookingId, int seatId, double price) {
        this.ticketId = ticketId;
        this.bookingId = bookingId;
        this.seatId = seatId;
        this.price = price;
    }

    public int getTicketId() { return ticketId; }
    public void setTicketId(int ticketId) { this.ticketId = ticketId; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getSeatLabel() { return seatLabel; }
    public void setSeatLabel(String seatLabel) { this.seatLabel = seatLabel; }
}
