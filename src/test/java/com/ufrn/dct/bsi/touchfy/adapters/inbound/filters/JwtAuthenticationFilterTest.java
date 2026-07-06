package com.ufrn.dct.bsi.touchfy.adapters.inbound.filters;

import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.infrastructure.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

class JwtAuthenticationFilterTest {

  private TokenService tokenService;
  private UserDetailsService userDetailsService;
  private JwtAuthenticationFilter filter;
  private HttpServletRequest request;
  private HttpServletResponse response;
  private FilterChain filterChain;

  @BeforeEach
  void setUp() {
    tokenService = mock(TokenService.class);
    userDetailsService = mock(UserDetailsService.class);
    filter = new JwtAuthenticationFilter(tokenService, userDetailsService);
    request = mock(HttpServletRequest.class);
    response = mock(HttpServletResponse.class);
    filterChain = mock(FilterChain.class);
  }

  @Test
  void deveContinuarChainQuandoAuthorizationForNulo() throws Exception {
    when(request.getHeader("Authorization")).thenReturn(null);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
    verifyNoInteractions(tokenService);
  }

  @Test
  void deveContinuarChainQuandoAuthorizationNaoForBearer() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Basic token");

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void deveRetornar401QuandoTokenForInvalido() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer tokenInvalido");
    when(tokenService.isTokenValid("tokenInvalido")).thenReturn(false);

    filter.doFilterInternal(request, response, filterChain);

    verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    verifyNoMoreInteractions(filterChain);
  }

  @Test
  void deveAutenticarQuandoTokenForValido() throws Exception {
    final String token = "tokenValido";
    final String username = "usuario";
    final UserDetails userDetails = mock(UserDetails.class);

    when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
    when(tokenService.isTokenValid(token)).thenReturn(true);
    when(tokenService.getSubject(token)).thenReturn(username);
    when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);

    filter.doFilterInternal(request, response, filterChain);

    verify(filterChain).doFilter(request, response);
  }

  @Test
  void deveRetornar401QuandoExtrairSubjectLancarExcecao() throws Exception {
    when(request.getHeader("Authorization")).thenReturn("Bearer tokenEx");
    when(tokenService.isTokenValid("tokenEx")).thenReturn(true);
    when(tokenService.getSubject("tokenEx")).thenThrow(new RuntimeException("erro"));

    filter.doFilterInternal(request, response, filterChain);

    verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
