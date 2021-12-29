package com.bol.mancala.mancalagame.model;

import com.bol.mancala.mancalagame.constant.MancalaConst;
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
public class GameBoard {

    private List<Pit> pits;
    private HashMap<Turn, Integer> scoreBoard;

    public Pit getPit(int pitId) {
        return pits.get((pitId - 1) % MancalaConst.LAST_PIT);
    }

    public void setPit(Pit pit, int id) {
        pits.set((id - 1) % MancalaConst.LAST_PIT, pit);
    }

    public HashMap getAllScoreBoard() {
        //PlayerA score
        scoreBoard.put(Turn.PlayerA, getPits().stream()
                .filter(pit -> pit.getId() < MancalaConst.PLAYER_A_PIT_HOUSE)
                .mapToInt(Pit::getStoneCount).sum());
        //PlayerB score
        scoreBoard.put(Turn.PlayerB, getPits().stream()
                .filter(pit -> pit.getId() < MancalaConst.PLAYER_B_PIT_HOUSE)
                .filter(pit -> pit.getId() > MancalaConst.PLAYER_A_PIT_HOUSE)
                .mapToInt(Pit::getStoneCount).sum());

        return scoreBoard;
    }
}
