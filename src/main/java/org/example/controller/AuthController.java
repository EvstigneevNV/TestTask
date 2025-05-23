package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.model.TelegramUser;
import org.example.model.TelegramUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class AuthController {

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        log.info("Authenticated user: {}", String.valueOf(authentication));
        if (authentication != null && authentication.isAuthenticated()) {
            TelegramUser user = ((TelegramUserDetails) authentication.getPrincipal()).getUser();
            model.addAttribute("user", user);
            log.info("User: {}", String.valueOf(user));
            return "profile";
        }
        log.info("User not authenticated");
        return "index";
    }

    @PostMapping("/auth/telegram")
    public String authenticate(@RequestBody TelegramUser user,
                                          HttpSession session,
                                          HttpServletResponse response) {
        log.info("/auth/telegram User: {}", String.valueOf(user));
        try {
            if (user != null && user.getId() != null) {
                String userJson = new ObjectMapper().writeValueAsString(user);
                Cookie cookie = new Cookie("telegram_auth", URLEncoder.encode(userJson, StandardCharsets.UTF_8));
                cookie.setPath("/");
                cookie.setMaxAge(86400);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                response.addCookie(cookie);
                session.setAttribute("telegramUser", user);
                return "redirect:profile";
            }
            return "redirect:error";
        } catch (Exception e) {
            return "redirect:error";
        }
    }
}
