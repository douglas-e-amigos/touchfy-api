package com.ufrn.dct.bsi.touchfy.shared.converters;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(final Email email) {
        return email == null ? null : email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(final String value) {
        return value == null ? null : new Email(value);
    }
}
