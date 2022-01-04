package com.bol.mancala.mancalagame.model;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pit {
    private int id;
    private int stoneCount;

    public Pit(int id, int stoneCount) {
        this.id = id;
        if (isBigPit())
            this.stoneCount = MancalaConst.EMPTY_STONE;
        else
            this.stoneCount = stoneCount;

    }

    @JsonIgnore
    public boolean isBigPit() {
        return (this.getId() == MancalaConst.PLAYER_B_PIT_BIG_PIT)
                || (this.getId() == MancalaConst.PLAYER_A_PIT_BIG_PIT);
    }

    @JsonIgnore
    public Turn getCurrentTurn() {
        if (this.getId() <= MancalaConst.PLAYER_A_PIT_BIG_PIT)
            return Turn.PlayerA;
        return Turn.PlayerB;
    }
}
