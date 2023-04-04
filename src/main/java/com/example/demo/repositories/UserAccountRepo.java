package com.example.demo.repositories;

import com.example.demo.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepo extends JpaRepository<UserAccount,Long>{
    Optional<UserAccount> findByUserName(String username);
    Optional<UserAccount> findByUserEmail(String email);
}
