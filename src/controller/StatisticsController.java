package controller;

import dao.StatisticsDAO;
import java.util.List;

public class StatisticsController {
    private StatisticsDAO statisticsDAO = new StatisticsDAO();

    public List<Object[]> getRevenueByDateRange(String from, String to) {
        return statisticsDAO.getRevenueByDateRange(from, to);
    }

    public List<Object[]> getRevenueByMovie() {
        return statisticsDAO.getRevenueByMovie();
    }

    public double getTotalRevenue() { return statisticsDAO.getTotalRevenue(); }
    public int getTotalTickets() { return statisticsDAO.getTotalTickets(); }
}
