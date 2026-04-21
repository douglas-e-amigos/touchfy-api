package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.infrastructure.security.PasswordMaker;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordMakerImpl implements PasswordMaker {
    private final PasswordEncoder passwordEncoder;

    @Override
    public String execute(final String password) {
        return passwordEncoder.encode(password);
    }
}
