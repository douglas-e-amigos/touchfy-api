package com.ufrn.dct.bsi.touchfy.shared.dtos;

public record ErroResponse(
        String mensagem,
        int statusCode
) {
}