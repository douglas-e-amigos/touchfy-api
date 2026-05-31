package com.ufrn.dct.bsi.touchfy.adapters.inbound.rest;

import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.ufrn.dct.bsi.touchfy.shared.dtos.ErroResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String MENSAGEM_PADRAO = "Erro ao processar a requisição: ";
    private static final String PREFIXO_PACOTE_APLICACAO = "com.ufrn.dct.bsi.touchfy";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        final String mensagem = exception.getBindingResult().getFieldErrors().stream()
                .map(this::formatarErroDeCampo)
                .reduce((primeiro, segundo) -> primeiro + "; " + segundo)
                .orElse("Dados da requisição inválidos.");

        return buildResponse(HttpStatus.BAD_REQUEST, mensagem, exception);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraintViolation(final ConstraintViolationException exception) {
        final String mensagem = exception.getConstraintViolations().stream()
                .map(violacao -> violacao.getMessage())
                .filter(Objects::nonNull)
                .reduce((primeiro, segundo) -> primeiro + "; " + segundo)
                .orElse("Dados da requisição inválidos.");

        return buildResponse(HttpStatus.BAD_REQUEST, mensagem, exception);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResponse> handleIllegalArgument(final IllegalArgumentException exception) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> handleResponseStatus(final ResponseStatusException exception) {
        final String mensagem = exception.getReason() == null || exception.getReason().isBlank()
                ? "Falha ao processar a requisição."
                : exception.getReason();

        return buildResponse(HttpStatus.valueOf(exception.getStatusCode().value()), mensagem, exception);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(final RuntimeException exception) {
        return buildResponse(resolveRuntimeStatus(exception), exception.getMessage(), exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGeneric(final Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception);
    }

    private ResponseEntity<ErroResponse> buildResponse(
            final HttpStatus status,
            final String detalhe,
            final Throwable throwable
    ) {
        final StackTraceElement origem = findOriginalFrame(throwable);
        final String mensagemCompleta = MENSAGEM_PADRAO + normalizeMessage(detalhe);
        logException(status, mensagemCompleta, origem, throwable);
        final ErroResponse response = new ErroResponse(
            mensagemCompleta,
            status.value()
        );

        return ResponseEntity.status(status).body(response);
    }

        private void logException(
            final HttpStatus status,
            final String mensagem,
            final StackTraceElement origem,
            final Throwable throwable
        ) {
        final String arquivo = origem == null ? "desconhecido" : origem.getFileName();
        final Integer linha = origem == null ? null : origem.getLineNumber();

        LOGGER.error(
            "statusCode={}, mensagem='{}', arquivo='{}', linha={}",
            status.value(),
            mensagem,
            arquivo,
            linha,
            throwable
        );
        }

    private HttpStatus resolveRuntimeStatus(final RuntimeException exception) {
        final String mensagem = normalizeMessage(exception.getMessage()).toLowerCase();

        if (mensagem.contains("não encontrado") || mensagem.contains("nao encontrado")) {
            return HttpStatus.NOT_FOUND;
        }

        if (mensagem.contains("não autenticado")
                || mensagem.contains("nao autenticado")
                || mensagem.contains("credenciais inválidas")
                || mensagem.contains("credenciais invalidas")
                || mensagem.contains("token inválido")
                || mensagem.contains("token invalido")
                || mensagem.contains("token expirado")
                || mensagem.contains("token revogado")) {
            return HttpStatus.UNAUTHORIZED;
        }

        if (mensagem.contains("não autorizado") || mensagem.contains("nao autorizado")) {
            return HttpStatus.FORBIDDEN;
        }

        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    private StackTraceElement findOriginalFrame(final Throwable throwable) {
        Throwable atual = throwable;

        while (atual != null) {
            final StackTraceElement frameDaAplicacao = Arrays.stream(atual.getStackTrace())
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
        final String mensagem = fieldError.getDefaultMessage() == null || fieldError.getDefaultMessage().isBlank()
                ? "valor inválido"
                : fieldError.getDefaultMessage();

        return fieldError.getField() + ": " + mensagem;
    }

    private String normalizeMessage(final String message) {
        return message == null || message.isBlank() ? "Falha inesperada." : message;
    }
}