package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.dto.PlayGameRequestDto;

public interface GameEngineService {
    GameResponseDto play(PlayGameRequestDto gameRequestDto);
}
