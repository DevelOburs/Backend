package com.fridgify.user_api.entity;

import lombok.Getter;

@Getter
public class DummyUser {
    private final String username;
    private final String password;

    public DummyUser(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

