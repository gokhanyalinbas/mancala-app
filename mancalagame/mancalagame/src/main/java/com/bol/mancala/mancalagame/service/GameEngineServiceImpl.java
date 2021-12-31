package com.bol.mancala.mancalagame.service;

import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.dto.PlayGameRequestDto;
import com.bol.mancala.mancalagame.entity.MancalaGame;
import com.bol.mancala.mancalagame.model.Pit;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static com.bol.mancala.mancalagame.util.GameEngineUtil.applyRules;
import static com.bol.mancala.mancalagame.util.GameEngineUtil.distributeAllStones;

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

        MancalaGame game = modelMapper.map(mancalaGameService.findGame(gameRequestDto.getGameId()), MancalaGame.class);
        Pit currentPit = game.getGameBoard().getPit(gameRequestDto.getPitId());
        game = sow(game, currentPit);
        return mancalaGameService.updateGame(game);

    }

    private MancalaGame sow(MancalaGame game, Pit currentPit) {
        applyRules(game, currentPit);
        return distributeAllStones(game, currentPit);
    }


}
