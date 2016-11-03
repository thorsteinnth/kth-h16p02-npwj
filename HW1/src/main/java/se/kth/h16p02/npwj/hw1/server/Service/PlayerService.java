package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;

public class PlayerService
{
    private int getNewPlayerId()
    {
        int maxId = -1;

        for (Player p : AppData.players)
        {
            if (p.getId() > maxId)
                maxId = p.getId();
        }

        return maxId + 1;
    }

    public Player addPlayer()
    {
        Player newPlayer = new Player(getNewPlayerId());
        AppData.players.add(newPlayer);
        return newPlayer;
    }
}
