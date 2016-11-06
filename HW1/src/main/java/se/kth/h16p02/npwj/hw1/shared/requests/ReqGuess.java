package main.java.se.kth.h16p02.npwj.hw1.shared.requests;

public class ReqGuess extends Request
{
    public ReqGuess(String gameId, String guess)
    {
        this.requestType = RequestType.Guess;
        this.gameId = gameId;
        this.guess = guess;
    }

    public String guess;
    public String gameId;

    @Override
    public String toString()
    {
        return "ReqGuess{" +
                "guess='" + guess + '\'' +
                ", gameId='" + gameId + '\'' +
                '}';
    }
}
