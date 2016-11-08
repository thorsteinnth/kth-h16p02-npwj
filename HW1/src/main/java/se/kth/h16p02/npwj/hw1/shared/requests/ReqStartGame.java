package main.java.se.kth.h16p02.npwj.hw1.shared.requests;

public class ReqStartGame extends Request
{
    public ReqStartGame(String playerId)
    {
        this.requestType = RequestType.StartGame;
        this.playerId = playerId;
    }

    private String playerId;

    public String getPlayerId()
    {
        return playerId;
    }

    @Override
    public String toString()
    {
        return "ReqStartGame{" +
                "playerId='" + playerId + '\'' +
                '}';
    }
}
