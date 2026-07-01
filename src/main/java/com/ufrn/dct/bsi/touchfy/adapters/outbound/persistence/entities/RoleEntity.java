package com.ufrn.dct.bsi.touchfy.adapters.outbound.persistence.entities;

import com.ufrn.dct.bsi.touchfy.domain.role.ERole;
import jakarta.persistence.*;
import java.util.Set;
import lombok.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  private ERole name;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "roles_permissions",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
  private Set<PermissionEntity> permissions;
}
