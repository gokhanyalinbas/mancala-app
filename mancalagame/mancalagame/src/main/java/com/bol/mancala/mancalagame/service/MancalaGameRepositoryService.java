package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.dto.CreateGameRequestDto;
import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.entity.MancalaGame;

public interface MancalaGameRepositoryService {

    GameResponseDto createNewGame(CreateGameRequestDto gameRequestDto);

    GameResponseDto findGame(String gameId);

    GameResponseDto updateGame(MancalaGame mancalaGame);
}
