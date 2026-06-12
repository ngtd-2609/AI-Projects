package view;

import controller.StatisticsController;
import util.Validator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StatisticsPanel extends JPanel {
    private StatisticsController controller = new StatisticsController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtFrom, txtTo;
    private JLabel lblTotalRevenue, lblTotalTickets;

    public StatisticsPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tổng quan
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Tổng quan"));
        lblTotalRevenue = new JLabel("Tổng doanh thu: 0 VNĐ");
        lblTotalRevenue.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalTickets = new JLabel("Tổng vé bán: 0");
        lblTotalTickets.setFont(new Font("Arial", Font.BOLD, 14));
        summaryPanel.add(lblTotalRevenue);
        summaryPanel.add(lblTotalTickets);

        // Bộ lọc
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Từ ngày:"));
        txtFrom = new JTextField(10);
        txtFrom.setText(Validator.firstDayOfMonthDisplay());
        filterPanel.add(txtFrom);
        filterPanel.add(new JLabel("Đến ngày:"));
        txtTo = new JTextField(10);
        txtTo.setText(Validator.todayDisplay());
        filterPanel.add(txtTo);
        JButton btnFilter = new JButton("Lọc theo ngày");
        btnFilter.addActionListener(e -> filterByDate());
        filterPanel.add(btnFilter);
        JButton btnByMovie = new JButton("Theo phim");
        btnByMovie.addActionListener(e -> showByMovie());
        filterPanel.add(btnByMovie);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(summaryPanel, BorderLayout.NORTH);
        topPanel.add(filterPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        // Bảng thống kê
        String[] columns = {"Mục", "Doanh thu (VNĐ)", "Số vé"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        loadSummary();
        filterByDate();
    }

    private void loadSummary() {
        double revenue = controller.getTotalRevenue();
        int tickets = controller.getTotalTickets();
        lblTotalRevenue.setText("Tổng doanh thu: " + String.format("%,.0f", revenue) + " VNĐ");
        lblTotalTickets.setText("Tổng vé bán: " + tickets);
    }

    private void filterByDate() {
        tableModel.setRowCount(0);
        String from = Validator.toDatabaseDate(txtFrom.getText().trim());
        String to = Validator.toDatabaseDate(txtTo.getText().trim());
        List<Object[]> data = controller.getRevenueByDateRange(from, to);
        for (Object[] row : data) {
            tableModel.addRow(new Object[]{Validator.toDisplayDate((String) row[0]),
                String.format("%,.0f", (double) row[1]), row[2]});
        }
        loadSummary();
    }

    private void showByMovie() {
        tableModel.setRowCount(0);
        List<Object[]> data = controller.getRevenueByMovie();
        for (Object[] row : data) {
            tableModel.addRow(new Object[]{row[0], String.format("%,.0f", (double) row[1]), row[2]});
        }
    }
}
