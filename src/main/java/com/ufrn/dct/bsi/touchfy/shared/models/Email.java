package com.ufrn.dct.bsi.touchfy.shared.models;

import java.util.regex.Pattern;

import lombok.Getter;

@Getter
public class Email {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final Pattern PATTERN = Pattern.compile(EMAIL_REGEX);
    private final String value;

    public Email(final String value) {
        if (value == null || !PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Email inválido");
        }
        this.value = value;
    }
}