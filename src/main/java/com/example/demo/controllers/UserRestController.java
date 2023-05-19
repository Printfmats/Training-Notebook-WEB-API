package com.example.demo.controllers;


import com.example.demo.entities.UserAccount;
import com.example.demo.entities.UserNotes;
import com.example.demo.repositories.UserAccountRepo;
import com.example.demo.repositories.UserNotesRepo;
import com.example.demo.services.TokenService;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.UserNotesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
import java.util.*;


@Controller
public class UserRestController {

    private final PasswordEncoder passwordEncoder;
    private final UserAccountRepo userAccountDAO;
    private final UserNotesRepo userNotesRepo;
    private final UserAccountService userService;
    private final UserNotesService userNotesService;
    private final TokenService tokenService;

    @Autowired
    public UserRestController(PasswordEncoder passwordEncoder, UserAccountRepo userAccountDAO, UserNotesRepo userNotesRepo, UserAccountService userService, UserNotesService userNotesService, TokenService tokenService) {
        this.passwordEncoder = passwordEncoder;
        this.userAccountDAO = userAccountDAO;
        this.userNotesRepo = userNotesRepo;
        this.userService = userService;
        this.userNotesService = userNotesService;
        this.tokenService = tokenService;
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

    @RequestMapping("/password/{email}")
    public String apiSettingPasswordAfterOauth(@PathVariable("email") String email, Model model) {
        model.addAttribute("email", email);
        return "passwordoauthpage";
    }

    @PostMapping("/password")
    public String savePassword(@RequestParam("email") String email, @RequestParam("password") String password) {
        Optional<UserAccount> userAccountOptional = userAccountDAO.findByUserEmail(email);
        if (userAccountOptional.isPresent()) {
            UserAccount userAccount = userAccountOptional.get();
            userAccount.setUserPassword(passwordEncoder.encode(password));
            userAccountDAO.save(userAccount);
            // Przekieruj użytkownika na stronę po zapisie hasła
            return "redirect:/login";
        }
        return "redirect:/";
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
    public String apiTreningiPage(@RequestParam(value = "sort", required = false) String sortField, Model model, Principal principal) {
        String username = principal.getName();
        UserAccount user = userAccountDAO.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<UserNotes> userNotes = userNotesService.findAllNotesByUserAccountId(user.getUserId());
        if (sortField != null) {
            switch (sortField) {
                case "title":
                    Collections.sort(userNotes, Comparator.comparing(UserNotes::getTitle));
                    break;
                case "activityType":
                    Collections.sort(userNotes, Comparator.comparing(UserNotes::getActivityType));
                    break;
            }
        }
        model.addAttribute("userNotes", userNotes);
        return "treningipage";
    }

    @RequestMapping("/api/treningi/delete/{idNote}")
    public String deleteNote(@PathVariable("idNote") Long idNote) {
        System.out.println(idNote);
        userNotesRepo.deleteById(idNote);
        return "redirect:/api/treningi";
    }

    @RequestMapping("/api/profil")
    public String apiProfilPage(Model model, @AuthenticationPrincipal UserDetails userDetails, Authentication authentication) {
        UserAccount userAccount = userAccountDAO.findByUserName(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        int userNotes = userNotesService.countAllByUserAccountUserId(userAccount.getUserId());
        model.addAttribute("email", userAccount.getUserEmail());
        model.addAttribute("how_many_notes",userNotes);
        model.addAttribute("role", userDetails.getAuthorities());
        System.out.println(tokenService.generateToken(authentication));
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
    public String activateUser(@RequestParam("userId") Long userId, @RequestParam("role") String role) {
        // Pobierz użytkownika na podstawie userId
        Optional<UserAccount> optionalUserAccount = userAccountDAO.findById(userId);
        if (optionalUserAccount.isPresent()) {
            UserAccount userAccount = optionalUserAccount.get();
            userAccount.setRole(role); // Ustaw rolę na podstawie wartości pola "role"
            userAccountDAO.save(userAccount); // Zapisz zmiany w bazie danych
        }
        // Przekieruj na stronę /api/activation
        return "redirect:/api/activation";
    }




//	@Bean  //ODBLOKOWAĆ PRZY UŻYCIU DOCKER COMPOSE //API ZAKŁADA ŻE NA POCZĄTKU JUŻ JEST JAKIŚ ADMIN KTÓRY AKTYWUJE KONTA
//    CommandLineRunner commandLineRunner (UserAccountRepo repositoryUser){
//		return args -> {
//			repositoryUser.save(new UserAccount("ggg",passwordEncoder.encode("ggg"),"ggg@wp.pl","ADMIN"));
//		};
//	}
}
