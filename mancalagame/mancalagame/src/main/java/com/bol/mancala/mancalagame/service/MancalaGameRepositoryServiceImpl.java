package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.dto.CreateGameRequestDto;
import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.exception.MancalaGameNotFoundException;
import com.bol.mancala.mancalagame.repository.GameRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class MancalaGameRepositoryServiceImpl implements MancalaGameRepositoryService {

    private final ModelMapper mapper;
    private final GameRepository gameRepository;

    public MancalaGameRepositoryServiceImpl(GameRepository gameRepository, ModelMapper mapper) {
        this.gameRepository = gameRepository;
        this.mapper = mapper;

    }

    @Override
    public GameResponseDto createNewGame(CreateGameRequestDto gameRequestDto) {
        MancalaGame mancalaGame = new MancalaGame(gameRequestDto.getPlayerA(), gameRequestDto.getPlayerB());
        return mapper.map(gameRepository.save(mancalaGame), GameResponseDto.class);
    }

    @Override
    @Cacheable(value = "mancalaGame", key = "#gameId", unless = "#result  == null")
    public GameResponseDto findGame(String gameId) {
        Optional<MancalaGame> mancalaGame = gameRepository.findById(gameId);
        return mapper.map(mancalaGame.orElseThrow(() -> new MancalaGameNotFoundException("Game not found :" + gameId)), GameResponseDto.class);
    }

    @Override
    @Transactional
    @CachePut(value = "mancalaGame", key = "#mancalaGame.id")
    public GameResponseDto updateGame(MancalaGame mancalaGame) {
        return mapper.map(gameRepository.save(mancalaGame), GameResponseDto.class);
    }


}
