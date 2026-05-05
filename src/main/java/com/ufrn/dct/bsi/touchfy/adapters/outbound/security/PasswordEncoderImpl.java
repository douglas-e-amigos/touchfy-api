package com.ufrn.dct.bsi.touchfy.adapters.outbound.security;

import com.ufrn.dct.bsi.touchfy.infrastructure.security.PasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PasswordEncoderImpl implements PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public Boolean matches(final String senhaPura, final String senhaCriptografada) {
        return passwordEncoder.matches(senhaPura, senhaCriptografada);
    }
}
