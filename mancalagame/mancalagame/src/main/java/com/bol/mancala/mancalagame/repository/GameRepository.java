package com.bol.mancala.mancalagame.repository;

import com.bol.mancala.mancalagame.entity.MancalaGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends MongoRepository<MancalaGame, String> {
    //All repository functions
}
