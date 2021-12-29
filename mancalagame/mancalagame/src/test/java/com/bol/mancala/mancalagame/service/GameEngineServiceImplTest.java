package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.dto.*;
import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.exception.UnexpectedMoveException;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GameEngineServiceImplTest {


    GameEngineServiceImpl gameEngineService;
    CreateGameRequestDto gameRequestDto;
    PlayGameRequestDto playGameRequestDto;
    @Mock
    private MancalaGameRepositoryServiceImpl mancalaGameRepositoryService;

    @Autowired
    private ModelMapper modelMapper;
    private MancalaGame mancalaGame;
    private MancalaGame updatedMancalaGame;
    private GameResponseDto gameResponseDto;
    private GameResponseDto updatedGameResponseDto;

    @BeforeEach
    void init() {
        gameEngineService = new GameEngineServiceImpl(mancalaGameRepositoryService, modelMapper);
        gameRequestDto = new CreateGameRequestDto(new Player("1", "PlayerA"), new Player("2", "PlayerB"));
        mancalaGame = new MancalaGame(new Player("1", "PlayerA"), new Player("2", "PlayerB"));
        mancalaGame.setId("12");
        updatedMancalaGame = new MancalaGame(new Player("1", "PlayerA"), new Player("2", "PlayerB"));
        updatedMancalaGame.setId("12");
        gameResponseDto = GameResponseDto.builder()
                .gameBoard(new GameBoardDto(mancalaGame.getGameBoard().getPits(), mancalaGame.getGameBoard().getScoreBoard()))
                .id(mancalaGame.getId())
                .playerA(new PlayerDto(mancalaGame.getPlayerA().getId(), mancalaGame.getPlayerA().getName()))
                .playerB(new PlayerDto(mancalaGame.getPlayerB().getId(), mancalaGame.getPlayerB().getName()))
                .turn(mancalaGame.getTurn())
                .winner(mancalaGame.getWinner())
                .build();
        updatedGameResponseDto = gameResponseDto;
        playGameRequestDto = new PlayGameRequestDto(1, "12");
    }

    @Test
    @DisplayName("Play with valid stone count")
    void play() {
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(gameResponseDto);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertEquals(play.getGameBoard().getPits().get(0).getStoneCount(), 0);
        assertEquals(play.getGameBoard().getPits().get(2).getStoneCount(), 7);
    }

    @Test
    @DisplayName("Get winner for PlayerA")
    void decideToWinnerForPlayerA() {
        gameResponseDto.getGameBoard().getPits().get(0).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(1).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(2).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(3).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(4).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(5).setStoneCount(1);
        gameResponseDto.getGameBoard().getPits().get(6).setStoneCount(Integer.MAX_VALUE - 1);

        updatedGameResponseDto.setWinner(mancalaGame.getPlayerA());
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(6);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getWinner().getId()).isEqualTo(updatedGameResponseDto.getWinner().getId());
        assertThat(play.getWinner().getName()).isEqualTo(updatedGameResponseDto.getWinner().getName());
    }

    @Test
    @DisplayName("Get winner for PlayerB")
    void decideToWinnerForPlayerB() {
        gameResponseDto.getGameBoard().getPits().get(0).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(1).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(2).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(3).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(4).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(5).setStoneCount(1);
        gameResponseDto.getGameBoard().getPits().get(13).setStoneCount(Integer.MAX_VALUE - 50);

        updatedGameResponseDto.setWinner(mancalaGame.getPlayerB());
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(6);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getWinner().getId()).isEqualTo(updatedGameResponseDto.getWinner().getId());
        assertThat(play.getWinner().getName()).isEqualTo(updatedGameResponseDto.getWinner().getName());
    }

    @Test
    @DisplayName("Last pit control-opposite pit has stone that greater than 0")
    void lastPitControlWithStoneCount() {
        gameResponseDto.getGameBoard().getPits().get(0).setStoneCount(1);
        gameResponseDto.getGameBoard().getPits().get(1).setStoneCount(0);
        gameResponseDto.getGameBoard().getPits().get(11).setStoneCount(6);

        updatedGameResponseDto.getGameBoard().getPits().get(6).setStoneCount(7);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(1);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getGameBoard().getPits().get(6).getStoneCount()).isEqualTo(updatedGameResponseDto.getGameBoard().getPits().get(6).getStoneCount());
        ;
    }

    @Test
    @DisplayName("Next Turn For playerA - housePit")
    void nextTurnForPlayerAHousePit() {
        gameResponseDto.getGameBoard().getPits().get(5).setStoneCount(1);

        updatedGameResponseDto.setTurn(Turn.PlayerA);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(6);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getTurn()).isEqualTo(updatedGameResponseDto.getTurn());
        ;
    }

    @Test
    @DisplayName("Next Turn For playerB - housePit")
    void nextTurnForPlayerBHousePit() {
        gameResponseDto.getGameBoard().getPits().get(12).setStoneCount(1);

        updatedGameResponseDto.setTurn(Turn.PlayerB);
        gameResponseDto.setTurn(Turn.PlayerB);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(13);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getTurn()).isEqualTo(updatedGameResponseDto.getTurn());
        ;
    }

    @Test
    @DisplayName("Next Turn For playerB - without housePit")
    void nextTurnForPlayerBWithoutHousePit() {
        gameResponseDto.getGameBoard().getPits().get(5).setStoneCount(3);

        updatedGameResponseDto.setTurn(Turn.PlayerB);
        gameResponseDto.setTurn(Turn.PlayerA);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(6);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getTurn()).isEqualTo(updatedGameResponseDto.getTurn());
        ;
    }

    @Test
    @DisplayName("Next Turn For playerA - without housePit")
    void nextTurnForPlayerAWithoutHousePit() {
        gameResponseDto.getGameBoard().getPits().get(12).setStoneCount(3);

        updatedGameResponseDto.setTurn(Turn.PlayerA);
        gameResponseDto.setTurn(Turn.PlayerB);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        when(mancalaGameRepositoryService.updateGame(any(MancalaGame.class))).thenReturn(updatedGameResponseDto);
        playGameRequestDto.setPitId(13);
        GameResponseDto play = gameEngineService.play(playGameRequestDto);
        assertThat(play.getTurn()).isEqualTo(updatedGameResponseDto.getTurn());
        ;
    }

    @Test
    @DisplayName("UnexpectedMove Error -PlayerA turn")
    void unexpectedMoveForPlayerB() {
        gameResponseDto.getGameBoard().getPits().get(12).setStoneCount(3);
        gameResponseDto.setTurn(Turn.PlayerA);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        playGameRequestDto.setPitId(13);
        UnexpectedMoveException exception = assertThrows(UnexpectedMoveException.class, () -> {
            gameEngineService.play(playGameRequestDto);
        });

        assertThat(exception).hasMessageContaining("PlayerA").isInstanceOf(UnexpectedMoveException.class);
    }

    @Test
    @DisplayName("UnexpectedMove Error -PlayerB turn")
    void unexpectedMoveForPlayerA() {
        gameResponseDto.getGameBoard().getPits().get(5).setStoneCount(3);
        gameResponseDto.setTurn(Turn.PlayerB);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        playGameRequestDto.setPitId(6);
        UnexpectedMoveException exception = assertThrows(UnexpectedMoveException.class, () -> {
            gameEngineService.play(playGameRequestDto);
        });

        assertThat(exception).hasMessageContaining("PlayerB").isInstanceOf(UnexpectedMoveException.class);
    }

    @Test
    @DisplayName("UnexpectedMove Error -HousePit move")
    void unexpectedMoveForHousePit() {
        gameResponseDto.setTurn(Turn.PlayerA);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        playGameRequestDto.setPitId(7);
        UnexpectedMoveException exception = assertThrows(UnexpectedMoveException.class, () -> {
            gameEngineService.play(playGameRequestDto);
        });

        assertThat(exception).hasMessageContaining("house").isInstanceOf(UnexpectedMoveException.class);
    }

    @Test
    @DisplayName("UnexpectedMove Error -Empty pit")
    void unexpectedMoveForEmptyPit() {
        gameResponseDto.getGameBoard().getPits().get(5).setStoneCount(0);
        gameResponseDto.setTurn(Turn.PlayerA);
        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);
        playGameRequestDto.setPitId(6);
        UnexpectedMoveException exception = assertThrows(UnexpectedMoveException.class, () -> {
            gameEngineService.play(playGameRequestDto);
        });

        assertThat(exception).hasMessageContaining("empty").isInstanceOf(UnexpectedMoveException.class);
    }
}