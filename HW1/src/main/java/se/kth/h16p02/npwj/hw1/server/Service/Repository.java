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

    public static synchronized void addPlayer(Player player)
    {
        players.add(player);
    }

    public static ArrayList<Player> getPlayers()
    {
        return players;
    }

    public static synchronized int getNewPlayerId()
    {
        int maxId = -1;

        for (Player p : players)
        {
            if (p.getId() > maxId)
                maxId = p.getId();
        }

        return maxId + 1;
    }

    public static synchronized void addGame(Game game)
    {
        games.add(game);
    }

    public static ArrayList<Game> getGames()
    {
        return games;
    }

    public static synchronized int getNewGameId()
    {
        int maxId = -1;

        for (Game g : games)
        {
            if (g.getId() > maxId)
                maxId = g.getId();
        }

        return maxId + 1;
    }
}
