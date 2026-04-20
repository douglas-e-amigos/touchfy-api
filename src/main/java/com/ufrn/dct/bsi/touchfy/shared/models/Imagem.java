package com.ufrn.dct.bsi.touchfy.shared.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Imagem {
    private String nome;
    private String caminhoDoArquivo;
    private String extensao;
    private Double tamanhoEmBytes;
    private String textoAlternativo;
}
