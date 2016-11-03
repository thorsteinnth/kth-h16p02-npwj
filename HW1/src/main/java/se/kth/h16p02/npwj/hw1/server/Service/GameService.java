package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;

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

    public Game getGame(int gameId)
    {
        List<Game> foundGames =
                AppData.games
                        .stream()
                        .filter(game -> game.getId() == gameId)
                        .collect(Collectors.toList());

        if (foundGames.size() == 0)
            return null;
        else if (foundGames.size() > 1)
            throw new IllegalStateException("More than one game found with ID: " + gameId);
        else
            return foundGames.get(0);
    }

    public Game addGame(int playerId)
    {
        Game newGame = new Game(getNewGameId(), playerId, getRandomWord());
        AppData.games.add(newGame);
        return newGame;
    }
}
