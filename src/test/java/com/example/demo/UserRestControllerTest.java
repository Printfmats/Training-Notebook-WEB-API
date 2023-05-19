package com.example.demo;

import com.example.demo.controllers.UserRestController;
import com.example.demo.entities.UserAccount;
import com.example.demo.repositories.UserAccountRepo;
import com.example.demo.repositories.UserNotesRepo;
import com.example.demo.services.UserAccountService;
import com.example.demo.services.TokenService;
import com.example.demo.services.UserNotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserRestControllerTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserAccountRepo userAccountRepo;

    @Mock
    private UserAccountService userAccountService;

    @Mock
    private TokenService tokenService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    @Mock
    private UserNotesRepo userNotesRepo;

    @Mock
    private UserNotesService userNotesService;

    private UserRestController userRestController;

    @Autowired
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userRestController = new UserRestController(
                passwordEncoder,
                userAccountRepo,
                userNotesRepo,
                userAccountService,
                userNotesService,
                tokenService
        );
    }

    @Test
    void testRegisterUser_SuccessfulRegistration() {
        UserAccount userAccount = new UserAccount("John Doe", "password", "john@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userAccountService.isEmailAlreadyRegistered(userAccount.getUserEmail())).thenReturn(false);
        when(passwordEncoder.encode(userAccount.getUserPassword())).thenReturn("hashedPassword");

        String result = userRestController.registerUser(userAccount, bindingResult, model, redirectAttributes);

        verify(passwordEncoder).encode(userAccount.getUserPassword());
        verify(userAccountRepo).save(userAccount);
        verify(redirectAttributes).addAttribute(eq("successMessage"), anyString());
        assertEquals("redirect:/login", result);
    }


    @Test
    void testRegisterUser_ValidationErrors() {
        UserAccount userAccount = new UserAccount("John Doe", "password", "john@example.com");
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = userRestController.registerUser(userAccount, bindingResult, model, redirectAttributes);

        verifyNoInteractions(passwordEncoder, userAccountRepo, redirectAttributes);
        assertEquals("registerpage", result);
    }

    @Test
    void testRegisterUser_EmailAlreadyRegistered() {
        UserAccount userAccount = new UserAccount("John Doe", "password", "john@example.com");
        when(bindingResult.hasErrors()).thenReturn(false);
        when(userAccountService.isEmailAlreadyRegistered(userAccount.getUserEmail())).thenReturn(true);

        String result = userRestController.registerUser(userAccount, bindingResult, model, redirectAttributes);

        verifyNoInteractions(passwordEncoder, userAccountRepo);
        verify(bindingResult).rejectValue(eq("userEmail"), anyString(), anyString());
        assertEquals("registerpage", result);
    }


}

