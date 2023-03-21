package com.example.demo.user_account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserAccountDAO userAccountDAO;
    @Autowired
    public UserService(UserAccountDAO userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }




}
