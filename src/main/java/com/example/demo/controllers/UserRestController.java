package com.example.demo.controllers;


import com.example.demo.entities.UserAccount;
import com.example.demo.entities.UserNotes;
import com.example.demo.repositories.UserAccountRepo;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.UserNotesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;


@Controller
public class UserRestController {

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepo userAccountDAO;

    private final UserAccountService userService;
    private final UserNotesService userNotesService;

    @Autowired
    public UserRestController(PasswordEncoder passwordEncoder, UserAccountRepo userAccountDAO, UserAccountService userService, UserNotesService userNotesService) {
        this.passwordEncoder = passwordEncoder;
        this.userAccountDAO = userAccountDAO;
        this.userService = userService;
        this.userNotesService = userNotesService;
    }

    @RequestMapping("/") public String startingPage() {
        return "startpage";
    }
    @GetMapping("/login")
    public String logingPage(){
        return "loginpage";
    }
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userAccount", new UserAccount());
        return "registerpage";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userAccount") @Valid UserAccount userAccount, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "registerpage";
        }
        if (userService.isEmailAlreadyRegistered(userAccount.getUserEmail())) {
            bindingResult.rejectValue("userEmail", "error.userAccount", "This email is already registered");
            return "registerpage";
        }
        userAccount.setUserPassword(passwordEncoder.encode(userAccount.getUserPassword()));
        userAccountDAO.save(userAccount);
        redirectAttributes.addAttribute("successMessage", "Registration successful. You can now log in.");
        return "redirect:/login";
    }

    @RequestMapping("/api/dodaj-notatki")
    public String apiAddingNotesPage() {
        return "nowenotatkipage";
    }

    @RequestMapping("/api/treningi")
    public String apiTreiningiPage(Model model, Principal principal) {
        String username = principal.getName();
        UserAccount user = userAccountDAO.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        List<UserNotes> userNotes = userNotesService.findAllNotesByUserAccountId(user.getUserId());
        model.addAttribute("userNotes", userNotes);
        return "treningipage";
    }

    @RequestMapping("/api/profil")
    public String apiProfilPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserAccount user = userAccountDAO.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        model.addAttribute("email", user.getUserEmail());
        return "profilpage";
    }
}
