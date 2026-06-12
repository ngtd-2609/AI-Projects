package controller;

import java.util.ArrayList;

/**
 * Interface Searchable<T> - định nghĩa chức năng tìm kiếm.
 * Các Controller có hỗ trợ tìm kiếm phải implement interface này.
 */
public interface Searchable<T> {
    ArrayList<T> search(String keyword);
}
