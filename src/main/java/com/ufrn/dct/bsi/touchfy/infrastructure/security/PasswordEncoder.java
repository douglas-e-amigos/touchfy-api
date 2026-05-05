package com.ufrn.dct.bsi.touchfy.infrastructure.security;

public interface PasswordEncoder {
    Boolean matches(final String senhaPura, final String senhaCriptografada);
}
