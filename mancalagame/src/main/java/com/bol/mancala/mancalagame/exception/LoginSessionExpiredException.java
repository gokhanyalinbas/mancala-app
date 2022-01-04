package com.bol.mancala.mancalagame.exception;

public class LoginSessionExpiredException extends RuntimeException {
    public LoginSessionExpiredException(String s) {
        super(s);
    }
}
