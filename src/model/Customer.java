package model;

/**
 * Lớp Customer kế thừa từ lớp trừu tượng Person.
 * Đại diện cho khách hàng đặt vé trong hệ thống.
 * Ví dụ về tính kế thừa (Inheritance): Customer IS-A Person.
 */
public class Customer extends Person {
    private static final long serialVersionUID = 2L;

    private int customerId;
    private String email;

    public Customer() { super(); }

    public Customer(int customerId, String fullname, String phone, String email) {
        super(customerId, fullname, phone);  // Gọi constructor lớp cha Person
        this.customerId = customerId;
        this.email = email;
    }

    /**
     * Cài đặt phương thức trừu tượng từ Person.
     * Mỗi lớp con có cách hiển thị thông tin riêng → Đa hình.
     */
    @Override
    public String getDisplayInfo() {
        return "Khách hàng: " + getFullname() + " - SĐT: " + getPhone()
             + " - Email: " + (email != null ? email : "N/A");
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public String toString() { return getFullname(); }
}
