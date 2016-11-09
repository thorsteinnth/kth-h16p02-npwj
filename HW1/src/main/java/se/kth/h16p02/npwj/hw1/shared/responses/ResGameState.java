package main.java.se.kth.h16p02.npwj.hw1.shared.responses;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;

import java.util.ArrayList;

public class ResGameState extends Response
{
    public ResGameState(Game game)
    {
        this.responseType = ResponseType.GameState;

        this.playerId = Integer.toString(game.getPlayer().getId());
        this.playerScore = Integer.toString(game.getPlayer().getScore());
        this.gameId = Integer.toString(game.getId());
        this.numberOfGuessesLeft = game.getAttemptsLeft();
        this.gameState = game.getGameState();
        this.gameStateString = game.getDisplayString();
        this.guessedCharacters = game.getGuessedCharacters();
    }

    private String playerId;
    private String playerScore;
    private String gameId;
    private int numberOfGuessesLeft;
    private String gameStateString;
    private Game.GameState gameState;
    private ArrayList<Character> guessedCharacters;

    public String getPlayerId()
    {
        return playerId;
    }

    public String getPlayerScore()
    {
        return playerScore;
    }

    public String getGameId()
    {
        return gameId;
    }

    public int getNumberOfGuessesLeft()
    {
        return numberOfGuessesLeft;
    }

    public String getGameStateString()
    {
        return gameStateString;
    }

    public Game.GameState getGameState()
    {
        return gameState;
    }

    public ArrayList<Character> getGuessedCharacters()
    {
        return guessedCharacters;
    }

    @Override
    public String toString()
    {
        return "ResGameState{" +
                "playerId='" + playerId + '\'' +
                ", playerScore='" + playerScore + '\'' +
                ", gameId='" + gameId + '\'' +
                ", numberOfGuessesLeft=" + numberOfGuessesLeft +
                ", gameStateString='" + gameStateString + '\'' +
                ", gameState=" + gameState + '\'' +
                ", guessedCharacters=" + guessedCharacters  +
                '}';
    }
}
