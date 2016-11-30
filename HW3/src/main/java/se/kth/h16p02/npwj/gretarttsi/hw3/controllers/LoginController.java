package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.IOException;
import java.util.StringTokenizer;

public class LoginController extends Controller
{
    public LoginController(Trader user)
    {
        super(user);
    }

    @Override
    protected void printConsolePrompt()
    {
        System.out.print("Login>");
    }

    @Override
    public void run()
    {
        boolean run = true;

        while (run)
        {
            printConsolePrompt();

            try
            {
                String userInput = consoleIn.readLine();
                run = execute(parse(userInput));
            }
            catch (IOException ex)
            {
                System.err.println(ex);
            }
        }
    }

    @Override
    protected Command parse(String userInput)
    {
        if (userInput == null)
        {
            return null;
        }

        StringTokenizer tokenizer = new StringTokenizer(userInput);
        if (tokenizer.countTokens() == 0)
        {
            return null;
        }

        LoginCommand.CommandType commandType = null;
        String username = "";
        String password = "";
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens())
        {
            switch (userInputTokenNo)
            {
                case 1:
                    try
                    {
                        String commandNameString = tokenizer.nextToken();
                        commandType = LoginCommand.CommandType.valueOf(
                                LoginCommand.CommandType.class, commandNameString);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;

                case 2:
                    username = tokenizer.nextToken();
                    break;

                case 3:
                    password = tokenizer.nextToken();
                    break;

                default:
                    System.out.println("Illegal command");
                    return null;
            }
            userInputTokenNo++;
        }

        return new LoginCommand(commandType, username, password);
    }

    private boolean execute(Command command)
    {
        if (command == null)
        {
            return true;
        }

        LoginCommand loginCommand = (LoginCommand)command;

        // Commands that take no arguments

        switch (loginCommand.getCommandType())
        {
            case quit:
                System.out.println("Closing program...");
                System.exit(0);
                return false;

            case ls:
            case help:
                for (LoginCommand.CommandType availableCommandType : LoginCommand.CommandType.values())
                {
                    System.out.println(availableCommandType);
                }
                return true;
        }

        // Commands that take two arguments, username and password

        if (loginCommand.getUsername() == null || loginCommand.getUsername() == "")
        {
            System.out.println("Username is not specified");
            return true;
        }

        if (loginCommand.getPassword() == null || loginCommand.getPassword() == "")
        {
            System.out.println("Password is not specified");
            return true;
        }

        switch (loginCommand.getCommandType())
        {
            case login:
                System.out.println("Should login with username and password: " + loginCommand.getUsername() + " " + loginCommand.getPassword());
                return true;
            case register:
                System.out.println("Should register with username and password: " + loginCommand.getUsername() + " " + loginCommand.getPassword());
                return true;
        }

        return true;
    }
}
