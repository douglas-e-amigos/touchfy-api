package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.mappers;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmailMapper {
    @Named("mapEmailToValue")
    default String toValue(Email email) {
        if  (email == null) {
            return null;
        }
        return email.getValue();
    }

    @Named("mapValueToEmail")
    default Email toObject(String value) {
        return new Email(value);
    }
}
