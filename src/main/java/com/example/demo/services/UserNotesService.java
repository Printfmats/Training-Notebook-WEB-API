package com.example.demo.services;

import com.example.demo.entities.UserNotes;
import com.example.demo.repositories.UserNotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserNotesService {
    private  final UserNotesRepo userNotesRepo;

    @Autowired
    public UserNotesService(UserNotesRepo userNotesRepo) {
        this.userNotesRepo = userNotesRepo;
    }

    public List<UserNotes> findAllNotesByUserAccountId(Long userId) {
        return userNotesRepo.findAllByUserAccountUserId(userId);
    }
}
