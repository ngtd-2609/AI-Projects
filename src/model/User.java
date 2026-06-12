package model;

/**
 * Lớp User kế thừa từ lớp trừu tượng Person.
 * Đại diện cho người dùng hệ thống (Admin, Staff, Customer).
 * Ví dụ về tính kế thừa (Inheritance): User IS-A Person.
 */
public class User extends Person {
    private static final long serialVersionUID = 3L;

    private int userId;
    private String username;
    private String password;
    private String role;

    public User() { super(); }

    public User(int userId, String username, String password, String fullname, String role) {
        super(userId, fullname, null);  // Gọi constructor lớp cha Person
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * Cài đặt phương thức trừu tượng từ Person.
     * User hiển thị thông tin khác Customer → Đa hình.
     */
    @Override
    public String getDisplayInfo() {
        return "Người dùng: " + getFullname() + " (" + role + ")"
             + " - Tài khoản: " + username;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() { return getFullname() + " (" + role + ")"; }
}
