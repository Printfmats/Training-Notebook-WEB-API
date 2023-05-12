package com.example.demo.security_log;

import java.io.IOException;
import java.util.Optional;

import com.example.demo.services.UserAccountService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.entities.UserAccount;
import com.example.demo.repositories.UserAccountRepo;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {


    private UserAccountRepo userAccountRepo;

    @Autowired
    public CustomSuccessHandler(UserAccountRepo userAccountRepo) {
        this.userAccountRepo = userAccountRepo;

    }



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        // Pobranie informacji o użytkowniku z tokena OAuth2
        OAuth2User oAuth2User = oauthToken.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Sprawdzenie, czy użytkownik jest już w bazie
        Optional<UserAccount> existingUserOptional = userAccountRepo.findByUserEmail(email);

        if (!existingUserOptional.isPresent()) {
            UserAccount newUserAccount = new UserAccount();
            newUserAccount.setUserName(name);
            newUserAccount.setUserEmail(email);
            newUserAccount.setUserPassword("BRAK");
            // Dodanie użytkownika do bazy
            userAccountRepo.save(newUserAccount);
        }

        // Przekierowanie użytkownika na stronę /password z przekazanym emailem jako zmienną ścieżki
        String redirectUrl = "/password/" + email;
        DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

}
