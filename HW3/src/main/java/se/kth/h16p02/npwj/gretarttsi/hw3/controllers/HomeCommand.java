package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

public class HomeCommand extends Command
{
    private CommandType commandType;

    public enum CommandType
    {
        logout,
        bank,
        marketplace,
        home,
        help,
        ls
    }

    public HomeCommand(CommandType commandType)
    {
        this.commandType = commandType;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }
}
