package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ErroResponse;
import com.ufrn.dct.bsi.touchfy.shared.exceptions.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
  private static final String PREFIXO_PACOTE_APLICACAO = "com.ufrn.dct.bsi.touchfy";

  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ErroResponse> handleDomainException(
      final DomainException exception, final HttpServletRequest request) {
    return buildResponse(exception.getStatus(), exception.getMessage(), exception, request);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErroResponse> handleAccessDenied(
      final AccessDeniedException exception, final HttpServletRequest request) {
    return buildResponse(HttpStatus.FORBIDDEN, exception.getMessage(), exception, request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErroResponse> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException exception, final HttpServletRequest request) {
    final String mensagem =
        exception.getBindingResult().getFieldErrors().stream()
            .map(this::formatarErroDeCampo)
            .reduce((primeiro, segundo) -> primeiro + "; " + segundo)
            .orElse("Dados da requisição inválidos.");

    return buildResponse(HttpStatus.BAD_REQUEST, mensagem, exception, request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ErroResponse> handleConstraintViolation(
      final ConstraintViolationException exception, final HttpServletRequest request) {
    final String mensagem =
        exception.getConstraintViolations().stream()
            .map(violacao -> violacao.getMessage())
            .filter(Objects::nonNull)
            .reduce((primeiro, segundo) -> primeiro + "; " + segundo)
            .orElse("Dados da requisição inválidos.");

    return buildResponse(HttpStatus.BAD_REQUEST, mensagem, exception, request);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErroResponse> handleIllegalArgument(
      final IllegalArgumentException exception, final HttpServletRequest request) {
    final String mensagem =
        exception.getMessage() == null || exception.getMessage().isBlank()
            ? "Dados da requisição inválidos."
            : exception.getMessage();

    return buildResponse(HttpStatus.BAD_REQUEST, mensagem, exception, request);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErroResponse> handleRuntime(
      final RuntimeException exception, final HttpServletRequest request) {
    final String msg = exception.getMessage();

    if (msg != null && msg.toLowerCase().startsWith("conflito:")) {
      return buildResponse(HttpStatus.CONFLICT, msg, exception, request);
    }

    throw exception;
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<ErroResponse> handleResponseStatus(
      final ResponseStatusException exception, final HttpServletRequest request) {
    final String mensagem =
        exception.getReason() == null || exception.getReason().isBlank()
            ? "Falha ao processar a requisição."
            : exception.getReason();

    return buildResponse(
        HttpStatus.valueOf(exception.getStatusCode().value()), mensagem, exception, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErroResponse> handleGeneric(
      final Exception exception, final HttpServletRequest request) {
    final String traceId = UUID.randomUUID().toString();
    LOGGER.error("traceId={} path={} erro inesperado", traceId, request.getRequestURI(), exception);

    final ErroResponse response =
        new ErroResponse(
            "Erro interno inesperado. Se o problema persistir, informe o código: " + traceId,
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            Instant.now(),
            traceId);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private ResponseEntity<ErroResponse> buildResponse(
      final HttpStatus status,
      final String detalhe,
      final Throwable throwable,
      final HttpServletRequest request) {
    final String traceId = UUID.randomUUID().toString();
    final StackTraceElement origem = findOriginalFrame(throwable);
    final String arquivo = origem == null ? "desconhecido" : origem.getFileName();
    final Integer linha = origem == null ? null : origem.getLineNumber();

    LOGGER.error(
        "traceId={} status={} path={} mensagem='{}' arquivo='{}' linha={}",
        traceId,
        status.value(),
        request.getRequestURI(),
        detalhe,
        arquivo,
        linha,
        throwable);

    final ErroResponse response = new ErroResponse(detalhe, status.value(), Instant.now(), traceId);

    return ResponseEntity.status(status).body(response);
  }

  private StackTraceElement findOriginalFrame(final Throwable throwable) {
    Throwable atual = throwable;

    while (atual != null) {
      final StackTraceElement frameDaAplicacao =
          Arrays.stream(atual.getStackTrace())
              .filter(this::isApplicationFrame)
              .findFirst()
              .orElse(null);

      if (frameDaAplicacao != null) {
        return frameDaAplicacao;
      }

      atual = atual.getCause();
    }

    final StackTraceElement[] stackTrace = throwable.getStackTrace();
    return stackTrace.length == 0 ? null : stackTrace[0];
  }

  private boolean isApplicationFrame(final StackTraceElement frame) {
    return frame.getClassName() != null
        && frame.getClassName().startsWith(PREFIXO_PACOTE_APLICACAO)
        && !frame.getClassName().equals(GlobalExceptionHandler.class.getName());
  }

  private String formatarErroDeCampo(final FieldError fieldError) {
    final String mensagem =
        fieldError.getDefaultMessage() == null || fieldError.getDefaultMessage().isBlank()
            ? "valor inválido"
            : fieldError.getDefaultMessage();

    return fieldError.getField() + ": " + mensagem;
  }
}
