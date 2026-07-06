package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ErroResponse;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.DomainException;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler handler;
  private HttpServletRequest request;

  @BeforeEach
  void setUp() {
    handler = new GlobalExceptionHandler();
    request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn("/test");
  }

  @Test
  void deveTratarDomainException() {
    final DomainException exception = new RecursoNaoEncontradoException("Recurso não encontrado.");

    final ResponseEntity<ErroResponse> response = handler.handleDomainException(exception, request);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Recurso não encontrado.", response.getBody().mensagem());
  }

  @Test
  void deveTratarAccessDenied() {
    final AccessDeniedException exception = new AccessDeniedException("Acesso negado");

    final ResponseEntity<ErroResponse> response = handler.handleAccessDenied(exception, request);

    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    assertEquals("Acesso negado", response.getBody().mensagem());
  }

  @Test
  void deveTratarMethodArgumentNotValid() throws Exception {
    final BindingResult bindingResult = mock(BindingResult.class);
    when(bindingResult.getFieldErrors())
        .thenReturn(List.of(new FieldError("obj", "nome", "nome não pode ser vazio")));
    final Method method =
        GlobalExceptionHandler.class.getMethod(
            "handleMethodArgumentNotValid",
            MethodArgumentNotValidException.class,
            HttpServletRequest.class);
    final var parameter = new org.springframework.core.MethodParameter(method, 0);
    final MethodArgumentNotValidException exception =
        new MethodArgumentNotValidException(parameter, bindingResult);

    final ResponseEntity<ErroResponse> response =
        handler.handleMethodArgumentNotValid(exception, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().mensagem().contains("nome: nome não pode ser vazio"));
  }

  @Test
  void deveTratarConstraintViolation() {
    final ConstraintViolation<?> violacao = mock(ConstraintViolation.class);
    when(violacao.getMessage()).thenReturn("valor inválido");
    final ConstraintViolationException exception =
        new ConstraintViolationException("erro", Set.of(violacao));

    final ResponseEntity<ErroResponse> response =
        handler.handleConstraintViolation(exception, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("valor inválido", response.getBody().mensagem());
  }

  @Test
  void deveTratarIllegalArgumentException() {
    final IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

    final ResponseEntity<ErroResponse> response = handler.handleIllegalArgument(exception, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Argumento inválido", response.getBody().mensagem());
  }

  @Test
  void deveTratarIllegalArgumentExceptionComMensagemNula() {
    final IllegalArgumentException exception = new IllegalArgumentException();

    final ResponseEntity<ErroResponse> response = handler.handleIllegalArgument(exception, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Dados da requisição inválidos.", response.getBody().mensagem());
  }

  @Test
  void deveTratarRuntimeExceptionComConflito() {
    final RuntimeException exception = new RuntimeException("conflito: recurso já existe");

    final ResponseEntity<ErroResponse> response = handler.handleRuntime(exception, request);

    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertTrue(response.getBody().mensagem().toLowerCase().startsWith("conflito:"));
  }

  @Test
  void deveLancarRuntimeExceptionSemConflito() {
    final RuntimeException exception = new RuntimeException("erro qualquer");

    assertThrows(RuntimeException.class, () -> handler.handleRuntime(exception, request));
  }

  @Test
  void deveTratarResponseStatusException() {
    final ResponseStatusException exception =
        new ResponseStatusException(HttpStatus.BAD_GATEWAY, "falha no gateway");

    final ResponseEntity<ErroResponse> response = handler.handleResponseStatus(exception, request);

    assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
    assertEquals("falha no gateway", response.getBody().mensagem());
  }

  @Test
  void deveTratarResponseStatusExceptionComReasonNulo() {
    final ResponseStatusException exception = new ResponseStatusException(HttpStatus.BAD_REQUEST);

    final ResponseEntity<ErroResponse> response = handler.handleResponseStatus(exception, request);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Falha ao processar a requisição.", response.getBody().mensagem());
  }

  @Test
  void deveTratarExceptionGenerica() {
    final Exception exception = new Exception("erro inesperado");

    final ResponseEntity<ErroResponse> response = handler.handleGeneric(exception, request);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertTrue(response.getBody().mensagem().startsWith("Erro interno inesperado"));
    assertNotNull(response.getBody().traceId());
  }

  @Test
  void deveIncluirTraceIdNaResposta() {
    final DomainException exception = new RecursoNaoEncontradoException("teste");

    final ResponseEntity<ErroResponse> response = handler.handleDomainException(exception, request);

    assertNotNull(response.getBody().traceId());
    assertFalse(response.getBody().traceId().isBlank());
  }
}
