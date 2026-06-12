package view.components;

import model.Seat;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class SeatMapPanel extends JPanel {
    private Set<Integer> bookedSeatIds;
    private Set<Integer> selectedSeatIds = new HashSet<>();
    private Map<Integer, JButton> seatButtons = new HashMap<>();

    private static final Color COLOR_AVAILABLE = new Color(76, 175, 80);
    private static final Color COLOR_BOOKED = new Color(244, 67, 54);
    private static final Color COLOR_SELECTED = new Color(255, 193, 7);
    private static final Color COLOR_VIP = new Color(33, 150, 243);

    public SeatMapPanel() {
        setLayout(new BorderLayout());
    }

    public void loadSeats(List<Seat> seats, List<Integer> bookedIds) {
        this.bookedSeatIds = new HashSet<>(bookedIds);
        this.selectedSeatIds.clear();
        this.seatButtons.clear();
        removeAll();

        if (seats.isEmpty()) {
            add(new JLabel("Không có ghế", SwingConstants.CENTER));
            revalidate(); repaint();
            return;
        }

        // Legend
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        legend.add(createLegend(COLOR_AVAILABLE, "Trống"));
        legend.add(createLegend(COLOR_BOOKED, "Đã đặt"));
        legend.add(createLegend(COLOR_SELECTED, "Đang chọn"));
        legend.add(createLegend(COLOR_VIP, "VIP (trống)"));
        add(legend, BorderLayout.NORTH);

        // Screen label
        JPanel screenPanel = new JPanel();
        JLabel screenLabel = new JLabel("=====  MÀN HÌNH  =====");
        screenLabel.setFont(new Font("Arial", Font.BOLD, 12));
        screenPanel.add(screenLabel);

        // Calculate grid
        int cols = 8;
        int rows = (int) Math.ceil((double) seats.size() / cols);

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 4, 4));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        int idx = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (idx < seats.size()) {
                    Seat seat = seats.get(idx);
                    JButton btn = new JButton(seat.getSeatLabel());
                    btn.setFont(new Font("Arial", Font.PLAIN, 10));
                    btn.setPreferredSize(new Dimension(55, 35));
                    btn.setFocusPainted(false);

                    if (bookedSeatIds.contains(seat.getSeatId())) {
                        btn.setBackground(COLOR_BOOKED);
                        btn.setForeground(Color.WHITE);
                        btn.setEnabled(false);
                    } else if ("VIP".equals(seat.getSeatType())) {
                        btn.setBackground(COLOR_VIP);
                        btn.setForeground(Color.WHITE);
                    } else {
                        btn.setBackground(COLOR_AVAILABLE);
                        btn.setForeground(Color.WHITE);
                    }

                    final int seatId = seat.getSeatId();
                    btn.addActionListener(e -> toggleSeat(seatId, btn, seat.getSeatType()));
                    seatButtons.put(seatId, btn);
                    gridPanel.add(btn);
                    idx++;
                } else {
                    gridPanel.add(new JLabel());
                }
            }
        }

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(screenPanel, BorderLayout.NORTH);
        centerPanel.add(gridPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private void toggleSeat(int seatId, JButton btn, String seatType) {
        if (selectedSeatIds.contains(seatId)) {
            selectedSeatIds.remove(Integer.valueOf(seatId));
            btn.setBackground("VIP".equals(seatType) ? COLOR_VIP : COLOR_AVAILABLE);
        } else {
            selectedSeatIds.add(seatId);
            btn.setBackground(COLOR_SELECTED);
        }
        btn.setForeground(selectedSeatIds.contains(seatId) ? Color.BLACK : Color.WHITE);
    }

    public Set<Integer> getSelectedSeatIds() { return selectedSeatIds; }

    private JPanel createLegend(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        JLabel colorBox = new JLabel("  ");
        colorBox.setOpaque(true);
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(16, 16));
        p.add(colorBox);
        p.add(new JLabel(text));
        return p;
    }
}
