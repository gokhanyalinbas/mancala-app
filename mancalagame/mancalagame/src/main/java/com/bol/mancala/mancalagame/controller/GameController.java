package com.bol.mancala.mancalagame.controller;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.dto.CreateGameRequestDto;
import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.dto.PlayGameRequestDto;
import com.bol.mancala.mancalagame.service.GameEngineServiceImpl;
import com.bol.mancala.mancalagame.service.MancalaGameRepositoryServiceImpl;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(MancalaConst.REQUEST_MAPPING)
@CrossOrigin
@RequiredArgsConstructor
public class GameController {

    private final MancalaGameRepositoryServiceImpl mancalaGameService;
    private final GameEngineServiceImpl gameEngineServiceImpl;

    @PostMapping(MancalaConst.CREATE)
    @ApiOperation("Create new game for two players")
    public ResponseEntity<GameResponseDto> createGame(@Valid @RequestBody final CreateGameRequestDto createGameRequestDto) {
        return new ResponseEntity<>(mancalaGameService.createNewGame(createGameRequestDto), HttpStatus.OK);
    }

    @GetMapping(MancalaConst.RESUME)
    @ApiOperation("You can load your game with gameID")
    public ResponseEntity<GameResponseDto> resumeGame(@PathVariable final String id) {
        return new ResponseEntity<>(mancalaGameService.findGame(id), HttpStatus.OK);
    }

    @PutMapping(MancalaConst.PLAY)
    @ApiOperation("Play game with gameId and your pitId")
    public ResponseEntity<GameResponseDto> playGame(@Valid @RequestBody final PlayGameRequestDto gameRequestDto) {
        return new ResponseEntity<>(gameEngineServiceImpl.play(gameRequestDto), HttpStatus.ACCEPTED);
    }

}
