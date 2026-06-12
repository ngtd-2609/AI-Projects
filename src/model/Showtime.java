package model;

import java.io.Serializable;

public class Showtime implements Serializable {
    private static final long serialVersionUID = 8L;

    private int showtimeId;
    private int movieId;
    private int roomId;
    private String showDate;
    private String startTime;
    private String endTime;
    private double ticketPrice;
    // Extra fields for display
    private String movieTitle;
    private String roomName;

    public Showtime() {}

    public Showtime(int showtimeId, int movieId, int roomId, String showDate,
                    String startTime, String endTime, double ticketPrice) {
        this.showtimeId = showtimeId;
        this.movieId = movieId;
        this.roomId = roomId;
        this.showDate = showDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.ticketPrice = ticketPrice;
    }

    public int getShowtimeId() { return showtimeId; }
    public void setShowtimeId(int showtimeId) { this.showtimeId = showtimeId; }
    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public String getShowDate() { return showDate; }
    public void setShowDate(String showDate) { this.showDate = showDate; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public double getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(double ticketPrice) { this.ticketPrice = ticketPrice; }
    public String getMovieTitle() { return movieTitle; }
    public void setMovieTitle(String movieTitle) { this.movieTitle = movieTitle; }
    public String getRoomName() { return roomName; }
    public void setRoomName(String roomName) { this.roomName = roomName; }

    @Override
    public String toString() {
        return (movieTitle != null ? movieTitle : "Phim #" + movieId) + " - " + showDate + " " + startTime;
    }
}
