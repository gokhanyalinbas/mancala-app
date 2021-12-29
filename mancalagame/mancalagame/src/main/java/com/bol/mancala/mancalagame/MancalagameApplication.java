package com.bol.mancala.mancalagame;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MancalagameApplication {

    public static void main(String[] args) {
        SpringApplication.run(MancalagameApplication.class, args);
    }

}
