package com.bol.mancala.mancalagame.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PlayGameRequestDto {
    @DecimalMin(value = "1", message = "Pit id must be greater than 0")
    @DecimalMax(value = "14", message = "Pit id must be less or equal to 14")
    private int pitId;
    @NotNull
    @NotBlank
    private String gameId;
}
