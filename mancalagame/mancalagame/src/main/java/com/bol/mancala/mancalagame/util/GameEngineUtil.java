package com.bol.mancala.mancalagame.util;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.exception.UnexpectedMoveException;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;

public class GameEngineUtil {

    private static final int ONE = 1;
    private static final int ZERO = 0;

    public static void applyRules(MancalaGame currentGame, Pit currentPit) {
        //Pit is Big pit or not ?
        if (currentPit.isBigPit())
            throw new UnexpectedMoveException("You can't pick the big pit!");
        //Who is the owner of pit ?
        if (currentPit.getCurrentTurn() != currentGame.getTurn())
            throw new UnexpectedMoveException("This is not your turn ! It is " + currentGame.getTurn() + "'s turn.");
        // Stones count control
        if (currentPit.getStoneCount() == ZERO)
            throw new UnexpectedMoveException("You can not pick an empty pit !");

    }

    public static MancalaGame control(MancalaGame game, Pit lastStonePit) {
        // apply last stone rule
        game = lastPitControl(game, lastStonePit);
        // After last move, control game is if finished or not
        if (checkIfGameFinished(game)) {
            game.setWinner(getWinner(game));
        }
        return game;
    }

    public static MancalaGame lastPitControl(MancalaGame game, Pit lastStonePit) {
        if (isLastPit(game, lastStonePit)) {

            int playerBigPitIndex = lastStonePit.getId() < MancalaConst.PLAYER_A_PIT_BIG_PIT ? MancalaConst.PLAYER_A_PIT_BIG_PIT : MancalaConst.PLAYER_B_PIT_BIG_PIT;
            Pit oppositePit = game.getGameBoard().getPit(MancalaConst.LAST_PIT - lastStonePit.getId());
            Pit bigPit = game.getGameBoard().getPit(playerBigPitIndex);
            game = processLastPit(game, lastStonePit, playerBigPitIndex, oppositePit, bigPit);
        }
        return game;
    }

    private static boolean isLastPit(MancalaGame game, Pit lastStonePit) {
        return !lastStonePit.isBigPit() && lastStonePit.getCurrentTurn() == game.getTurn()
                && lastStonePit.getStoneCount() == ONE;
    }

    public static MancalaGame processLastPit(MancalaGame game, Pit lastStonePit, int playerBigPitIndex, Pit oppositePit, Pit bigPit) {
        bigPit.setStoneCount(bigPit.getStoneCount() + oppositePit.getStoneCount() + lastStonePit.getStoneCount());
        game.getGameBoard().setPit(bigPit, playerBigPitIndex);
        oppositePit.setStoneCount(MancalaConst.EMPTY_STONE);
        lastStonePit.setStoneCount(MancalaConst.EMPTY_STONE);
        game.getGameBoard().setPit(oppositePit, oppositePit.getId());
        game.getGameBoard().setPit(lastStonePit, lastStonePit.getId());
        return game;
    }

    public static boolean checkIfGameFinished(MancalaGame game) {

        return (Integer) game.getGameBoard().getAllScoreBoard().get(game.getTurn()) == 0;
    }

    public static Player getWinner(MancalaGame game) {
        // Total point : Big pit score + remain pits scores
        int playerAPoint = game.getGameBoard().getPit(MancalaConst.PLAYER_A_PIT_BIG_PIT).getStoneCount() +
                game.getGameBoard().getScoreBoard().get(Turn.PlayerA);
        int playerBPoint = game.getGameBoard().getPit(MancalaConst.PLAYER_B_PIT_BIG_PIT).getStoneCount() +
                game.getGameBoard().getScoreBoard().get(Turn.PlayerB);
        //If points are equal, the winner is PlayerB. Because PlayerA turned first :)
        if (playerAPoint > playerBPoint)
            return game.getPlayerA();
        return game.getPlayerB();
    }

    public static Turn nextTurn(int lastPitId, Turn currentTurn) {
        if (lastPitId == MancalaConst.PLAYER_A_PIT_BIG_PIT && currentTurn.equals(Turn.PlayerA))
            return Turn.PlayerA;
        else if (lastPitId == MancalaConst.PLAYER_B_PIT_BIG_PIT && currentTurn.equals(Turn.PlayerB))
            return Turn.PlayerB;
        else if (currentTurn.equals(Turn.PlayerA))
            return Turn.PlayerB;
        return Turn.PlayerA;
    }

    public static MancalaGame distributeAllStones(MancalaGame game, Pit currentPit) {
        int pitId = currentPit.getId() + ONE;
        int turnStoneCount = currentPit.getStoneCount();
        //distribute stones by one
        for (int i = 0; i < turnStoneCount; i++) {
            Pit pit = distributeByOne(game, pitId);
            game.getGameBoard().setPit(pit, pitId);
            pitId++;
        }

        currentPit.setStoneCount(ZERO);
        game.getGameBoard().setPit(currentPit, currentPit.getId());
        Pit lastStonePit = game.getGameBoard().getPit(pitId - ONE);
        // apply last stone rule and check for winner
        game = control(game, lastStonePit);
        game.setTurn(nextTurn(lastStonePit.getId(), game.getTurn()));
        return game;
    }

    private static Pit distributeByOne(MancalaGame game, int pitId) {
        Pit pit = game.getGameBoard().getPit(pitId);
        pit.setStoneCount(pit.getStoneCount() + ONE);
        return pit;
    }


}
