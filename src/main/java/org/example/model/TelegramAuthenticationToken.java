package org.example.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class TelegramAuthenticationToken extends AbstractAuthenticationToken {

    private final TelegramUser principal;

    public TelegramAuthenticationToken(TelegramUser principal) {
        super(Collections.emptyList());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return principal.getHash();
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}