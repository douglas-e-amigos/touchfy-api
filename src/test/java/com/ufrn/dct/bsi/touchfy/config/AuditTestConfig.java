package com.ufrn.dct.bsi.touchfy.config;

import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@TestConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditTestConfig {

  @Bean
  @Primary
  public AuditorAware<UUID> auditorProvider() {
    return () -> {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
        return Optional.empty();
      }
      return Optional.of(UUID.nameUUIDFromBytes(auth.getName().getBytes()));
    };
  }
}
