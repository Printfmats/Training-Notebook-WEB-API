package com.example.demo.repositories;

import com.example.demo.entities.UserNotes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserNotesRepo extends JpaRepository<UserNotes,Long> {

    List<UserNotes> findAllByUserAccountUserId(Long userId);
}
