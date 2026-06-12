package view;

import controller.AuthController;
import model.User;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private User currentUser;

    public MainFrame(User user) {
        this.currentUser = user;
        setTitle("Quản Lý Rạp Chiếu Phim - " + user.getFullname());
        setSize(1100, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(51, 51, 51));
        headerPanel.setPreferredSize(new Dimension(0, 40));
        JLabel lblTitle = new JLabel("  HỆ THỐNG QUẢN LÝ RẠP CHIẾU PHIM");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel lblUser = new JLabel("Xin chào, " + user.getFullname() + " (" + user.getRole() + ")  ");
        lblUser.setForeground(Color.WHITE);
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.addActionListener(e -> {
            new AuthController().logout();
            dispose();
            new LoginFrame().setVisible(true);
        });
        userPanel.add(lblUser);
        userPanel.add(btnLogout);
        headerPanel.add(userPanel, BorderLayout.EAST);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(160, 0));
        sidebar.setBackground(new Color(60, 63, 65));

        String[] menuItems, cardNames;
        boolean isAdmin = "ADMIN".equals(currentUser.getRole());
        boolean isCustomer = "CUSTOMER".equals(currentUser.getRole());

        if (isAdmin) {
            menuItems = new String[]{"Quản lý Phim", "Phòng chiếu", "Lịch chiếu", "Đặt vé", "Khách hàng", "Thống kê", "Tài khoản"};
            cardNames = new String[]{"movie", "room", "showtime", "booking", "customer", "statistics", "user"};
        } else if (isCustomer) {
            menuItems = new String[]{"L\u1ecbch chi\u1ebfu", "\u0110\u1eb7t v\u00e9 online"};
            cardNames = new String[]{"showtime", "booking"};
        } else {
            menuItems = new String[]{"Lịch chiếu", "Đặt vé", "Khách hàng"};
            cardNames = new String[]{"showtime", "booking", "customer"};
        }

        for (int i = 0; i < menuItems.length; i++) {
            JButton btn = new JButton(menuItems[i]);
            btn.setMaximumSize(new Dimension(160, 40));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setFocusPainted(false);
            final String card = cardNames[i];
            btn.addActionListener(e -> cardLayout.show(contentPanel, card));
            sidebar.add(Box.createVerticalStrut(5));
            sidebar.add(btn);
        }

        // Content
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(new MoviePanel(), "movie");
        contentPanel.add(new RoomPanel(), "room");
        contentPanel.add(new ShowtimePanel(isCustomer), "showtime");
        contentPanel.add(new BookingPanel(currentUser), "booking");
        contentPanel.add(new CustomerPanel(), "customer");
        contentPanel.add(new StatisticsPanel(), "statistics");
        contentPanel.add(new UserPanel(currentUser), "user");

        // Layout
        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
    }
}
