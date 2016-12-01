package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

public class MarketPlaceCommand extends Command
{
    private CommandType commandType;
    private String productName;
    private float amount;

    public enum CommandType
    {
        //register,
        logout,
        inspect,
        sell,
        buy,
        wish,
        getwishes,
        history,
        help,
        exit,
        ls
    }

    public MarketPlaceCommand(CommandType commandType, String productName, float amount)
    {
        this.commandType = commandType;
        this.productName = productName;
        this.amount = amount;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }

    public String getProductName()
    {
        return productName;
    }

    public float getAmount()
    {
        return amount;
    }

    @Override
    public String toString()
    {
        return "MarketPlaceCommand{" +
                "commandType=" + commandType +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                '}';
    }
}
