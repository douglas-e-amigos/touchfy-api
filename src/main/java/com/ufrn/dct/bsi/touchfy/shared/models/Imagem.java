package com.ufrn.dct.bsi.touchfy.shared.models;

import lombok.*;

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
