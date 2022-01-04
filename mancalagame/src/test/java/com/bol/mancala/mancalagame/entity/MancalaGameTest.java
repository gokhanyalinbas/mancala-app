package com.bol.mancala.mancalagame.entity;

import com.bol.mancala.mancalagame.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class MancalaGameTest {

    MancalaGame mancalaGame;

    @BeforeEach
    void setup() {
        mancalaGame = new MancalaGame(new Player("1", "Gokhan"), new Player("2", "Hulya"));
    }

    @Test
    void getPlayerA() {
        assertNotNull(mancalaGame.getPlayerA());
    }

    @Test
    void getWinner() {
        mancalaGame.setWinner(new Player("1", "Gokhan"));
        assertNotNull(mancalaGame.getWinner());
    }
}