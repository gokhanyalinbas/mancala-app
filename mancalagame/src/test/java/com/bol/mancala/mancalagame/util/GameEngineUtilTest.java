package com.bol.mancala.mancalagame.util;

import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.exception.UnexpectedMoveException;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameEngineUtilTest {

    MancalaGame mancalaGame;
    Pit pit;

    @BeforeEach
    void setUp() {
        pit = new Pit(1, 6);
        mancalaGame = new MancalaGame(new Player("1", "Gokhan"), new Player("2", "Hulya"));
    }

    @Test
    @DisplayName("Apply rules for big pit")
    void applyRulesForBigPit() {

        pit.setId(7);

        Exception exception = assertThrows(UnexpectedMoveException.class, () -> {
            GameEngineUtil.applyRules(mancalaGame, pit);
        });
        assertThat(exception).hasMessageContaining("big pit");
    }

    @Test
    @DisplayName("Apply rules for next turn")
    void applyRulesForNextTurn() {

        pit.setId(9);

        Exception exception = assertThrows(UnexpectedMoveException.class, () -> {
            GameEngineUtil.applyRules(mancalaGame, pit);
        });
        assertThat(exception).hasMessageContaining("This is not your turn");
    }

    @Test
    @DisplayName("Apply rules for empty pit")
    void applyRulesForEmptyPit() {

        pit.setStoneCount(0);

        Exception exception = assertThrows(UnexpectedMoveException.class, () -> {
            GameEngineUtil.applyRules(mancalaGame, pit);
        });
        assertThat(exception).hasMessageContaining("You can not pick an empty pit");
    }

    @Test
    @DisplayName("No winner control")
    void controlForNoWinner() {
        mancalaGame = GameEngineUtil.control(mancalaGame, pit);
        assertNull(mancalaGame.getWinner());
    }

    @Test
    @DisplayName("Winner control")
    void controlForWinner() {
        mancalaGame.getGameBoard().getPits().get(0).setStoneCount(0);
        mancalaGame.getGameBoard().getPits().get(1).setStoneCount(0);
        mancalaGame.getGameBoard().getPits().get(2).setStoneCount(0);
        mancalaGame.getGameBoard().getPits().get(3).setStoneCount(0);
        mancalaGame.getGameBoard().getPits().get(4).setStoneCount(0);
        mancalaGame.getGameBoard().getPits().get(5).setStoneCount(0);
        mancalaGame.getGameBoard().getPits().get(6).setStoneCount(Integer.MAX_VALUE - 100);
        mancalaGame = GameEngineUtil.control(mancalaGame, pit);
        assertEquals(mancalaGame.getWinner().getName(), "Gokhan");
    }

    @Test
    @DisplayName("Last pit control-Not an empty pit")
    void lastPitControl() {
        mancalaGame = GameEngineUtil.lastPitControl(mancalaGame, pit);
        assertEquals(mancalaGame.getGameBoard().getPit(7).getStoneCount(), 0);
    }

    @Test
    @DisplayName("Last pit control-empty pit")
    void lastPitControlEmptyPit() {
        pit.setStoneCount(1);
        pit.setId(6);
        mancalaGame = GameEngineUtil.lastPitControl(mancalaGame, pit);
        assertEquals(mancalaGame.getGameBoard().getPit(7).getStoneCount(), 7);
    }


    @Test
    @DisplayName("Get next turn")
    void nextTurn() {
        assertEquals(GameEngineUtil.nextTurn(5, Turn.PlayerA), Turn.PlayerB);
        assertEquals(GameEngineUtil.nextTurn(8, Turn.PlayerB), Turn.PlayerA);
        assertEquals(GameEngineUtil.nextTurn(14, Turn.PlayerB), Turn.PlayerB);
        assertEquals(GameEngineUtil.nextTurn(7, Turn.PlayerA), Turn.PlayerA);
    }

    @Test
    @DisplayName("Distribute all stones")
    void distributeAllStones() {
        pit.setId(5);
        mancalaGame = GameEngineUtil.distributeAllStones(mancalaGame, pit);
        assertEquals(mancalaGame.getGameBoard().getPit(5).getStoneCount(), 0);
        assertEquals(mancalaGame.getGameBoard().getPit(7).getStoneCount(), 1);
        assertEquals(mancalaGame.getGameBoard().getPit(8).getStoneCount(), 7);
        assertEquals(mancalaGame.getTurn(), Turn.PlayerB);
    }
}