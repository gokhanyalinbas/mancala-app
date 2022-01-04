package com.bol.mancala.mancalagame.constant;

public class MancalaConst {

    //Board Const
    public static final int LAST_PIT = 14;
    public static final int FIRST_PIT = 1;
    public static final int PLAYER_B_PIT_BIG_PIT = 14;
    public static final int PLAYER_A_PIT_BIG_PIT = 7;
    public static final int EMPTY_STONE = 0;
    public static final int DEFAULT_STONE_COUNT = 6;

    //Controller Const
    public static final String REQUEST_MAPPING = "games";
    public static final String CREATE = "/create";
    public static final String RESUME = "/resume/{id}";
    public static final String PLAY = "/play";
    public static final String LOGIN = "login";
}

