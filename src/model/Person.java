package model;

import java.io.Serializable;

/**
 * Lớp trừu tượng Person - đại diện cho một "người" trong hệ thống.
 * Là lớp cha của Customer và User.
 * Chứa phương thức trừu tượng getDisplayInfo() buộc lớp con phải cài đặt.
 */
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 1L;

    protected int id;
    private String fullname;
    private String phone;

    public Person() {}

    public Person(int id, String fullname, String phone) {
        this.id = id;
        this.fullname = fullname;
        this.phone = phone;
    }

    /**
     * Phương thức trừu tượng - mỗi lớp con phải tự cài đặt cách hiển thị thông tin.
     * Đây là ví dụ về tính trừu tượng (Abstraction) trong OOP.
     */
    public abstract String getDisplayInfo();

    // Getter / Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() { return fullname; }
}
