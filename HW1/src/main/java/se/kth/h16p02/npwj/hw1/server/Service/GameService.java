package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;

import java.io.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameService
{
    private int getNewGameId()
    {
        int maxId = -1;

        for (Game g : AppData.games)
        {
            if (g.getId() > maxId)
                maxId = g.getId();
        }

        return maxId + 1;
    }

    private String getRandomWord()
    {
        // Adapted from: http://stackoverflow.com/a/2218067
        // When we read the first line it has a 100% chance of being chosen as the result.
        // When we read the 2nd line it has a 50% chance of replacing the first line as the result.
        // When we read the 3rd line it has a 33% chance of becoming the result.
        // The fourth line has a 25% chance of becoming the result
        // etc.

        try
        {
            File file = new File(getClass().getResource("words.txt").getFile());
            BufferedReader br = new BufferedReader(new FileReader(file));

            String chosenLine = null;
            String readLine;
            int n = 0;
            while ((readLine = br.readLine()) != null)
            {
                n++;

                // nextInt(bound) returns a pseudorandom int value between zero (inclusive)
                // and the specified bound (exclusive).
                // We use a random number generator isolated to the current thread.
                if (ThreadLocalRandom.current().nextInt(n) == 0)
                    chosenLine = readLine;
            }
            System.out.println("Random word: " + chosenLine);

            return chosenLine;
        }
        catch (IOException ex)
        {
            System.err.println("GameService.getRandomWord() - error: " + ex.toString());
            return "Placeholder";
        }
    }

    public Game getGame(int gameId) throws GameNotFoundException, IllegalStateException
    {
        List<Game> foundGames =
                AppData.games
                        .stream()
                        .filter(game -> game.getId() == gameId)
                        .collect(Collectors.toList());

        if (foundGames.size() == 0)
            throw new GameNotFoundException();
        else if (foundGames.size() > 1)
            throw new IllegalStateException("More than one game found with ID: " + gameId);
        else
            return foundGames.get(0);
    }

    public Game addGame(Player player)
    {
        Game newGame = new Game(getNewGameId(), player, getRandomWord());
        AppData.games.add(newGame);
        return newGame;
    }

    public Game endGame(int gameId) throws GameNotFoundException, IllegalStateException
    {
        Game game = getGame(gameId);
        game.cancelGame();
        return game;
    }

    public Game addGuessToGame(int gameId, String guess) throws GameNotFoundException, IllegalStateException
    {
        Game game = getGame(gameId);

        Game.GameState oldGameState = game.getGameState();
        game.addGuess(guess);
        Game.GameState newGameState = game.getGameState();

        if (oldGameState == Game.GameState.InProgress)
        {
            if (newGameState == Game.GameState.Won)
                game.getPlayer().incrementScore();
            else if (newGameState == Game.GameState.Lost)
                game.getPlayer().decrementScore();
        }

        return game;
    }
}
