package main.java.se.kth.h16p02.npwj.hw1.server.Domain;

public class Player
{
    private int id;
    private int score;

    public Player(int id)
    {
        this.id = id;
        this.score = 0;
    }

    public int getId()
    {
        return id;
    }

    public int getScore()
    {
        return score;
    }

    public void incrementScore()
    {
        this.score++;
    }

    public void decrementScore()
    {
        this.score--;
    }

    @Override
    public String toString()
    {
        return "Player{" +
                "id=" + id +
                ", score=" + score +
                '}';
    }
}
