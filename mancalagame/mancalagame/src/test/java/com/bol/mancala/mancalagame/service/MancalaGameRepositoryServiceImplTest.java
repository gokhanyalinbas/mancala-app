package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.dto.CreateGameRequestDto;
import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import com.bol.mancala.mancalagame.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class MancalaGameRepositoryServiceImplTest {


    CreateGameRequestDto gameRequestDto;
    String id;
    private MancalaGameRepositoryServiceImpl mancalaGameRepositoryService;
    @Mock
    private GameRepository gameRepository;
    @Autowired
    private ModelMapper modelMapper;
    private MancalaGame mancalaGame;
    private GameResponseDto gameResponseDto;

    @BeforeEach
    void init() {
        mancalaGameRepositoryService = new MancalaGameRepositoryServiceImpl(gameRepository, modelMapper);
        gameRequestDto = new CreateGameRequestDto(new Player("1", "PlayerA"), new Player("2", "PlayerB"));
        mancalaGame = new MancalaGame(new Player("1", "PlayerA"), new Player("2", "PlayerB"));
        mancalaGame.setId("12");
        gameResponseDto = (modelMapper.map(mancalaGame, GameResponseDto.class));
        id = "12";
    }

    @Test
    @DisplayName("Create succesfull game")
    void createNewGame() {
        when(gameRepository.save(any(MancalaGame.class))).thenReturn(mancalaGame);
        GameResponseDto result = mancalaGameRepositoryService.createNewGame(gameRequestDto);
        assertThat(result).isNotNull();
        verify(gameRepository, times(1)).save(any(MancalaGame.class));

    }

    @Test
    @DisplayName("Find game with valid id")
    void findGame() {
        when(gameRepository.findById(any(String.class))).thenReturn(Optional.of(mancalaGame));
        GameResponseDto result = mancalaGameRepositoryService.findGame(id);
        assertThat(result.getId()).isEqualTo("12");
        verify(gameRepository, times(1)).findById(any(String.class));
    }


    @Test
    @DisplayName("Update game with Turn")
    void updateGame() {
        when(gameRepository.save(any(MancalaGame.class))).thenReturn(mancalaGame);
        mancalaGame.setTurn(Turn.PlayerB);
        GameResponseDto result = mancalaGameRepositoryService.updateGame(mancalaGame);
        assertThat(result.getTurn()).isEqualTo(Turn.PlayerB);
        verify(gameRepository, times(1)).save(any(MancalaGame.class));

    }
}