package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

public class LoginCommand extends Command
{
    private CommandType commandType;
    private String username;
    private String password;

    public enum CommandType
    {
        login,
        register,
        quit,
        ls,
        help
    }

    public LoginCommand(CommandType commandType, String username, String password)
    {
        this.commandType = commandType;
        this.username = username;
        this.password = password;
    }

    public CommandType getCommandType()
    {
        return commandType;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    @Override
    public String toString()
    {
        return "LoginCommand{" +
                "commandType=" + commandType +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
