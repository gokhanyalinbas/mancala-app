package com.bol.mancala.mancalagame.dto;

import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GameResponseDto {

    private String id;
    private GameBoardDto gameBoard;
    private PlayerDto playerA;
    private PlayerDto playerB;
    private Turn turn;
    private Player winner;
}
