package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;

import java.util.ArrayList;

public class Repository
{
    private static ArrayList<Player> players;
    private static ArrayList<Game> games;

    public Repository()
    {
        players = new ArrayList<>();
        games = new ArrayList<>();
    }

    //region Players

    public static synchronized Player addPlayer()
    {
        Player newPlayer = new Player(getNewPlayerId());
        players.add(newPlayer);
        return newPlayer;
    }

    public static ArrayList<Player> getPlayers()
    {
        return players;
    }

    private static int getNewPlayerId()
    {
        int maxId = -1;

        for (Player p : players)
        {
            if (p.getId() > maxId)
                maxId = p.getId();
        }

        return maxId + 1;
    }

    //endregion

    //region Games

    public static synchronized Game addGame(Player player, String word)
    {
        Game newGame = new Game(getNewGameId(), player, word);
        games.add(newGame);
        return newGame;
    }

    public static ArrayList<Game> getGames()
    {
        return games;
    }

    private static int getNewGameId()
    {
        int maxId = -1;

        for (Game g : games)
        {
            if (g.getId() > maxId)
                maxId = g.getId();
        }

        return maxId + 1;
    }

    //endregion
}
