package com.bol.mancala.mancalagame.exception;

public class UnexpectedMoveException extends RuntimeException {
    public UnexpectedMoveException(String s) {
        super(s);
    }
}
