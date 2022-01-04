package com.bol.mancala.mancalagame.controller;

import com.bol.mancala.mancalagame.MancalagameApplication;
import com.bol.mancala.mancalagame.constant.MancalaConst;
import com.bol.mancala.mancalagame.dto.CreateGameRequestDto;
import com.bol.mancala.mancalagame.dto.GameResponseDto;
import com.bol.mancala.mancalagame.dto.PlayGameRequestDto;
import com.bol.mancala.mancalagame.dto.WebUserDto;
import com.bol.mancala.mancalagame.model.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = MancalagameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IntegrationTest {

    private final String host = "http://localhost:";
    String token = "";
    @Value("${login.username}")
    String user;
    @Value("${login.password}")
    String password;
    String gameId = null;
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setToken() throws JSONException {
        WebUserDto webUserDto = new WebUserDto(user, password);
        ResponseEntity<String> responseEntity = this.restTemplate
                .postForEntity(host + port + "/login", webUserDto, String.class);
        token = "Bearer " + new JSONObject(responseEntity.getBody()).getString("token");
    }

    @Test
    void createGame() {
        CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto(new Player("1", "Gokhan"), new Player("2", "Hulya"));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<CreateGameRequestDto> request = new HttpEntity<>(createGameRequestDto, headers);
        ResponseEntity<GameResponseDto> responseEntity = this.restTemplate
                .postForEntity(host + port + "/" + MancalaConst.REQUEST_MAPPING + MancalaConst.CREATE, request, GameResponseDto.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
        gameId = responseEntity.getBody().getId();
    }

    @Test
    void resumeGame() {
        if (gameId == null)
            createGame();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<GameResponseDto> responseEntity = this.restTemplate
                .exchange(host + port + "/" + MancalaConst.REQUEST_MAPPING + MancalaConst.RESUME, HttpMethod.GET, request, GameResponseDto.class, gameId);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    void playGame() {
        if (gameId == null)
            createGame();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        PlayGameRequestDto playGameRequestDto = new PlayGameRequestDto(1, gameId);
        HttpEntity<PlayGameRequestDto> request = new HttpEntity(playGameRequestDto, headers);
        ResponseEntity<GameResponseDto> responseEntity = this.restTemplate
                .exchange(host + port + "/" + MancalaConst.REQUEST_MAPPING + MancalaConst.PLAY, HttpMethod.PUT, request, GameResponseDto.class);
        assertEquals(202, responseEntity.getStatusCodeValue());
    }
}