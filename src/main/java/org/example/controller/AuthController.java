package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.example.model.TelegramUser;
import org.example.model.TelegramUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
public class AuthController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            TelegramUser user = ((TelegramUserDetails) authentication.getPrincipal()).getUser();
            model.addAttribute("user", user);
            return "profile";
        }
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/auth/telegram")
    public ResponseEntity<?> authenticate(@RequestBody TelegramUser user,
                                          HttpServletResponse response) {
        try {
            String userJson = new ObjectMapper().writeValueAsString(user);
            Cookie cookie = new Cookie("telegram_auth", URLEncoder.encode(userJson, StandardCharsets.UTF_8));
            cookie.setPath("/");
            cookie.setMaxAge(86400); // 1 день
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
