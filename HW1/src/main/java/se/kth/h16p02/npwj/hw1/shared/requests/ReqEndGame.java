package main.java.se.kth.h16p02.npwj.hw1.shared.requests;

public class ReqEndGame extends Request
{
    public ReqEndGame(String gameId)
    {
        this.requestType = RequestType.EndGame;
        this.gameId = gameId;
    }

    private String gameId;

    public String getGameId()
    {
        return gameId;
    }

    @Override
    public String toString()
    {
        return "ReqEndGame{" +
                "gameId='" + gameId + '\'' +
                '}';
    }
}
