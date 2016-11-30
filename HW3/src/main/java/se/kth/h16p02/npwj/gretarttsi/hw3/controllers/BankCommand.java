package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

public class BankCommand extends Command
{
    private CommandType commandType;
    private String username;
    private float amount;

    public enum CommandType
    {
        newaccount,
        getaccount,
        deleteaccount,
        deposit,
        withdraw,
        balance,
        exit,
        help,
        list,
        ls
    };

    public BankCommand(CommandType commandType, String username, float amount)
    {
        this.commandType = commandType;
        this.username = username;
        this.amount = amount;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }

    public String getUsername()
    {
        return username;
    }

    public float getAmount()
    {
        return amount;
    }

    @Override
    public String toString()
    {
        return "BankCommand{" +
                "commandType=" + commandType +
                ", username='" + username + '\'' +
                ", amount=" + amount +
                '}';
    }
}
