package com.bol.mancala.mancalagame.dto;

import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Turn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameBoardDto {
    private List<Pit> pits;
    private HashMap<Turn, Integer> scoreBoard;
}
