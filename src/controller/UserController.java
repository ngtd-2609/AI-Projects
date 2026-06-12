package controller;

import dao.UserDAO;
import model.User;
import java.util.List;

public class UserController {
    private UserDAO userDAO = new UserDAO();

    public List<User> getAll() { return userDAO.getAll(); }
    public boolean insert(User u) { return userDAO.insert(u); }
    public boolean update(User u) { return userDAO.update(u); }
    public boolean delete(int id) { return userDAO.delete(id); }
}
