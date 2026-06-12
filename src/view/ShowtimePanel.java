package view;

import controller.ShowtimeController;
import controller.MovieController;
import controller.RoomController;
import model.*;
import util.Validator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShowtimePanel extends JPanel {
    private ShowtimeController controller = new ShowtimeController();
    private MovieController movieController = new MovieController();
    private RoomController roomController = new RoomController();
    private JTable table;
    private DefaultTableModel tableModel;

    public ShowtimePanel() {
        this(false);
    }

    public ShowtimePanel(boolean readOnly) {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel(readOnly ? "L\u1ecaCH CHI\u1ebeU" : "QU\u1ea2N L\u00dd L\u1ecaCH CHI\u1ebeU");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        add(title, BorderLayout.NORTH);

        String[] columns = {"ID", "Phim", "Ph\u00f2ng", "Ng\u00e0y chi\u1ebfu", "Gi\u1edd b\u1eaft \u0111\u1ea7u", "Gi\u1edd k\u1ebft th\u00fac", "Gi\u00e1 v\u00e9 (VN\u0110)"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Th\u00eam");
        JButton btnEdit = new JButton("S\u1eeda");
        JButton btnDelete = new JButton("X\u00f3a");
        JButton btnRefresh = new JButton("L\u00e0m m\u1edbi");
        btnAdd.addActionListener(e -> showForm(null));
        btnEdit.addActionListener(e -> editSelected());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> loadData());
        if (!readOnly) {
            btnPanel.add(btnAdd);
            btnPanel.add(btnEdit);
            btnPanel.add(btnDelete);
        }
        btnPanel.add(btnRefresh);
        add(btnPanel, BorderLayout.SOUTH);

        loadData();
    }

    private void loadData() {
        tableModel.setRowCount(0);
        for (Showtime s : controller.getAll()) {
            tableModel.addRow(new Object[]{s.getShowtimeId(), s.getMovieTitle(), s.getRoomName(),
                Validator.toDisplayDate(s.getShowDate()), s.getStartTime(), s.getEndTime(),
                String.format("%,.0f", s.getTicketPrice())});
        }
    }

    private void editSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Ch\u1ecdn l\u1ecbch chi\u1ebfu c\u1ea7n s\u1eeda!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        Showtime found = controller.getAll().stream().filter(s -> s.getShowtimeId() == id).findFirst().orElse(null);
        if (found != null) showForm(found);
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row < 0) { JOptionPane.showMessageDialog(this, "Ch\u1ecdn l\u1ecbch chi\u1ebfu c\u1ea7n x\u00f3a!"); return; }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "B\u1ea1n c\u00f3 ch\u1eafc mu\u1ed1n x\u00f3a?", "X\u00e1c nh\u1eadn", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (controller.delete(id)) loadData();
            else JOptionPane.showMessageDialog(this, "Kh\u00f4ng th\u1ec3 x\u00f3a! C\u00f3 th\u1ec3 \u0111\u00e3 c\u00f3 v\u00e9 \u0111\u1eb7t.");
        }
    }

    private void showForm(Showtime showtime) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            showtime == null ? "Th\u00eam l\u1ecbch chi\u1ebfu" : "S\u1eeda l\u1ecbch chi\u1ebfu", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Movie> movies = movieController.getAll();
        List<Room> rooms = roomController.getAll();
        JComboBox<Movie> cmbMovie = new JComboBox<>(movies.toArray(new Movie[0]));
        JComboBox<Room> cmbRoom = new JComboBox<>(rooms.toArray(new Room[0]));
        JTextField txtDate = new JTextField(15);
        txtDate.setToolTipText("DD/MM/YYYY");
        JTextField txtStart = new JTextField(15);
        txtStart.setToolTipText("HH:MM");
        JTextField txtEnd = new JTextField(15);
        txtEnd.setToolTipText("HH:MM");
        JTextField txtPrice = new JTextField(15);

        if (showtime != null) {
            for (int i = 0; i < movies.size(); i++)
                if (movies.get(i).getMovieId() == showtime.getMovieId()) cmbMovie.setSelectedIndex(i);
            for (int i = 0; i < rooms.size(); i++)
                if (rooms.get(i).getRoomId() == showtime.getRoomId()) cmbRoom.setSelectedIndex(i);
            txtDate.setText(Validator.toDisplayDate(showtime.getShowDate()));
            txtStart.setText(showtime.getStartTime());
            txtEnd.setText(showtime.getEndTime());
            txtPrice.setText(String.valueOf((int) showtime.getTicketPrice()));
        } else {
            txtDate.setText(Validator.todayDisplay());
        }

        String[] labels = {"Phim:", "Ph\u00f2ng:", "Ng\u00e0y (DD/MM/YYYY):", "Gi\u1edd b\u1eaft \u0111\u1ea7u (HH:MM):", "Gi\u1edd k\u1ebft th\u00fac (HH:MM):", "Gi\u00e1 v\u00e9 (VN\u0110):"};
        Component[] fields = {cmbMovie, cmbRoom, txtDate, txtStart, txtEnd, txtPrice};
        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            form.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            form.add(fields[i], gbc);
        }

        JButton btnSave = new JButton("L\u01b0u");
        gbc.gridx = 0; gbc.gridy = labels.length; gbc.gridwidth = 2;
        form.add(btnSave, gbc);

        btnSave.addActionListener(e -> {
            Movie selMovie = (Movie) cmbMovie.getSelectedItem();
            Room selRoom = (Room) cmbRoom.getSelectedItem();
            if (selMovie == null || selRoom == null || txtDate.getText().trim().isEmpty() ||
                txtStart.getText().trim().isEmpty() || txtEnd.getText().trim().isEmpty() || txtPrice.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui l\u00f2ng \u0111i\u1ec1n \u0111\u1ea7y \u0111\u1ee7 th\u00f4ng tin!"); return;
            }

            Showtime s = showtime != null ? showtime : new Showtime();
            s.setMovieId(selMovie.getMovieId());
            s.setRoomId(selRoom.getRoomId());
            s.setShowDate(Validator.toDatabaseDate(txtDate.getText().trim()));
            s.setStartTime(txtStart.getText().trim());
            s.setEndTime(txtEnd.getText().trim());
            try { s.setTicketPrice(Double.parseDouble(txtPrice.getText().trim())); }
            catch (NumberFormatException ex) { JOptionPane.showMessageDialog(dialog, "Gi\u00e1 v\u00e9 ph\u1ea3i l\u00e0 s\u1ed1!"); return; }

            boolean ok = showtime != null ? controller.update(s) : controller.insert(s);
            if (ok) { dialog.dispose(); loadData(); }
            else JOptionPane.showMessageDialog(dialog, "Tr\u00f9ng l\u1ecbch chi\u1ebfu! Ph\u00f2ng n\u00e0y \u0111\u00e3 c\u00f3 su\u1ea5t chi\u1ebfu trong khung gi\u1edd n\u00e0y.");
        });

        dialog.add(form);
        dialog.setVisible(true);
    }
}
