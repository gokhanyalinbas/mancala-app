package com.bol.mancala.mancalagame.model;

import lombok.Getter;

@Getter
public enum Turn {
    PlayerA("A"), PlayerB("B");

    private final String turn;

    Turn(String turn) {
        this.turn = turn;
    }

}
