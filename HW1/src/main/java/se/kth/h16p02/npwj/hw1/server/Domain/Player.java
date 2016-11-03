package main.java.se.kth.h16p02.npwj.hw1.server.Domain;

public class Player
{
    private int id;

    public Player(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "Player{" +
                "id=" + id +
                '}';
    }
}
