package com.bol.mancala.mancalagame.model;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Pit {
    private int id;
    private int stoneCount;

    public Pit(int id, int stoneCount) {
        this.id = id;
        if (isHouse())
            this.stoneCount = MancalaConst.EMPTY_STONE;
        else
            this.stoneCount = stoneCount;

    }

    @JsonIgnore
    public boolean isHouse() {
        return (this.getId() == MancalaConst.PLAYER_B_PIT_HOUSE)
                || (this.getId() == MancalaConst.PLAYER_A_PIT_HOUSE);
    }

    @JsonIgnore
    public Turn getCurrentTurn() {
        if (this.getId() <= MancalaConst.PLAYER_A_PIT_HOUSE)
            return Turn.PlayerA;
        return Turn.PlayerB;
    }
}
