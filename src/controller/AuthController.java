package controller;

import dao.UserDAO;
import model.User;

public class AuthController {
    private UserDAO userDAO = new UserDAO();
    private static User currentUser;

    public User login(String username, String password) {
        User user = userDAO.authenticate(username, password);
        if (user != null) currentUser = user;
        return user;
    }

    public void logout() { currentUser = null; }
    public static User getCurrentUser() { return currentUser; }
}
