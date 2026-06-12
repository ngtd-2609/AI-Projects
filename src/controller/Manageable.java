package controller;

import java.util.ArrayList;

/**
 * Interface Manageable<T> - định nghĩa các thao tác CRUD chuẩn.
 * Các Controller phải implement interface này để đảm bảo tính nhất quán.
 */
public interface Manageable<T> {
    boolean add(T item);
    boolean update(T item);
    boolean delete(int id);
    ArrayList<T> getAll();
}
