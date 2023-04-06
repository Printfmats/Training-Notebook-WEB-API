package com.example.demo.services;

import com.example.demo.entities.UserNotes;
import com.example.demo.repositories.UserNotesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotesService {
    private  final UserNotesRepo userNotesRepo;

    @Autowired
    public UserNotesService(UserNotesRepo userNotesRepo) {
        this.userNotesRepo = userNotesRepo;
    }

    public List<UserNotes> findAllNotesByUserAccountId(Long userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "endDate");
        return userNotesRepo.findAllByUserAccountUserIdOrderByEndDateDesc(userId);
    }

    public int countAllByUserAccountUserId(Long userId) {
        return userNotesRepo.countByUserAccountUserId(userId);
    }
}
