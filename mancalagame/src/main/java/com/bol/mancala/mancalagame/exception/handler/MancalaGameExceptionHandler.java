package com.bol.mancala.mancalagame.exception.handler;

import com.bol.mancala.mancalagame.exception.ExceptionResponse;
import com.bol.mancala.mancalagame.exception.LoginSessionExpiredException;
import com.bol.mancala.mancalagame.exception.MancalaGameNotFoundException;
import com.bol.mancala.mancalagame.exception.UnexpectedMoveException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MancalaGameExceptionHandler {

    @ExceptionHandler(MancalaGameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public final ExceptionResponse handleMancalaGameNotFoundException(Exception ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return new ExceptionResponse("NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UnexpectedMoveException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public final ExceptionResponse handleUnexpectedMoveException(Exception ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return new ExceptionResponse("BAD_REQUEST", ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ExceptionResponse handleAuthenticationException(Exception ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return new ExceptionResponse("LOGIN_ERROR", ex.getMessage());
    }

    @ExceptionHandler(LoginSessionExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public final ExceptionResponse handleLoginSessionExpiredException(Exception ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return new ExceptionResponse("SESSION_EXPIRED", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleAllOtherExceptions(Exception ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        return new ExceptionResponse("INTERNAL_SERVER_ERROR", ex.getMessage());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleConstraintViolationException(ConstraintViolationException ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        String violations = ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("|"));
        return new ExceptionResponse("validation violation", violations);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        System.err.println(ex.getClass().getSimpleName() + ": " + ex.getMessage());
        String violations = ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        return new ExceptionResponse("validation violation", violations);
    }
}
