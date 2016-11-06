package main.java.se.kth.h16p02.npwj.hw1.shared.requests;

public class ReqCreatePlayerAndStartGame extends Request
{
    public ReqCreatePlayerAndStartGame()
    {
        this.requestType = RequestType.CreatePlayerAndStartGame;
    }

    @Override
    public String toString()
    {
        return "ReqCreatePlayerAndStartGame{}";
    }
}
