import util.DatabaseConnection;
import util.BinaryFileHelper;
import view.LoginFrame;
import model.*;
import controller.*;
import javax.swing.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Load SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("SQLite JDBC driver not found!");
            System.exit(1);
        }

        // Initialize database
        DatabaseConnection.initDatabase();

        // === DEMO CÁC TÍNH CHẤT OOP ===
        demoOOPConcepts();

        // === DEMO FILE NHỊ PHÂN ===
        demoBinaryFile();

        // Launch GUI
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }

    /**
     * Demo tính kế thừa, đa hình, upcasting, downcasting và instanceof.
     * Phục vụ cho yêu cầu đề bài OOP.
     */
    private static void demoOOPConcepts() {
        System.out.println("=== DEMO CÁC TÍNH CHẤT OOP ===\n");

        // --- 1. KẾ THỪA (Inheritance) ---
        System.out.println("--- 1. Kế thừa (Inheritance) ---");
        Customer customer = new Customer(1, "Nguyễn Văn A", "0901234567", "a@email.com");
        User admin = new User(1, "admin@123", "123456789", "Quản Trị Viên", "ADMIN");
        System.out.println("Customer (kế thừa Person): " + customer.getFullname());
        System.out.println("User (kế thừa Person): " + admin.getFullname());

        // --- 2. ĐA HÌNH (Polymorphism) - Override ---
        System.out.println("\n--- 2. Đa hình (Polymorphism) ---");
        System.out.println("Customer.getDisplayInfo(): " + customer.getDisplayInfo());
        System.out.println("User.getDisplayInfo(): " + admin.getDisplayInfo());
        // Cùng phương thức getDisplayInfo() nhưng kết quả khác nhau → Đa hình

        // Đa hình tính giá ghế
        Seat gheeThuong = new Seat(1, 1, "A1", "THUONG");
        Seat gheeVIP = new VIPSeat(2, 1, "E1");  // Upcasting: VIPSeat → Seat
        double giaGoc = 75000;
        System.out.println("Ghế thường A1 - Giá: " + gheeThuong.calculatePrice(giaGoc) + " VNĐ");
        System.out.println("Ghế VIP E1 - Giá: " + gheeVIP.calculatePrice(giaGoc) + " VNĐ");
        // Cùng gọi calculatePrice() nhưng VIPSeat trả giá × 1.5

        // --- 3. UPCASTING ---
        System.out.println("\n--- 3. Upcasting ---");
        Person person1 = customer;  // Upcasting: Customer → Person (tự động)
        Person person2 = admin;     // Upcasting: User → Person (tự động)
        System.out.println("person1 (thực chất là Customer): " + person1.getDisplayInfo());
        System.out.println("person2 (thực chất là User): " + person2.getDisplayInfo());

        // --- 4. INSTANCEOF & DOWNCASTING ---
        System.out.println("\n--- 4. instanceof & Downcasting ---");
        displayPersonInfo(person1);
        displayPersonInfo(person2);

        // instanceof với Seat
        System.out.println("\n--- instanceof với Seat ---");
        displaySeatInfo(gheeThuong);
        displaySeatInfo(gheeVIP);

        System.out.println("\n=== KẾT THÚC DEMO OOP ===\n");
    }

    /**
     * Ví dụ sử dụng instanceof và downcasting với Person.
     */
    private static void displayPersonInfo(Person person) {
        System.out.println("Kiểm tra kiểu: " + person.getClass().getSimpleName());

        if (person instanceof Customer) {
            // DOWNCASTING: Person → Customer (ép kiểu tường minh)
            Customer cust = (Customer) person;
            System.out.println("  → Đây là Customer! Email: " + cust.getEmail());
        } else if (person instanceof User) {
            // DOWNCASTING: Person → User
            User user = (User) person;
            System.out.println("  → Đây là User! Vai trò: " + user.getRole()
                + ", Username: " + user.getUsername());
        }
    }

    /**
     * Ví dụ sử dụng instanceof và downcasting với Seat.
     */
    private static void displaySeatInfo(Seat seat) {
        if (seat instanceof VIPSeat) {
            VIPSeat vip = (VIPSeat) seat;
            System.out.println("Ghế " + vip.getSeatLabel() + " → VIP (hệ số x" + vip.getVipMultiplier() + ")");
        } else {
            System.out.println("Ghế " + seat.getSeatLabel() + " → Thường");
        }
    }

    /**
     * Demo đọc/ghi file nhị phân .dat sử dụng ObjectOutputStream/ObjectInputStream.
     */
    private static void demoBinaryFile() {
        System.out.println("=== DEMO FILE NHỊ PHÂN (.dat) ===\n");

        // Lưu danh sách phim ra file .dat
        MovieController movieCtrl = new MovieController();
        ArrayList<Movie> movies = movieCtrl.getAll();
        BinaryFileHelper.saveToFile(movies, "data/backup/movies.dat");

        // Đọc lại từ file .dat
        ArrayList<Movie> loadedMovies = BinaryFileHelper.loadFromFile("data/backup/movies.dat");
        System.out.println("Danh sách phim đọc từ file .dat:");
        for (Movie m : loadedMovies) {
            System.out.println("  - " + m.getTitle() + " (" + m.getDurationMin() + " phút)");
        }

        // Lưu danh sách khách hàng
        CustomerController custCtrl = new CustomerController();
        ArrayList<Customer> customers = custCtrl.getAll();
        if (!customers.isEmpty()) {
            BinaryFileHelper.saveToFile(customers, "data/backup/customers.dat");
        }

        // Lưu danh sách booking
        BookingController bookingCtrl = new BookingController();
        ArrayList<Booking> bookings = new ArrayList<>(bookingCtrl.getAll());
        if (!bookings.isEmpty()) {
            BinaryFileHelper.saveToFile(bookings, "data/backup/bookings.dat");
        }

        System.out.println("\n=== KẾT THÚC DEMO FILE NHỊ PHÂN ===\n");
    }
}
