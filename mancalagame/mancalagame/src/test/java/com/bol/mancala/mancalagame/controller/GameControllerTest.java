package com.bol.mancala.mancalagame.controller;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.dto.*;
import com.bol.mancala.mancalagame.exception.MancalaGameNotFoundException;
import com.bol.mancala.mancalagame.exception.UnexpectedMoveException;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import com.bol.mancala.mancalagame.service.GameEngineServiceImpl;
import com.bol.mancala.mancalagame.service.MancalaGameRepositoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({GameController.class})
class GameControllerTest {
    @MockBean
    GameEngineServiceImpl gameEngineService;
    @MockBean
    MancalaGameRepositoryServiceImpl mancalaGameRepositoryService;
    CreateGameRequestDto gameRequestDto;
    GameResponseDto gameResponseDto;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        gameRequestDto = new CreateGameRequestDto(new Player("1", "PlayerA"), new Player("2", "PlayerB"));
        gameResponseDto = GameResponseDto.builder()
                .turn(Turn.PlayerA)
                .id("12")
                .gameBoard(new GameBoardDto(IntStream.range(MancalaConst.FIRST_PIT, MancalaConst.LAST_PIT + 1)
                        .mapToObj(i -> new Pit(i, MancalaConst.DEFAULT_STONE_COUNT))
                        .collect(Collectors.toList()), new HashMap<>()))
                .playerA(new PlayerDto("1", "PlayerA"))
                .playerB(new PlayerDto("2", "PlayerB"))
                .build();

    }

    @Test
    @DisplayName("Create game successfully")
    @WithMockUser("admin")
    void createGame() throws Exception {

        when(mancalaGameRepositoryService.createNewGame(any(CreateGameRequestDto.class))).thenReturn(gameResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.CREATE)
                                .content(new ObjectMapper().writeValueAsString(gameRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(gameResponseDto)));

    }

    @Test
    @DisplayName("Create game with invalid arguments")
    @WithMockUser("admin")
    void createGameWithInvalidArgs() throws Exception {

        CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.CREATE)
                                .content(new ObjectMapper().writeValueAsString(createGameRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").exists());

    }

    @Test
    @DisplayName("Resume game with valid id")
    @WithMockUser("admin")
    void resumeGame() throws Exception {

        when(mancalaGameRepositoryService.findGame(any(String.class))).thenReturn(gameResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.RESUME, "12")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(gameResponseDto)));

    }

    @Test
    @DisplayName("Resume game with invalid id")
    @WithMockUser("admin")
    void resumeGameWithInvalidId() throws Exception {

        when(mancalaGameRepositoryService.findGame(any(String.class))).thenThrow(new MancalaGameNotFoundException("not found"));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.RESUME, "12")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").exists())
                .andExpect(jsonPath("$.reason").value("not found"));
    }

    @Test
    @DisplayName("Resume game with invalid user")
    void resumeGameWithInvalidUser() throws Exception {

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.RESUME, "12")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Play game with valid arguments")
    @WithMockUser("admin")
    void playGame() throws Exception {

        gameResponseDto.setTurn(Turn.PlayerB);
        when(gameEngineService.play(any(PlayGameRequestDto.class))).thenReturn(gameResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.PLAY, "12", 5)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(gameResponseDto)));

    }

    @Test
    @DisplayName("Play game with invalid arguments")
    @WithMockUser("admin")
    void playGameWithInvalidArgs() throws Exception {

        when(gameEngineService.play(any(PlayGameRequestDto.class))).thenThrow(new MancalaGameNotFoundException("not found"));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.PLAY, "12", 5)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.reason").exists())
                .andExpect(jsonPath("$.reason").value("not found"));

    }

    @Test
    @DisplayName("Play game with invalid move")
    @WithMockUser("admin")
    void playGameWithInvalidMove() throws Exception {

        when(gameEngineService.play(any(PlayGameRequestDto.class))).thenThrow(new UnexpectedMoveException("not your turn"));

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.PLAY, "12", 5)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").exists())
                .andExpect(jsonPath("$.reason").value("not your turn"));

    }

    @Test
    @DisplayName("Play game - winner")
    @WithMockUser("admin")
    void playGameGetWinner() throws Exception {

        gameResponseDto.setWinner(new Player("1", "PlayerA"));
        when(gameEngineService.play(any(PlayGameRequestDto.class))).thenReturn(gameResponseDto);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/" + MancalaConst.REQUEST_MAPPING + MancalaConst.PLAY, "12", 5)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isAccepted())
                .andExpect(content().string(new ObjectMapper().writeValueAsString(gameResponseDto)))
                .andExpect(jsonPath("$.winner").exists());

    }
}