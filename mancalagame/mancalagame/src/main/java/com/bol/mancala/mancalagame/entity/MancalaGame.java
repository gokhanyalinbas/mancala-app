package com.bol.mancala.mancalagame.entity;

import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.model.GameBoard;
import com.bol.mancala.mancalagame.model.Pit;
import com.bol.mancala.mancalagame.model.Player;
import com.bol.mancala.mancalagame.model.Turn;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Document(collection = "games")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MancalaGame {

    @Id
    private String id;
    private GameBoard gameBoard;
    private Player playerA;
    private Player playerB;
    private Turn turn;
    private Player winner;

    public MancalaGame(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;
        //Firs turn for player A
        this.turn = Turn.PlayerA;
        this.gameBoard = new GameBoard(IntStream.range(MancalaConst.FIRST_PIT, MancalaConst.LAST_PIT + 1)
                .mapToObj(i -> new Pit(i, MancalaConst.DEFAULT_STONE_COUNT))
                .collect(Collectors.toList()), new HashMap<>());
    }


}
