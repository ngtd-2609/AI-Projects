<div align="center">

# 🎬 Cinema Management System

**Ứng dụng desktop quản lý rạp chiếu phim — Java Swing & SQLite**

[![Java](https://img.shields.io/badge/Java-SE%208+-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/SQLite-3.45-003B57?style=flat-square&logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat-square)](LICENSE)

*Phát triển bởi **Nguyen Tung Duong***

</div>

---

## Tổng Quan

**Cinema Management System** là ứng dụng desktop hoạt động offline, phục vụ các nghiệp vụ cốt lõi của rạp chiếu phim: quản lý phim, lịch chiếu, đặt vé, khách hàng và thống kê doanh thu.

Xây dựng hoàn toàn bằng Java theo kiến trúc **MVC + DAO**, chạy trên mọi máy có JDK 8+ — không cần server, không cần internet.

---

## Tính Năng

| Module | Mô tả |
|--------|-------|
| **Quản lý phim** | Thêm, sửa, xoá, tìm kiếm, sắp xếp danh mục phim |
| **Phòng chiếu & Sơ đồ ghế** | Quản lý phòng chiếu + tự động tạo sơ đồ ghế (Thường & VIP) |
| **Lịch chiếu** | Lên lịch suất chiếu với kiểm tra trùng lịch tự động |
| **Đặt vé** | Chọn ghế trực quan → tính giá tự động → đặt vé theo giao dịch |
| **Khách hàng** | Quản lý thông tin khách hàng với bộ lọc & tìm kiếm |
| **Thống kê doanh thu** | Báo cáo doanh thu theo ngày, theo phim |
| **Tài khoản** | Phân quyền truy cập: Admin / Staff / Customer |

---

## Kiến Trúc

```
┌─────────────────────────────────────────────┐
│              VIEW  (Java Swing)             │
│  LoginFrame · MainFrame · Feature Panels    │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│               CONTROLLER                    │
│  implements Manageable<T>, Searchable<T>    │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│             DAO  (Data Access)              │
└────────────────────┬────────────────────────┘
                     │
┌────────────────────▼────────────────────────┐
│          SQLite  (data/cinema.db)           │
└─────────────────────────────────────────────┘
```

---

## Cấu Trúc Thư Mục

```
cinema-management/
├── src/
│   ├── Main.java                # Điểm khởi động ứng dụng
│   ├── model/                   # Lớp dữ liệu (10 classes)
│   │   ├── Person.java          # Abstract base class
│   │   ├── Customer.java        # extends Person
│   │   ├── User.java            # extends Person
│   │   ├── Movie.java
│   │   ├── Room.java
│   │   ├── Seat.java
│   │   ├── VIPSeat.java         # extends Seat
│   │   ├── Showtime.java
│   │   ├── Booking.java
│   │   └── Ticket.java
│   ├── controller/              # Xử lý logic nghiệp vụ (10 classes)
│   │   ├── Manageable.java      # Interface CRUD generic
│   │   ├── Searchable.java      # Interface tìm kiếm generic
│   │   └── *Controller.java
│   ├── dao/                     # Truy vấn cơ sở dữ liệu (9 classes)
│   ├── view/                    # Giao diện Swing (10 classes)
│   │   ├── components/
│   │   │   └── SeatMapPanel.java
│   │   └── *.java
│   └── util/                    # Tiện ích
│       ├── DatabaseConnection.java
│       ├── Validator.java
│       └── BinaryFileHelper.java
├── data/
│   ├── cinema.db                # Cơ sở dữ liệu SQLite
│   └── backup/                  # File nhị phân (.dat)
├── lib/                         # Thư viện (đi kèm sẵn)
│   ├── sqlite-jdbc-3.45.3.0.jar
│   ├── slf4j-api-2.0.9.jar
│   └── slf4j-nop-2.0.9.jar
├── resources/posters/           # Poster phim
├── run.bat                      # Script biên dịch & chạy
├── run_jar.bat                  # Script chạy từ JAR
└── README.md
```

---

## Bắt Đầu

### Yêu cầu

- **Java JDK 8+** — kiểm tra bằng `java -version`
- **Hệ điều hành:** Windows 7/8/10/11

> Tất cả thư viện đã có sẵn trong `lib/` — không cần Maven, Gradle hay bất kỳ package manager nào.

### Cài đặt & Chạy

```bash
# Clone repository
git clone https://github.com/<your-username>/cinema-management.git
cd cinema-management

# Cách 1: Chạy từ file JAR
java -jar CinemaManagement.jar

# Cách 2: Biên dịch từ mã nguồn
javac -encoding UTF-8 -cp "lib/*" -d out \
  src/model/*.java src/dao/*.java src/controller/*.java \
  src/view/*.java src/view/components/*.java \
  src/util/*.java src/Main.java
java -cp "out;lib/*" Main
```

Trên Windows, có thể nhấn đúp `run.bat` hoặc `run_jar.bat`.

### Mở bằng IDE

1. Mở thư mục project trong **IntelliJ IDEA** hoặc **Eclipse**
2. Thêm các file `.jar` trong `lib/` vào classpath
3. Chạy `Main.java`

---

## Tài Khoản Demo

| Username | Password | Vai trò | Quyền truy cập |
|----------|----------|---------|----------------|
| `admin@123` | `123456789` | Admin | Toàn bộ hệ thống |
| `nhanvien` | `nv123` | Staff | Lịch chiếu, Đặt vé, Khách hàng |
| `khachhang` | `kh123` | Customer | Xem lịch, Đặt vé |

---

## Điểm Nổi Bật Kỹ Thuật

- **Abstract class & Interface** — `Person` (abstract), `Manageable<T>`, `Searchable<T>`
- **Kế thừa đa tầng** — `Customer`/`User` → `Person`, `VIPSeat` → `Seat`
- **Đa hình** — `getDisplayInfo()`, `calculatePrice()` hoạt động khác nhau tuỳ lớp con
- **Generics** — Collection type-safe với `ArrayList<T>`
- **Serialization** — `ObjectOutputStream`/`ObjectInputStream` cho backup `.dat`
- **Transaction** — `commit`/`rollback` đảm bảo toàn vẹn dữ liệu khi đặt vé
- **Validation** — Kiểm tra dữ liệu đầu vào tập trung qua `Validator`

---

## Công Nghệ

| Tầng | Công nghệ |
|------|-----------|
| Ngôn ngữ | Java SE 8+ |
| Giao diện | Java Swing |
| Cơ sở dữ liệu | SQLite 3.45 |
| JDBC Driver | sqlite-jdbc 3.45.3.0 |
| Logging | SLF4J 2.0.9 |

---

## Đóng Góp

Mọi đóng góp đều được hoan nghênh. Hãy mở issue hoặc gửi pull request.

1. Fork repository
2. Tạo nhánh tính năng (`git checkout -b feature/tinh-nang-moi`)
3. Commit thay đổi (`git commit -m 'Thêm tính năng mới'`)
4. Push lên nhánh (`git push origin feature/tinh-nang-moi`)
5. Mở Pull Request

---

## Giấy Phép

```
MIT (c) 2026 Nguyen Tung Duong
```

---