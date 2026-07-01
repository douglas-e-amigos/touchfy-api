package com.ufrn.dct.bsi.touchfy.domain.role;

import com.ufrn.dct.bsi.touchfy.domain.permission.Permission;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
  private Long id;
  private ERole name;
  private Set<Permission> permissions;
}
