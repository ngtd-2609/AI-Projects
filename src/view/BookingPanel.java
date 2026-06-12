package view;

import controller.*;
import model.*;
import util.Validator;
import view.components.SeatMapPanel;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BookingPanel extends JPanel {
    private BookingController bookingController = new BookingController();
    private ShowtimeController showtimeController = new ShowtimeController();
    private CustomerController customerController = new CustomerController();
    private User currentUser;

    private JTable showtimeTable;
    private DefaultTableModel showtimeModel;
    private SeatMapPanel seatMapPanel;
    private JTextField txtCustName, txtCustPhone, txtCustEmail;
    private JLabel lblSelectedInfo;
    private Showtime selectedShowtime;

    private JTable bookingTable;
    private DefaultTableModel bookingModel;

    public BookingPanel(User user) {
        this.currentUser = user;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Đặt vé mới", createBookingTab());
        tabs.addTab("Lịch sử đặt vé", createHistoryTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel createBookingTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(350, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Chọn suất chiếu"));

        String[] cols = {"ID", "Phim", "Phòng", "Ngày", "Giờ"};
        showtimeModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        showtimeTable = new JTable(showtimeModel);
        showtimeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        showtimeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) onShowtimeSelected();
        });

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadShowtimes());
        filterPanel.add(btnRefresh);

        leftPanel.add(filterPanel, BorderLayout.NORTH);
        leftPanel.add(new JScrollPane(showtimeTable), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));

        seatMapPanel = new SeatMapPanel();
        seatMapPanel.setBorder(BorderFactory.createTitledBorder("Sơ đồ ghế"));
        seatMapPanel.setPreferredSize(new Dimension(0, 350));

        JPanel custPanel = new JPanel(new GridBagLayout());
        custPanel.setBorder(BorderFactory.createTitledBorder("Thông tin khách hàng"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 8, 5, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtCustName = new JTextField();
        txtCustPhone = new JTextField();
        txtCustEmail = new JTextField();
        if ("CUSTOMER".equals(currentUser.getRole())) {
            txtCustName.setText(currentUser.getFullname());
            if (currentUser.getUsername().contains("@")) {
                txtCustEmail.setText(currentUser.getUsername());
            }
        }

        // Row 0: Họ tên
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        custPanel.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        custPanel.add(txtCustName, gbc);

        // Row 0: SĐT
        gbc.gridx = 2; gbc.weightx = 0;
        custPanel.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.6;
        custPanel.add(txtCustPhone, gbc);

        // Row 1: Email
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        custPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0;
        custPanel.add(txtCustEmail, gbc);

        lblSelectedInfo = new JLabel("Chưa chọn ghế nào");
        lblSelectedInfo.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        custPanel.add(lblSelectedInfo, gbc);

        JButton btnConfirm = new JButton("XÁC NHẬN ĐẶT VÉ");
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 13));
        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
        custPanel.add(btnConfirm, gbc);
        btnConfirm.addActionListener(e -> confirmBooking());

        rightPanel.add(seatMapPanel, BorderLayout.CENTER);
        rightPanel.add(custPanel, BorderLayout.SOUTH);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);

        loadShowtimes();
        return panel;
    }

    private JPanel createHistoryTab() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] cols = {"ID", "Phim", "Ngày chiếu", "Giờ", "Khách hàng", "Nhân viên", "Ngày đặt", "Tổng tiền (VNĐ)"};
        bookingModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        bookingTable = new JTable(bookingModel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadBookingHistory());
        JButton btnDelete = new JButton("Hủy vé");
        btnDelete.addActionListener(e -> cancelBooking());
        btnPanel.add(btnRefresh); btnPanel.add(btnDelete);

        panel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        loadBookingHistory();
        return panel;
    }

    private void loadShowtimes() {
        showtimeModel.setRowCount(0);
        for (Showtime s : showtimeController.getAll()) {
            showtimeModel.addRow(new Object[]{s.getShowtimeId(), s.getMovieTitle(),
                s.getRoomName(), Validator.toDisplayDate(s.getShowDate()), s.getStartTime()});
        }
    }

    private void onShowtimeSelected() {
        int row = showtimeTable.getSelectedRow();
        if (row < 0) return;
        int id = (int) showtimeModel.getValueAt(row, 0);
        selectedShowtime = showtimeController.getAll().stream()
            .filter(s -> s.getShowtimeId() == id).findFirst().orElse(null);
        if (selectedShowtime != null) {
            List<Seat> seats = bookingController.getSeatsByRoom(selectedShowtime.getRoomId());
            List<Integer> booked = bookingController.getBookedSeatIds(selectedShowtime.getShowtimeId());
            seatMapPanel.loadSeats(seats, booked);
        }
    }

    private void confirmBooking() {
        if (selectedShowtime == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn suất chiếu!"); return;
        }
        if (seatMapPanel.getSelectedSeatIds().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất 1 ghế!"); return;
        }
        if (txtCustName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!"); return;
        }

        Customer cust = new Customer();
        cust.setFullname(txtCustName.getText().trim());
        cust.setPhone(txtCustPhone.getText().trim());
        cust.setEmail(txtCustEmail.getText().trim());
        int custId = customerController.insert(cust);
        if (custId < 0) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo khách hàng!"); return;
        }

        List<Integer> seatIds = new ArrayList<>(seatMapPanel.getSelectedSeatIds());
        double total = seatIds.size() * selectedShowtime.getTicketPrice();

        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận đặt " + seatIds.size() + " vé?\nTổng tiền: " + String.format("%,.0f", total) + " VNĐ",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        Booking booking = new Booking();
        booking.setShowtimeId(selectedShowtime.getShowtimeId());
        booking.setCustomerId(custId);
        booking.setUserId(currentUser.getUserId());
        booking.setBookingDate(java.time.LocalDate.now().toString());
        booking.setTotalAmount(total);

        if (bookingController.createBooking(booking, seatIds, selectedShowtime.getTicketPrice())) {
            JOptionPane.showMessageDialog(this, "Đặt vé thành công!\nSố vé: " + seatIds.size() +
                "\nTổng tiền: " + String.format("%,.0f", total) + " VNĐ");
            if (!"CUSTOMER".equals(currentUser.getRole())) {
                txtCustName.setText(""); txtCustPhone.setText(""); txtCustEmail.setText("");
            }
            onShowtimeSelected();
            loadBookingHistory();
        } else {
            JOptionPane.showMessageDialog(this, "Đặt vé thất bại!");
        }
    }

    private void cancelBooking() {
        int row = bookingTable.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn vé cần hủy!"); return; }
        int id = (int) bookingModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Hủy vé #" + id + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (bookingController.deleteBooking(id)) {
                loadBookingHistory();
                onShowtimeSelected();
            }
        }
    }

    private void loadBookingHistory() {
        bookingModel.setRowCount(0);
        for (Booking b : bookingController.getAll()) {
            if ("CUSTOMER".equals(currentUser.getRole()) && b.getUserId() != currentUser.getUserId()) {
                continue;
            }
            bookingModel.addRow(new Object[]{b.getBookingId(), b.getMovieTitle(),
                Validator.toDisplayDate(b.getShowDate()),
                b.getStartTime(), b.getCustomerName(), b.getStaffName(),
                Validator.toDisplayDate(b.getBookingDate()),
                String.format("%,.0f", b.getTotalAmount())});
        }
    }
}
