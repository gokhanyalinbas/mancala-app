package com.bol.mancala.mancalagame.dto;

import com.bol.mancala.mancalagame.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameRequestDto {
    @NotNull
    private Player playerA;
    @NotNull
    private Player playerB;
}
