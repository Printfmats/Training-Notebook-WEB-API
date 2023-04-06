package com.example.demo.repositories;

import com.example.demo.entities.UserNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotesRepo extends JpaRepository<UserNotes,Long> {
    List<UserNotes> findAllByUserAccountUserIdOrderByEndDateDesc(Long userId);

    @Query("SELECT COUNT(n) FROM UserNotes n WHERE n.userAccount.userId = :userId")
    int countByUserAccountUserId(Long userId);
}
