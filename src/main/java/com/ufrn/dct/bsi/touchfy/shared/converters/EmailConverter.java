package com.ufrn.dct.bsi.touchfy.shared.converters;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {

    @Override
    public String convertToDatabaseColumn(Email email) {
        return email == null ? null : email.getValue();
    }

    @Override
    public Email convertToEntityAttribute(String value) {
        return value == null ? null : Email.builder().value(value).build();
    }
}
