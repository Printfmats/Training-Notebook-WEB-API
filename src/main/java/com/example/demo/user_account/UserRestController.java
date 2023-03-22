package com.example.demo.user_account;


import com.example.demo.security_log.SecurityUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



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
    public String registerUser(@ModelAttribute("userAccount") @Valid UserAccount userAccount, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "registerpage";
        }
        if (userService.isEmailAlreadyRegistered(userAccount.getUserEmail())) {
            return "registerpage";
        }
        userAccount.setUserPassword(passwordEncoder.encode(userAccount.getUserPassword()));
        userAccountDAO.save(userAccount);
        return "redirect:/login";
    }
    @RequestMapping("/api")
    public String apiPage() {
            return "apipage";
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
    public String apiProfilPage() {
        return "profilpage";
    }
}
