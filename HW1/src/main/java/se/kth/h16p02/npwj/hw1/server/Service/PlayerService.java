package main.java.se.kth.h16p02.npwj.hw1.server.Service;

import main.java.se.kth.h16p02.npwj.hw1.server.Domain.Player;

import java.util.List;
import java.util.stream.Collectors;

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

    public Player getPlayer(int playerId) throws PlayerNotFoundException, IllegalStateException
    {
        List<Player> foundPlayers =
                AppData.players
                        .stream()
                        .filter(player -> player.getId() == playerId)
                        .collect(Collectors.toList());

        if (foundPlayers.size() == 0)
            throw new PlayerNotFoundException();
        else if (foundPlayers.size() > 1)
            throw new IllegalStateException("More than one player found with ID: " + playerId);
        else
            return foundPlayers.get(0);
    }
}
