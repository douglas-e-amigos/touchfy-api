package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity()
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "nome_usuario", nullable = false, length = 200, unique = true)
    private String nomeUsuario;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private Email email;

    @Column(name = "caminho_da_imagem_de_perfil", length = 255)
    private String caminhoDaImagemDePerfil;

    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = false;

    @Column(name = "data_nascimento", nullable = false)
    private Date dataNascimento;
}
