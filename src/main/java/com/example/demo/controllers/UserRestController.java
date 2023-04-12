package com.example.demo.controllers;


import com.example.demo.entities.UserAccount;
import com.example.demo.entities.UserNotes;
import com.example.demo.repositories.UserAccountRepo;
import com.example.demo.repositories.UserNotesRepo;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.UserNotesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class UserRestController {

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepo userAccountDAO;
    private final UserNotesRepo userNotesRepo;
    private final UserAccountService userService;
    private final UserNotesService userNotesService;

    @Autowired
    public UserRestController(PasswordEncoder passwordEncoder, UserAccountRepo userAccountDAO, UserNotesRepo userNotesRepo, UserAccountService userService, UserNotesService userNotesService) {
        this.passwordEncoder = passwordEncoder;
        this.userAccountDAO = userAccountDAO;
        this.userNotesRepo = userNotesRepo;
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

    @PostMapping("/api/dodaj-notatki")
    public String addNote(@RequestParam("title") String title,
                          @RequestParam("duration") String duration,
                          @RequestParam("start-date") String startDateString,
                          @RequestParam("end-date") String endDateString,
                          @RequestParam("description") String description,
                          Principal principal) {

        // konwertuj startDateString i endDateString na obiekt java.sql.Timestamp
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date parsedStartDate = null;
        Date parsedEndDate = null;
        try {
            parsedStartDate = dateFormat.parse(startDateString);
            parsedEndDate = dateFormat.parse(endDateString);
        } catch (ParseException e) {
            // obsłuż wyjątek
        }
        Timestamp startDate = new Timestamp(parsedStartDate.getTime());
        Timestamp endDate = new Timestamp(parsedEndDate.getTime());

        String username = principal.getName(); // pobierz nazwę użytkownika
        Optional<UserAccount> userAccount = userAccountDAO.findByUserName(username);
        if (!userAccount.isPresent()) {
            // obsłuż wyjątek lub zwróć informację o błędzie
        }

        UserNotes note = new UserNotes(title, duration, startDate, endDate, description, userAccount.get());

        try {
            userNotesRepo.save(note);
        } catch (Exception e) {
            // obsłuż wyjątek
        }
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
        UserAccount userAccount = userAccountDAO.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        int userNotes = userNotesService.countAllByUserAccountUserId(userAccount.getUserId());
        model.addAttribute("email", userAccount.getUserEmail());
        model.addAttribute("how_many_notes",userNotes);
        model.addAttribute("role", userDetails.getAuthorities());
        return "profilpage";
    }



    @GetMapping("/api/activation")
    public String apiActivateAccountAsAdmin(Model model, Principal principal) {
        // Sprawdź rolę użytkownika
        Authentication auth = (Authentication) principal;
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ADMIN"));
        if (!isAdmin) {
            // Jeśli nie jest adminem, zwróć odpowiednią odpowiedź HTTP lub przekieruj na inną stronę
            return "redirect:/error403"; // Przykład przekierowania na stronę błędu 403
        }

        List<UserAccount> userAccount = userAccountDAO.findByRoleIsNull();
        model.addAttribute("userAccount", userAccount);
        return  "activationuserpage";
    }
    @PostMapping("/api/activation")
    public String activateUser(@RequestParam("userId") Long userId) {
        // Pobierz użytkownika na podstawie userId
        Optional<UserAccount> optionalUserAccount = userAccountDAO.findById(userId);
        if (optionalUserAccount.isPresent()) {
            UserAccount userAccount = optionalUserAccount.get();
            // Ustaw rolę użytkownika na "USER" za pomocą settera
            System.out.println(userAccount);
            userAccount.setRole("USER");
            userAccount.setUserEmail(userAccount.getUserEmail());

            userAccountDAO.save(userAccount);
            // Jeśli użytkownik miał wcześniej null rolę, to możesz również ustawić na null:
            // userAccount.setRole(null);

        }
        // Przekieruj na stronę /api/activation
        return "redirect:/api/activation";
    }




}
