package main.java.se.kth.h16p02.npwj.hw1.server.Domain;

import java.util.ArrayList;

public class Game
{
    private int id;
    private Player player;
    private String word;
    private int attemptsLeft;
    private ArrayList<Character> guessedCharacters;

    public Game(int id, Player player, String word)
    {
        this.id = id;
        this.player = player;
        this.word = word;
        this.attemptsLeft = 10;
        this.guessedCharacters = new ArrayList<>();
    }

    public int getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", player=" + player +
                ", word='" + word + '\'' +
                ", attemptsLeft=" + attemptsLeft +
                ", guessedCharacters=" + guessedCharacters +
                '}';
    }
}
