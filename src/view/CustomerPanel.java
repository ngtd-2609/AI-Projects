package view;

import controller.CustomerController;
import model.Customer;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerPanel extends JPanel {
    private CustomerController controller = new CustomerController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public CustomerPanel() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Tìm kiếm:"));
        txtSearch = new JTextField(20);
        topPanel.add(txtSearch);
        JButton btnSearch = new JButton("Tìm");
        btnSearch.addActionListener(e -> search());
        topPanel.add(btnSearch);
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);

        String[] columns = {"ID", "Họ tên", "Số điện thoại", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnPanel.add(btnEdit); btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Customer c : controller.getAll()) {
            tableModel.addRow(new Object[]{c.getCustomerId(), c.getFullname(), c.getPhone(), c.getEmail()});
        }
    }

    private void search() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }
        tableModel.setRowCount(0);
        for (Customer c : controller.search(kw)) {
            tableModel.addRow(new Object[]{c.getCustomerId(), c.getFullname(), c.getPhone(), c.getEmail()});
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn khách hàng cần sửa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Customer found = controller.getAll().stream().filter(c -> c.getCustomerId() == id).findFirst().orElse(null);
        if (found != null) showForm(found);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn khách hàng cần xóa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) loadData();
            else JOptionPane.showMessageDialog(this, "Không thể xóa!");
        }
    }

    private void showForm(Customer customer) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            customer == null ? "Thêm khách hàng" : "Sửa khách hàng", true);
        dialog.setSize(350, 220);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtName = new JTextField(15);
        JTextField txtPhone = new JTextField(15);
        JTextField txtEmail = new JTextField(15);

        if (customer != null) {
            txtName.setText(customer.getFullname());
            txtPhone.setText(customer.getPhone());
            txtEmail.setText(customer.getEmail());
        }

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Họ tên:"), gbc);
        gbc.gridx = 1; form.add(txtName, gbc);
        gbc.gridx = 0; gbc.gridy = 1; form.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 1; form.add(txtPhone, gbc);
        gbc.gridx = 0; gbc.gridy = 2; form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; form.add(txtEmail, gbc);

        JButton btnSave = new JButton("Lưu");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        form.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            if (txtName.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tên không được để trống!"); return;
            }
            Customer c = customer != null ? customer : new Customer();
            c.setFullname(txtName.getText().trim());
            c.setPhone(txtPhone.getText().trim());
            c.setEmail(txtEmail.getText().trim());

            boolean ok;
            if (customer != null) { ok = controller.update(c); }
            else { ok = controller.insert(c) > 0; }
            if (ok) { dialog.dispose(); loadData(); }
            else JOptionPane.showMessageDialog(dialog, "Thao tác thất bại!");
        });

        dialog.add(form);
        dialog.setVisible(true);
    }
}
