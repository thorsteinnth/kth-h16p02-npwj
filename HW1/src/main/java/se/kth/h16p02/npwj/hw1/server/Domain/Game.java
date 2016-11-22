package main.java.se.kth.h16p02.npwj.hw1.server.Domain;

import java.util.ArrayList;

public class Game
{
    private final int id;
    private final Player player;
    private int attemptsLeft;
    private final String word;
    private ArrayList<Character> guessedCharacters;
    private GameState gameState;

    public Game(int id, Player player, String word)
    {
        this.id = id;
        this.player = player;
        this.attemptsLeft = word.length() * 2;
        this.word = word;
        this.guessedCharacters = new ArrayList<>();
        this.gameState = GameState.InProgress;
    }

    public enum GameState
    {
        InProgress,
        Won,
        Lost,
        Cancelled
    }

    public int getId()
    {
        return id;
    }

    public Player getPlayer()
    {
        return player;
    }

    public int getAttemptsLeft()
    {
        return attemptsLeft;
    }

    public void cancelGame()

    {
        // Only allow cancelling games that are in progress (i.e. not finished)

        UUUUUUif (this.gameState == GameState.InProgress)
            SOÆDFIJOÆ
