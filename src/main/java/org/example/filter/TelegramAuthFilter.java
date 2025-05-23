package org.example.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.model.TelegramAuthenticationToken;
import org.example.model.TelegramUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class TelegramAuthFilter extends OncePerRequestFilter {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("telegram_auth".equals(cookie.getName())) {
                    try {
                        TelegramUser user = parseUserData(cookie.getValue());
                        if (validateTelegramUser(user)) {
                            Authentication auth = new TelegramAuthenticationToken(user);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private TelegramUser parseUserData(String data) throws JsonProcessingException {
        // Парсинг данных из cookie
        return new ObjectMapper().readValue(data, TelegramUser.class);
    }

    private boolean validateTelegramUser(TelegramUser user) {
        try {
            // Проверка срока действия данных (не старше 1 дня)
            long authDate = Long.parseLong(user.getAuthDate());
            if (System.currentTimeMillis() / 1000 - authDate > 86400) {
                return false;
            }

            // Проверка HMAC
            String dataCheckString = buildCheckString(user);
            String secretKey = HmacSHA256(botToken, "WebAppData");
            String calculatedHash = HmacSHA256(secretKey, dataCheckString);

            return calculatedHash.equals(user.getHash());
        } catch (Exception e) {
            return false;
        }
    }

    private String buildCheckString(TelegramUser user) {
        return Stream.of(
                        "auth_date=" + user.getAuthDate(),
                        "first_name=" + (user.getFirstName() != null ? user.getFirstName() : ""),
                        "id=" + user.getId(),
                        "last_name=" + (user.getLastName() != null ? user.getLastName() : ""),
                        "photo_url=" + (user.getPhotoUrl() != null ? user.getPhotoUrl() : ""),
                        "username=" + (user.getUsername() != null ? user.getUsername() : "")
                ).filter(s -> !s.endsWith("="))
                .sorted()
                .collect(Collectors.joining("\n"));
    }

    private String HmacSHA256(String key, String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secretKey);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}
