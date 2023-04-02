package com.example.demo.user_account;


import com.example.demo.security_log.SecurityUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;


@Controller
public class UserRestController {


    private PasswordEncoder passwordEncoder;
    private UserAccountDAO userAccountDAO;
    private UserService userService;

    @Autowired
    public UserRestController(PasswordEncoder passwordEncoder, UserAccountDAO userAccountDAO, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userAccountDAO = userAccountDAO;
        this.userService = userService;

    }


    @RequestMapping("/") public String startingPage() {
        return "startpage";
    }
    @RequestMapping("/login")
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
        return "redirect:/register";
    }

    @RequestMapping("/api/dodaj-notatki")
    public String apiAddingNotesPage() {
        return "nowenotatkipage";
    }

    @RequestMapping("/api/treningi")
    public String apiTreiningiPage() {
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
