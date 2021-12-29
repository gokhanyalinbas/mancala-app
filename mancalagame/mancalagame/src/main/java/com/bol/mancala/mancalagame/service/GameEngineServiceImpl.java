package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.dto.PlayGameRequestDto;
import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.exception.UnexpectedMoveException;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GameEngineServiceImpl implements GameEngineService {


    private MancalaGameRepositoryServiceImpl mancalaGameService;
    private ModelMapper modelMapper;

    public GameEngineServiceImpl(MancalaGameRepositoryServiceImpl mancalaGameService, ModelMapper modelMapper) {
        this.mancalaGameService = mancalaGameService;
        this.modelMapper = modelMapper;
    }

    @Override
    public GameResponseDto play(PlayGameRequestDto gameRequestDto) {
        //1st: Find game on DB
        MancalaGame game = modelMapper.map(mancalaGameService.findGame(gameRequestDto.getGameId()), MancalaGame.class);
        Pit currentPit = game.getGameBoard().getPit(gameRequestDto.getPitId());
        //2nd: Apply game rules
        applyRules(game, currentPit);
        //3th: Make your move
        game = sow(game, currentPit);
        //4th: Save game
        return mancalaGameService.updateGame(game);

    }

    private MancalaGame sow(MancalaGame game, Pit currentPit) {
        int pitId = currentPit.getId() + 1;
        int turnStoneCount = currentPit.getStoneCount();
        //distrubite stones by one
        while (turnStoneCount > 0) {
            Pit pit = game.getGameBoard().getPit(pitId);
            //Don't put opposite player's house pit
            if (!(pit.isHouse() && !pit.getCurrentTurn().equals(game.getTurn()))) {
                pit.setStoneCount(pit.getStoneCount() + 1);
                game.getGameBoard().setPit(pit, pitId);
                turnStoneCount--;
            }
            pitId++;
        }
        currentPit.setStoneCount(0);
        game.getGameBoard().setPit(currentPit, currentPit.getId());

        Pit lastStonePit = game.getGameBoard().getPit(pitId - 1);
        // apply last stone rule and check for winner
        game = control(game, lastStonePit);
        game.setTurn(nextTurn(lastStonePit.getId(), game.getTurn()));
        return game;
    }

    private MancalaGame control(MancalaGame game, Pit lastStonePit) {
        // apply last stone rule
        game = lastPitControl(game, lastStonePit);
        // After last move, control game is if finished or not
        if (checkIfGameFinished(game)) {
            game.setWinner(getWinner(game));
        }
        return game;
    }

    private MancalaGame lastPitControl(MancalaGame game, Pit lastStonePit) {
        if (!lastStonePit.isHouse() && lastStonePit.getCurrentTurn() == game.getTurn()
                && lastStonePit.getStoneCount() == 1) {

            int playerHouseIndex = lastStonePit.getId() < MancalaConst.PLAYER_A_PIT_HOUSE ? MancalaConst.PLAYER_A_PIT_HOUSE : MancalaConst.PLAYER_B_PIT_HOUSE;
            Pit oppositePit = game.getGameBoard().getPit(MancalaConst.LAST_PIT - lastStonePit.getId());
            Pit house = game.getGameBoard().getPit(playerHouseIndex);
            house.setStoneCount(house.getStoneCount() + oppositePit.getStoneCount() + lastStonePit.getStoneCount());
            game.getGameBoard().setPit(house, playerHouseIndex);
            oppositePit.setStoneCount(MancalaConst.EMPTY_STONE);
            lastStonePit.setStoneCount(MancalaConst.EMPTY_STONE);
            game.getGameBoard().setPit(oppositePit, oppositePit.getId());
            game.getGameBoard().setPit(lastStonePit, lastStonePit.getId());
        }
        return game;
    }

    private boolean checkIfGameFinished(MancalaGame game) {

        return (Integer) game.getGameBoard().getAllScoreBoard().get(game.getTurn()) == 0;
    }

    private Player getWinner(MancalaGame game) {
        // Total point : House pit score + remain pits scores
        int playerAPoint = game.getGameBoard().getPit(MancalaConst.PLAYER_A_PIT_HOUSE).getStoneCount() +
                game.getGameBoard().getScoreBoard().get(Turn.PlayerA);
        int playerBPoint = game.getGameBoard().getPit(MancalaConst.PLAYER_B_PIT_HOUSE).getStoneCount() +
                game.getGameBoard().getScoreBoard().get(Turn.PlayerB);
        //If points are equal, the winner is PlayerB. Because PlayerA turned first :)
        if (playerAPoint > playerBPoint)
            return game.getPlayerA();
        return game.getPlayerB();
    }

    private Turn nextTurn(int lastPitId, Turn currentTurn) {
        if (lastPitId == MancalaConst.PLAYER_A_PIT_HOUSE && currentTurn.equals(Turn.PlayerA))
            return Turn.PlayerA;
        else if (lastPitId == MancalaConst.PLAYER_B_PIT_HOUSE && currentTurn.equals(Turn.PlayerB))
            return Turn.PlayerB;
        else if (currentTurn.equals(Turn.PlayerA))
            return Turn.PlayerB;
        return Turn.PlayerA;
    }

    private void applyRules(MancalaGame currentGame, Pit currentPit) {
        //Pit is house or not ?
        if (currentPit.isHouse())
            throw new UnexpectedMoveException("You can't pick the house pit!");
        //Who is the owner of pit ?
        if (currentPit.getCurrentTurn() != currentGame.getTurn())
            throw new UnexpectedMoveException("This is not your turn ! It is " + currentGame.getTurn() + "'s turn.");
        // Stones count control
        if (currentPit.getStoneCount() == 0)
            throw new UnexpectedMoveException("You can not pick an empty pit !");
    }
}
