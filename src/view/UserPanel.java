package view;

import controller.UserController;
import model.User;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UserPanel extends JPanel {
    private UserController controller = new UserController();
    private JTable table;
    private DefaultTableModel tableModel;
    private User currentUser;

    public UserPanel(User currentUser) {
        this.currentUser = currentUser;
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top panel
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Thêm tài khoản");
        btnAdd.addActionListener(e -> showForm(null));
        topPanel.add(btnAdd);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Tên đăng nhập", "Mật khẩu", "Họ tên", "Vai trò"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Bottom buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (User u : controller.getAll()) {
            tableModel.addRow(new Object[]{u.getUserId(), u.getUsername(),
                u.getPassword(), u.getFullname(), u.getRole()});
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn tài khoản cần sửa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        User found = controller.getAll().stream().filter(u -> u.getUserId() == id).findFirst().orElse(null);
        if (found != null) showForm(found);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn tài khoản cần xóa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        String role = (String) tableModel.getValueAt(row, 4);

        // Không cho xóa tài khoản ADMIN
        if ("ADMIN".equals(role)) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản ADMIN!");
            return;
        }
        // Không cho xóa chính mình
        if (id == currentUser.getUserId()) {
            JOptionPane.showMessageDialog(this, "Không thể xóa tài khoản đang đăng nhập!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tài khoản này?",
            "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) loadData();
            else JOptionPane.showMessageDialog(this, "Không thể xóa! Tài khoản có liên kết với đơn đặt vé.");
        }
    }

    private void showForm(User user) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            user == null ? "Thêm tài khoản" : "Sửa tài khoản", true);
        dialog.setSize(380, 240);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtUsername = new JTextField(18);
        JPasswordField txtPassword = new JPasswordField(18);
        JTextField txtFullname = new JTextField(18);

        if (user != null) {
            txtUsername.setText(user.getUsername());
            txtPassword.setText(user.getPassword());
            txtFullname.setText(user.getFullname());
        }

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1; form.add(txtUsername, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; form.add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; form.add(txtFullname, gbc);

        JButton btnSave = new JButton("Lưu");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            String username = txtUsername.getText().trim();
            String password = new String(txtPassword.getPassword()).trim();
            String fullname = txtFullname.getText().trim();

            if (username.isEmpty() || password.isEmpty() || fullname.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }

            User u = user != null ? user : new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setFullname(fullname);
            if (user != null) {
                u.setRole(user.getRole());
            } else {
                u.setRole("STAFF");
            }

            boolean ok;
            if (user != null) {
                ok = controller.update(u);
            } else {
                ok = controller.insert(u);
            }

            if (ok) {
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Thao tác thất bại! Tên đăng nhập có thể đã tồn tại.");
            }
        });

        dialog.add(form);
        dialog.setVisible(true);
    }
}
