package util;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_URL = "jdbc:sqlite:data/cinema.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public static void initDatabase() {
        try {
            Connection conn = getConnection();
            Statement stmt = conn.createStatement();

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "user_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "fullname TEXT NOT NULL," +
                "role TEXT NOT NULL DEFAULT 'STAFF')");

            stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                "movie_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "genre TEXT," +
                "director TEXT," +
                "actors TEXT," +
                "duration_min INTEGER NOT NULL," +
                "description TEXT," +
                "poster_path TEXT," +
                "status TEXT NOT NULL DEFAULT 'DANG_CHIEU')");

            stmt.execute("CREATE TABLE IF NOT EXISTS rooms (" +
                "room_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_name TEXT NOT NULL UNIQUE," +
                "total_seats INTEGER NOT NULL," +
                "room_type TEXT NOT NULL DEFAULT '2D')");

            stmt.execute("CREATE TABLE IF NOT EXISTS seats (" +
                "seat_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "room_id INTEGER NOT NULL," +
                "seat_label TEXT NOT NULL," +
                "seat_type TEXT NOT NULL DEFAULT 'THUONG'," +
                "FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE CASCADE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS showtimes (" +
                "showtime_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "movie_id INTEGER NOT NULL," +
                "room_id INTEGER NOT NULL," +
                "show_date TEXT NOT NULL," +
                "start_time TEXT NOT NULL," +
                "end_time TEXT NOT NULL," +
                "ticket_price REAL NOT NULL," +
                "FOREIGN KEY (movie_id) REFERENCES movies(movie_id)," +
                "FOREIGN KEY (room_id) REFERENCES rooms(room_id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "fullname TEXT NOT NULL," +
                "phone TEXT," +
                "email TEXT)");

            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (" +
                "booking_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "showtime_id INTEGER NOT NULL," +
                "customer_id INTEGER NOT NULL," +
                "user_id INTEGER," +
                "booking_date TEXT NOT NULL," +
                "total_amount REAL NOT NULL," +
                "payment_status TEXT NOT NULL DEFAULT 'CHUA_THANH_TOAN'," +
                "payment_method TEXT DEFAULT NULL," +
                "FOREIGN KEY (showtime_id) REFERENCES showtimes(showtime_id)," +
                "FOREIGN KEY (customer_id) REFERENCES customers(customer_id)," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id))");

            // Migrate existing databases: add new columns if missing
            try {
                stmt.execute("ALTER TABLE bookings ADD COLUMN payment_status TEXT NOT NULL DEFAULT 'DA_THANH_TOAN'");
            } catch (SQLException ignored) {} // column already exists
            try {
                stmt.execute("ALTER TABLE bookings ADD COLUMN payment_method TEXT DEFAULT 'TIEN_MAT'");
            } catch (SQLException ignored) {} // column already exists

            stmt.execute("CREATE TABLE IF NOT EXISTS tickets (" +
                "ticket_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "booking_id INTEGER NOT NULL," +
                "seat_id INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE CASCADE," +
                "FOREIGN KEY (seat_id) REFERENCES seats(seat_id))");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users");
            if (rs.next() && rs.getInt(1) == 0) {
                seedData(conn);
            }
            rs.close();
            ensureDefaultCustomerAccount(conn);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void ensureDefaultCustomerAccount(Connection conn) throws SQLException {
        String sql = "INSERT OR IGNORE INTO users (username, password, fullname, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "khachhang");
            ps.setString(2, "kh123");
            ps.setString(3, "Khách Hàng");
            ps.setString(4, "CUSTOMER");
            ps.executeUpdate();
        }
    }

    private static void seedData(Connection conn) throws SQLException {
        // Tài khoản
        PreparedStatement psUser = conn.prepareStatement(
            "INSERT INTO users (username, password, fullname, role) VALUES (?, ?, ?, ?)");
        String[][] users = {
            {"admin@123", "123456789", "Quản Trị Viên", "ADMIN"},
            {"nhanvien", "nv123", "Nguyễn Văn A", "STAFF"},
            {"khachhang", "kh123", "Khách Hàng", "CUSTOMER"}
        };
        for (String[] u : users) {
            psUser.setString(1, u[0]); psUser.setString(2, u[1]);
            psUser.setString(3, u[2]); psUser.setString(4, u[3]);
            psUser.executeUpdate();
        }
        psUser.close();

        // Phim
        PreparedStatement psMovie = conn.prepareStatement(
            "INSERT INTO movies (title, genre, director, actors, duration_min, description, status) VALUES (?,?,?,?,?,?,?)");
        Object[][] movies = {
            {"Lật Mặt 7", "Hành động", "Lý Hải", "Lý Hải, Kiều Minh Tuấn", 130, "Phim hành động Việt Nam", "DANG_CHIEU"},
            {"Mai", "Tâm lý", "Trấn Thành", "Phương Anh Đào, Tuấn Trần", 131, "Phim tâm lý tình cảm", "DANG_CHIEU"},
            {"Avengers: Endgame", "Hành động, Phiêu lưu", "Russo Brothers", "Robert Downey Jr., Chris Evans", 181, "Siêu anh hùng Marvel", "DANG_CHIEU"},
            {"Doraemon: Nobita và vùng đất lý tưởng", "Hoạt hình", "Takumi Doyama", "Doraemon, Nobita", 108, "Phim hoạt hình Nhật Bản", "DANG_CHIEU"},
            {"Kungfu Panda 4", "Hoạt hình, Hài", "Mike Mitchell", "Jack Black, Awkwafina", 94, "Phim hoạt hình hài hước", "DANG_CHIEU"},
            {"Godzilla x Kong", "Hành động, Khoa học", "Adam Wingard", "Rebecca Hall, Dan Stevens", 115, "Phim quái vật", "SAP_CHIEU"}
        };
        for (Object[] m : movies) {
            psMovie.setString(1, (String) m[0]); psMovie.setString(2, (String) m[1]);
            psMovie.setString(3, (String) m[2]); psMovie.setString(4, (String) m[3]);
            psMovie.setInt(5, (int) m[4]); psMovie.setString(6, (String) m[5]);
            psMovie.setString(7, (String) m[6]);
            psMovie.executeUpdate();
        }
        psMovie.close();

        // Phòng chiếu
        PreparedStatement psRoom = conn.prepareStatement(
            "INSERT INTO rooms (room_name, total_seats, room_type) VALUES (?, ?, ?)");
        String[][] rooms = {{"Phòng 1", "48", "2D"}, {"Phòng 2", "48", "3D"}, {"Phòng 3", "64", "IMAX"}};
        for (String[] r : rooms) {
            psRoom.setString(1, r[0]); psRoom.setInt(2, Integer.parseInt(r[1])); psRoom.setString(3, r[2]);
            psRoom.executeUpdate();
        }
        psRoom.close();

        // Ghế
        generateSeats(conn, 1, 6, 8);
        generateSeats(conn, 2, 6, 8);
        generateSeats(conn, 3, 8, 8);

        // Suất chiếu
        String today = java.time.LocalDate.now().toString();
        String tomorrow = java.time.LocalDate.now().plusDays(1).toString();
        PreparedStatement psShow = conn.prepareStatement(
            "INSERT INTO showtimes (movie_id, room_id, show_date, start_time, end_time, ticket_price) VALUES (?,?,?,?,?,?)");
        Object[][] shows = {
            {1, 1, today, "09:00", "11:10", 75000.0},
            {2, 1, today, "13:00", "15:11", 75000.0},
            {3, 2, today, "10:00", "13:01", 90000.0},
            {4, 3, today, "14:00", "15:48", 120000.0},
            {5, 1, tomorrow, "09:30", "11:04", 75000.0},
            {1, 2, tomorrow, "15:00", "17:10", 90000.0}
        };
        for (Object[] s : shows) {
            psShow.setInt(1, (int) s[0]); psShow.setInt(2, (int) s[1]);
            psShow.setString(3, (String) s[2]); psShow.setString(4, (String) s[3]);
            psShow.setString(5, (String) s[4]); psShow.setDouble(6, (double) s[5]);
            psShow.executeUpdate();
        }
        psShow.close();

    }

    private static void generateSeats(Connection conn, int roomId, int rows, int cols) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO seats (room_id, seat_label, seat_type) VALUES (?, ?, ?)");
        for (int r = 0; r < rows; r++) {
            for (int c = 1; c <= cols; c++) {
                String label = String.valueOf((char)('A' + r)) + c;
                String type = (r >= rows - 2) ? "VIP" : "THUONG";
                ps.setInt(1, roomId);
                ps.setString(2, label);
                ps.setString(3, type);
                ps.addBatch();
            }
        }
        ps.executeBatch();
        ps.close();
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
