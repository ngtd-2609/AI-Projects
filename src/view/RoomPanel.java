package view;

import controller.RoomController;
import model.Room;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class RoomPanel extends JPanel {
    private RoomController controller = new RoomController();
    private JTable table;
    private DefaultTableModel tableModel;

    public RoomPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("QUẢN LÝ PHÒNG CHIẾU");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Tên phòng", "Số ghế", "Loại phòng"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnRefresh = new JButton("Làm mới");
        btnAdd.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Room r : controller.getAll()) {
            tableModel.addRow(new Object[]{r.getRoomId(), r.getRoomName(), r.getTotalSeats(), r.getRoomType()});
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn phòng cần sửa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Room found = controller.getAll().stream().filter(r -> r.getRoomId() == id).findFirst().orElse(null);
        if (found != null) showForm(found);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn phòng cần xóa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phòng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) loadData();
            else JOptionPane.showMessageDialog(this, "Không thể xóa!");
        }
    }

    private void showForm(Room room) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            room == null ? "Thêm phòng" : "Sửa phòng", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = new JTextField(15);
        JTextField txtSeats = new JTextField(15);
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"2D", "3D", "IMAX"});

        if (room != null) {
            txtName.setText(room.getRoomName());
            txtSeats.setText(String.valueOf(room.getTotalSeats()));
            cmbType.setSelectedItem(room.getRoomType());
        }

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Tên phòng:"), gbc);
        gbc.gridx = 1; form.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("Số ghế:"), gbc);
        gbc.gridx = 1; form.add(txtSeats, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Loại phòng:"), gbc);
        gbc.gridx = 1; form.add(cmbType, gbc);

        JButton btnSave = new JButton("Lưu");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty() || txtSeats.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Không được để trống!"); return;
            }
            Room r = room != null ? room : new Room();
            r.setRoomName(txtName.getText().trim());
            try { r.setTotalSeats(Integer.parseInt(txtSeats.getText().trim())); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(dialog, "Số ghế phải là số!"); return; }
            r.setRoomType((String) cmbType.getSelectedItem());

            boolean ok = room != null ? controller.update(r) : controller.insert(r);
            if (ok) { dialog.dispose(); loadData(); }
            else JOptionPane.showMessageDialog(dialog, "Thao tác thất bại!");
        });

        dialog.add(form);
        dialog.setVisible(true);
    }
}
