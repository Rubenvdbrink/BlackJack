package nl.hu.bep2.casino.blackjack.domain.enums;

public enum GameState {
    STARTOFGAME, //0
    PLAYERHIT, //1
    PLAYERSTAND, //2
    PLAYERSURRENDER, //3
    PLAYERDOUBLE, //4
    PLAYERBLACKJACK, //5
    PLAYERWIN, //6
    PLAYERLOSE; //7
}
