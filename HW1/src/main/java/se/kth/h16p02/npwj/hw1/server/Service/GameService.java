package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;

import java.util.List;
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
        return "Gretar";
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
