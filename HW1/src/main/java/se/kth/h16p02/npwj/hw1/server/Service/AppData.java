package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Game;
import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;

import java.util.ArrayList;

public class AppData
{
    public static ArrayList<Player> players;
    public static ArrayList<Game> games;

    public AppData()
    {
        players = new ArrayList<>();
        games = new ArrayList<>();
    }
}
