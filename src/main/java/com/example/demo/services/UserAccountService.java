package com.example.demo.services;

import com.example.demo.entities.UserAccount;
import com.example.demo.repositories.UserAccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepo userAccountDAO;
    @Autowired
    public UserAccountService(UserAccountRepo userAccountDAO) {
        this.userAccountDAO = userAccountDAO;
    }

    public boolean isEmailAlreadyRegistered(String email) {
        Optional<UserAccount> userAccount = userAccountDAO.findByUserEmail(email);
        return userAccount.isPresent();
    }

    public List<UserAccount> doesUserNeedActivation() {
        List<UserAccount> userAccount = userAccountDAO.findByRoleIsNull();
        return userAccount;
    }



}
