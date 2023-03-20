package com.example.demo.user_account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountDAO extends JpaRepository<UserAccount,Long>{
    Optional<UserAccount> findByUserName(String username);

}
