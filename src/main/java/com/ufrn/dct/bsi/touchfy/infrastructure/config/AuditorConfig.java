package com.ufrn.dct.bsi.touchfy.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAwareImpl")
public class AuditorConfig {}
