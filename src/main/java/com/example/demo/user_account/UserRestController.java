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

import java.util.Optional;

@Controller
public class UserRestController {


    private PasswordEncoder passwordEncoder;
    private UserAccountDAO userAccountDAO;

    @Autowired
    public UserRestController(PasswordEncoder passwordEncoder, UserAccountDAO userAccountDAO) {
        this.passwordEncoder = passwordEncoder;
        this.userAccountDAO = userAccountDAO;
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
        userAccount.setUserPassword(passwordEncoder.encode(userAccount.getUserPassword()));
        userAccountDAO.save(userAccount);
        return "redirect:/login";
    }
//    @PostMapping("/register")
//    public String registerUser(@ModelAttribute("userAccount") @Valid UserAccount userAccount, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            return "registerpage";
//        }
//        userAccountDAO.save(userAccount);
//        return "redirect:/login";
//    }
    @RequestMapping("/api")
    public String apiPage() {
            return "apipage";
        }
}
