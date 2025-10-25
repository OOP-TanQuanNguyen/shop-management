package edu.ptithcm.services;

import edu.ptithcm.model.User;
import edu.ptithcm.utils.CryptoUtil;

public class LoginService {
    public static boolean authenticate(String username, String password) {
        User user = User.findByUsername(username);
        if (user == null) return false;
        return CryptoUtil.verifyPassword(password, user.getPasswordHash());
    }
}
