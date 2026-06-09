package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import com.ufrn.dct.bsi.touchfy.shared.models.Email;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.SQLRestriction;

@Entity
@SQLRestriction("ativo = true")
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Getter
@Setter
public class UsuarioEntity extends AuditableEntity {
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

    @Column(name = "caminho_da_foto_de_perfil", length = 255)
    private String caminhoDaImagemDePerfil;

    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = false;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns        = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles;
}

