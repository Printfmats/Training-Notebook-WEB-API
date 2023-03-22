package com.example.demo.user_account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserAccountDAO userAccountDAO;
    @Autowired
    public UserService(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    public boolean isEmailAlreadyRegistered(String email) {
        Optional<UserAccount> userAccount = userAccountDAO.findByUserEmail(email);
        return userAccount.isPresent();
    }



}
