package com.bol.mancala.mancalagame.dto;

import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameRequestDto {
    @NotNull
    private Player playerA;
    @NotNull
    private Player playerB;
    @NotNull
    private Turn turn;
    @DecimalMin(value = "1", message = "Pit id must be greater than 0")
    @DecimalMax(value = "14", message = "Pit id must be less or equal to 14")
    private int pitId;
    private String gameId;
}
