package com.bol.mancala.mancalagame.model;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameBoardTest {

    GameBoard gameBoard;

    @BeforeEach
    void setUp() {
        gameBoard = new GameBoard(IntStream.range(MancalaConst.FIRST_PIT, MancalaConst.LAST_PIT + 1)
                .mapToObj(i -> new Pit(i, MancalaConst.DEFAULT_STONE_COUNT))
                .collect(Collectors.toList()), new HashMap<>());
    }

    @Test
    @DisplayName("Valid pit id")
    void getPit() {
        int pit = gameBoard.getPit(10).getId();
        assertEquals(10, pit);

    }

    @Test
    @DisplayName("Valid pit id - greater than max pit")
    void getPitWithGreaterThanMaxPit() {
        int pit = gameBoard.getPit(15).getId();
        assertEquals(1, pit);

    }

    @Test
    @DisplayName("Set pit")
    void setPit() {
        Pit pit = new Pit(1, 9);
        gameBoard.setPit(pit, 1);
        assertEquals(9, gameBoard.getPit(1).getStoneCount());
    }

    @Test
    @DisplayName("Get Score")
    void getAllScoreBoard() {
        assertEquals(36, gameBoard.getAllScoreBoard().get(Turn.PlayerA));
        assertEquals(36, gameBoard.getAllScoreBoard().get(Turn.PlayerB));
    }

    @Test
    @DisplayName("Is house ?")
    void isHouse() {
        assertEquals(true, gameBoard.getPit(7).isHouse());
        assertEquals(true, gameBoard.getPit(14).isHouse());
        assertEquals(false, gameBoard.getPit(5).isHouse());

    }

    @Test
    @DisplayName("Current Turn")
    void currentTurn() {
        assertEquals(Turn.PlayerA, gameBoard.getPit(7).getCurrentTurn());
        assertEquals(Turn.PlayerA, gameBoard.getPit(1).getCurrentTurn());
        assertEquals(Turn.PlayerB, gameBoard.getPit(10).getCurrentTurn());
        assertEquals(Turn.PlayerB, gameBoard.getPit(14).getCurrentTurn());
    }
}