package controller;

import dao.CustomerDAO;
import model.Customer;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * CustomerController implements Manageable và Searchable interface.
 */
public class CustomerController implements Manageable<Customer>, Searchable<Customer> {
    private CustomerDAO customerDAO = new CustomerDAO();

    @Override
    public ArrayList<Customer> getAll() { return new ArrayList<>(customerDAO.getAll()); }

    @Override
    public ArrayList<Customer> search(String keyword) { return new ArrayList<>(customerDAO.search(keyword)); }

    @Override
    public boolean add(Customer c) { return customerDAO.insert(c) > 0; }

    public int insert(Customer c) { return customerDAO.insert(c); }

    @Override
    public boolean update(Customer c) { return customerDAO.update(c); }

    @Override
    public boolean delete(int id) { return customerDAO.delete(id); }

    /** Sắp xếp khách hàng theo tên A-Z */
    public ArrayList<Customer> getAllSortedByName() {
        ArrayList<Customer> list = getAll();
        list.sort(Comparator.comparing(Customer::getFullname));
        return list;
    }
}
