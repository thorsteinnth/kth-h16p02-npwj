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

    public static void addPlayer(Player player)
    {
        players.add(player);
    }

    public static ArrayList<Player> getPlayers()
    {
        return players;
    }

    public static void addGame(Game game)
    {
        games.add(game);
    }

    public static ArrayList<Game> getGames()
    {
        return games;
    }
}
