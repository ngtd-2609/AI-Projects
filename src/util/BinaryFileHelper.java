package util;

import java.io.*;
import java.util.ArrayList;

/**
 * Lớp tiện ích đọc/ghi file nhị phân (.dat).
 * Sử dụng ObjectOutputStream và ObjectInputStream để serialize/deserialize
 * danh sách đối tượng Java.
 */
public class BinaryFileHelper {

    /**
     * Ghi danh sách đối tượng ra file nhị phân .dat
     * @param list Danh sách đối tượng cần lưu
     * @param filePath Đường dẫn file .dat
     */
    public static <T extends Serializable> void saveToFile(ArrayList<T> list, String filePath) {
        // Tạo thư mục cha nếu chưa tồn tại
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filePath))) {
            oos.writeObject(list);
            System.out.println("[BinaryFileHelper] Đã lưu " + list.size()
                + " đối tượng vào " + filePath);
        } catch (IOException e) {
            System.err.println("[BinaryFileHelper] Lỗi ghi file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Đọc danh sách đối tượng từ file nhị phân .dat
     * @param filePath Đường dẫn file .dat
     * @return Danh sách đối tượng đọc được, hoặc ArrayList rỗng nếu lỗi
     */
    @SuppressWarnings("unchecked")
    public static <T extends Serializable> ArrayList<T> loadFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("[BinaryFileHelper] File không tồn tại: " + filePath);
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(filePath))) {
            ArrayList<T> list = (ArrayList<T>) ois.readObject();
            System.out.println("[BinaryFileHelper] Đã đọc " + list.size()
                + " đối tượng từ " + filePath);
            return list;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[BinaryFileHelper] Lỗi đọc file: " + e.getMessage());
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
