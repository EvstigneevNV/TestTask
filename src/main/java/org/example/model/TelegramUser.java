package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TelegramUser {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String photoUrl;
    private String authDate;
    private String hash;
}
