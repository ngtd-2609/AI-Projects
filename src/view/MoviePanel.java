package view;

import controller.MovieController;
import model.Movie;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MoviePanel extends JPanel {
    private MovieController controller = new MovieController();
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    public MoviePanel() {
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

        String[] columns = {"ID", "Tên phim", "Thể loại", "Đạo diễn", "Diễn viên", "Thời lượng (phút)", "Trạng thái"};
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
        btnAdd.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Movie m : controller.getAll()) {
            tableModel.addRow(new Object[]{m.getMovieId(), m.getTitle(), m.getGenre(),
                m.getDirector(), m.getActors(), m.getDurationMin(), m.getStatus()});
        }
    }

    private void search() {
        String kw = txtSearch.getText().trim();
        if (kw.isEmpty()) { loadData(); return; }
        tableModel.setRowCount(0);
        for (Movie m : controller.search(kw)) {
            tableModel.addRow(new Object[]{m.getMovieId(), m.getTitle(), m.getGenre(),
                m.getDirector(), m.getActors(), m.getDurationMin(), m.getStatus()});
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn phim cần sửa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        List<Movie> movies = controller.getAll();
        Movie found = movies.stream().filter(m -> m.getMovieId() == id).findFirst().orElse(null);
        if (found != null) showForm(found);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Chọn phim cần xóa!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa phim này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) loadData();
            else JOptionPane.showMessageDialog(this, "Không thể xóa! Phim có thể đang có lịch chiếu.");
        }
    }

    private void showForm(Movie movie) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            movie == null ? "Thêm phim" : "Sửa phim", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtTitle = new JTextField(20);
        JTextField txtGenre = new JTextField(20);
        JTextField txtDirector = new JTextField(20);
        JTextField txtActors = new JTextField(20);
        JTextField txtDuration = new JTextField(20);
        JTextArea txtDesc = new JTextArea(3, 20);
        JComboBox<String> cmbStatus = new JComboBox<>(new String[]{"DANG_CHIEU", "SAP_CHIEU", "NGUNG_CHIEU"});

        if (movie != null) {
            txtTitle.setText(movie.getTitle());
            txtGenre.setText(movie.getGenre());
            txtDirector.setText(movie.getDirector());
            txtActors.setText(movie.getActors());
            txtDuration.setText(String.valueOf(movie.getDurationMin()));
            txtDesc.setText(movie.getDescription());
            cmbStatus.setSelectedItem(movie.getStatus());
        }

        String[] labels = {"Tên phim:", "Thể loại:", "Đạo diễn:", "Diễn viên:", "Thời lượng (phút):", "Mô tả:", "Trạng thái:"};
        Component[] fields = {txtTitle, txtGenre, txtDirector, txtActors, txtDuration, new JScrollPane(txtDesc), cmbStatus};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }

        JButton btnSave = new JButton("Lưu");
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        form.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            if (txtTitle.getText().trim().isEmpty() || txtDuration.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Tên phim và thời lượng không được để trống!");
                return;
            }
            Movie m = movie != null ? movie : new Movie();
            m.setTitle(txtTitle.getText().trim());
            m.setGenre(txtGenre.getText().trim());
            m.setDirector(txtDirector.getText().trim());
            m.setActors(txtActors.getText().trim());
            try { m.setDurationMin(Integer.parseInt(txtDuration.getText().trim())); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(dialog, "Thời lượng phải là số!"); return; }
            m.setDescription(txtDesc.getText().trim());
            m.setStatus((String) cmbStatus.getSelectedItem());

            boolean ok = movie != null ? controller.update(m) : controller.insert(m);
            if (ok) { dialog.dispose(); loadData(); }
            else JOptionPane.showMessageDialog(dialog, "Thao tác thất bại!");
        });

        dialog.add(new JScrollPane(form));
        dialog.setVisible(true);
    }
}
