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
        this.attemptsLeft = 10;
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

    public GameState getGameState()
    {
        return gameState;
    }

    public ArrayList<Character> getGuessedCharacters()
    {
        return guessedCharacters;
    }

    public String getDisplayString()
    {
        // Return a string of the form "_ _ X _ _ X _"
        // Where X is a character that has already been guessed

        // If the game has been lost, return the whole word
        if (this.gameState == GameState.Lost)
            return this.word;

        char[] displayStringCharacters = this.word.toCharArray();
        for (int i = 0; i < displayStringCharacters.length; i++)
        {
            if (!this.guessedCharacters.contains(displayStringCharacters[i]))
                displayStringCharacters[i] = '_';
        }

        return new String(displayStringCharacters);
    }

    private boolean wordHasBeenGuessed()
    {
        boolean hasBeenGuessed = true;

        for (Character wordChar : this.word.toCharArray())
        {
            if (!this.guessedCharacters.contains(wordChar))
                hasBeenGuessed = false;
        }

        return hasBeenGuessed;
    }

    private void updateGameState()
    {
        // Can't change game status once the game is lost or won
        // Can only change from InProgress

        if (this.gameState == GameState.InProgress)
        {
            if (wordHasBeenGuessed())
                this.gameState = GameState.Won;
            else if (this.attemptsLeft <= 0)
                this.gameState = GameState.Lost;
        }
    }

    public void addGuess(String guess)
    {
        // You can either guess one character in the word, or the entire word
        // i.e. if you guess more than one character, and it isn't the word,
        // then you get nothing

        if (this.attemptsLeft <= 0)
        {
            // There are no attempts left, can't guess
            updateGameState();
            return;
        }

        this.attemptsLeft--;

        if (guess.equals(word))
        {
            // Word correctly guessed
            // Add all word characters to the list of guessed characters
            for (char wordCharacter : word.toCharArray())
            {
                if (!this.guessedCharacters.contains(wordCharacter))
                    this.guessedCharacters.add(wordCharacter);
            }
        }
        else
        {
            // Only allow guesses for single letters
            // All other guesses are wrong

            if (guess.length() == 1)
            {
                char guessedChar = guess.charAt(0);
                if (!this.guessedCharacters.contains(guessedChar))
                    this.guessedCharacters.add(guessedChar);
            }
            else
            {
                // Guess is wrong, do nothing
            }
        }

        updateGameState();
    }

    public void cancelGame()
    {
        // Only allow cancelling games that are in progress (i.e. not finished)

        if (this.gameState == GameState.InProgress)
            this.gameState = GameState.Cancelled;
    }

    @Override
    public String toString()
    {
        return "Game{" +
                "id=" + id +
                ", player=" + player +
                ", attemptsLeft=" + attemptsLeft +
                ", word='" + word + '\'' +
                ", guessedCharacters=" + guessedCharacters +
                ", gameState=" + gameState +
                '}';
    }
}
