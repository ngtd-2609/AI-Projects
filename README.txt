==============================================================
   HE THONG QUAN LY RAP CHIEU PHIM MINI
   Mon: Lap trinh Huong doi tuong (OOP)
   Ngon ngu: Java SE - Giao dien: Java Swing
   Co so du lieu: SQLite
==============================================================

I. YEU CAU HE THONG
------------------------------------------------------------
- Java JDK 8 tro len (da cai dat va them vao PATH)
  + Kiem tra: mo cmd, go "java -version"
  + Neu chua co, tai tai: https://www.oracle.com/java/technologies/downloads/
- He dieu hanh: Windows 7/8/10/11

II. CAU TRUC THU MUC
------------------------------------------------------------
cinema-management/
  |-- src/                    Ma nguon Java (.java)
  |   |-- Main.java           Diem khoi dong chuong trinh
  |   |-- model/               Tang Model (10 lop)
  |   |   |-- Person.java      Lop truu tuong (abstract class)
  |   |   |-- Customer.java    Ke thua Person
  |   |   |-- User.java        Ke thua Person
  |   |   |-- Movie.java       Thong tin phim
  |   |   |-- Room.java        Thong tin phong chieu
  |   |   |-- Seat.java        Ghe thuong
  |   |   |-- VIPSeat.java     Ghe VIP (ke thua Seat)
  |   |   |-- Showtime.java    Lich chieu
  |   |   |-- Booking.java     Don dat ve
  |   |   |-- Ticket.java      Ve chi tiet
  |   |-- controller/          Tang Controller (10 lop)
  |   |   |-- Manageable.java  Interface CRUD
  |   |   |-- Searchable.java  Interface tim kiem
  |   |   |-- AuthController.java
  |   |   |-- MovieController.java
  |   |   |-- RoomController.java
  |   |   |-- ShowtimeController.java
  |   |   |-- BookingController.java
  |   |   |-- CustomerController.java
  |   |   |-- StatisticsController.java
  |   |   |-- UserController.java
  |   |-- dao/                 Tang DAO - Truy cap du lieu (9 lop)
  |   |   |-- MovieDAO.java
  |   |   |-- RoomDAO.java
  |   |   |-- SeatDAO.java
  |   |   |-- ShowtimeDAO.java
  |   |   |-- BookingDAO.java
  |   |   |-- TicketDAO.java
  |   |   |-- CustomerDAO.java
  |   |   |-- UserDAO.java
  |   |   |-- StatisticsDAO.java
  |   |-- view/                Tang View - Giao dien (9 lop + 1 component)
  |   |   |-- LoginFrame.java
  |   |   |-- MainFrame.java
  |   |   |-- MoviePanel.java
  |   |   |-- RoomPanel.java
  |   |   |-- ShowtimePanel.java
  |   |   |-- BookingPanel.java
  |   |   |-- CustomerPanel.java
  |   |   |-- StatisticsPanel.java
  |   |   |-- UserPanel.java
  |   |   |-- components/
  |   |       |-- SeatMapPanel.java
  |   |-- util/                Tang Utility
  |       |-- DatabaseConnection.java
  |       |-- Validator.java
  |       |-- BinaryFileHelper.java
  |-- data/                    Du lieu
  |   |-- cinema.db            Co so du lieu SQLite
  |   |-- backup/              File nhi phan .dat
  |       |-- movies.dat
  |       |-- customers.dat
  |-- lib/                     Thu vien ben ngoai
  |   |-- sqlite-jdbc-3.45.3.0.jar
  |   |-- slf4j-api-2.0.9.jar
  |   |-- slf4j-nop-2.0.9.jar
  |-- out/                     Thu muc bien dich (.class)
  |-- resources/               Tai nguyen (poster phim)
  |-- CinemaManagement.jar     File JAR chay nhanh
  |-- run.bat                  Script bien dich va chay
  |-- run_jar.bat              Script chay tu file JAR
  |-- MANIFEST.MF              Manifest cho file JAR
  |-- README.txt               Huong dan su dung (file nay)
  |-- bao_cao_quan_ly_rap_chieu_phim.md   Bao cao (Markdown)

III. CACH CHAY CHUONG TRINH
------------------------------------------------------------

  Cach 1: Chay tu file JAR (NHANH NHAT)
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  1. Giai nen file .zip
  2. Mo thu muc cinema-management
  3. Nhan doi (double-click) vao file "run_jar.bat"
  
  Hoac mo Command Prompt (cmd) tai thu muc goc:
     > java -jar CinemaManagement.jar

  Cach 2: Bien dich va chay tu ma nguon
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  1. Giai nen file .zip
  2. Mo Command Prompt (cmd) tai thu muc goc
  3. Chay file run.bat:
     > run.bat

  Hoac chay bang lenh thu cong:
     > javac -encoding UTF-8 -cp "lib/*" -d out src/model/*.java src/dao/*.java src/controller/*.java src/view/*.java src/view/components/*.java src/util/*.java src/Main.java
     > java -cp "out;lib/*" Main

  Cach 3: Mo bang IDE
  ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  1. Mo IntelliJ IDEA hoac Eclipse
  2. Import project (Open Folder)
  3. Them cac file JAR trong thu muc lib/ vao classpath
  4. Chay file Main.java

IV. TAI KHOAN MAU
------------------------------------------------------------
  +-------------+-----------+----------+--------------------+
  | Tai khoan   | Mat khau  | Vai tro  | Quyen truy cap     |
  +-------------+-----------+----------+--------------------+
  | admin@123   | 123456789 | ADMIN    | Toan bo chuc nang  |
  | nhanvien    | nv123     | STAFF    | Lich chieu, Dat ve,|
  |             |           |          | Khach hang         |
  | khachhang   | kh123     | CUSTOMER | Xem lich, Dat ve   |
  +-------------+-----------+----------+--------------------+

V. CHUC NANG CHINH
------------------------------------------------------------
  1. Quan ly phim     : Them, Sua, Xoa, Tim kiem, Sap xep
  2. Quan ly phong    : Them, Sua, Xoa (tu dong tao so do ghe)
  3. Quan ly lich chieu: Them, Sua, Xoa (kiem tra trung lich)
  4. Dat ve           : Chon suat chieu, chon ghe, tinh tien
  5. Quan ly khach hang: Them, Sua, Xoa, Tim kiem
  6. Thong ke doanh thu: Theo ngay, theo phim
  7. Quan ly tai khoan : Them, Sua, Xoa (chi ADMIN)

VI. CAC TINH CHAT OOP DUOC AP DUNG
------------------------------------------------------------
  1.  Dong goi (Encapsulation)   : private fields + getter/setter
  2.  Lop truu tuong (Abstract)  : Person.java (abstract class)
  3.  Interface                  : Manageable<T>, Searchable<T>
  4.  Ke thua (Inheritance)      : Customer, User extends Person
                                   VIPSeat extends Seat
  5.  Da hinh (Polymorphism)     : getDisplayInfo(), calculatePrice()
  6.  Upcasting                  : Person p = new Customer()
  7.  Downcasting                : Customer c = (Customer) person
  8.  instanceof                 : if (person instanceof Customer)
  9.  ArrayList<T>               : Luu tru danh sach doi tuong
  10. Comparator                 : Sap xep da tieu chi
  11. Serializable               : Ho tro file nhi phan .dat
  12. File nhi phan              : ObjectOutputStream/ObjectInputStream
  13. Exception Handling         : try-catch xu ly ngoai le
  14. Transaction                : commit/rollback dat ve

VII. FILE DU LIEU NHI PHAN (.dat)
------------------------------------------------------------
  - data/backup/movies.dat     : Danh sach phim (backup)
  - data/backup/customers.dat  : Danh sach khach hang (backup)
  
  Cac file .dat duoc tao tu dong khi khoi dong chuong trinh
  bang ObjectOutputStream/ObjectInputStream.

VIII. LUU Y
------------------------------------------------------------
  - Khong xoa file cinema.db (co so du lieu chinh)
  - Khong xoa thu muc lib/ (chua thu vien JDBC)
  - Neu gap loi "SQLite JDBC driver not found", kiem tra
    thu muc lib/ co du 3 file .jar
  - Neu muon reset du lieu, xoa file data/cinema.db roi
    chay lai chuong trinh (se tu dong tao du lieu mau)

==============================================================
  (C) 2026 - Bai tap lon mon Lap trinh Huong doi tuong
==============================================================
