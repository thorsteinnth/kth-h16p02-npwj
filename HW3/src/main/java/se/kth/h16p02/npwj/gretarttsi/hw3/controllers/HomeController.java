package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class HomeController extends Controller
{
    private static final String HOME = "Home";

    private String userName;

    enum Command
    {
        bank,
        marketplace,
        home,
        help,
        ls
    }

    public HomeController(Trader user)
    {
        super(user);

        try
        {
            this.userName = user.getUsername();
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
            this.userName = "UnknownUser";
        }
    }

    @Override
    public void printConsolePrompt()
    {
        System.out.print(this.userName + "@" + HOME + ">");
    }

    @Override
    public void run()
    {
        while (true)
        {
            printConsolePrompt();
            try
            {
                String userInput = consoleIn.readLine();
                execute(parse(userInput));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void execute(Command command)
    {
        if (command == null)
        {
            return;
        }

        switch (command)
        {
            case ls:
            case help:
                for (Command availableCommand : Command.values()) {
                    System.out.println(availableCommand);
                }
                return;

            case marketplace:
                System.out.println("Should run marketplace");
                return;

            case bank:
                System.out.println("Should run bank");
                return;

        }
    }

    private Command parse(String userInput)
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

        Command command = null;
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens())
        {
            switch (userInputTokenNo)
            {
                case 1:
                    try
                    {
                        String commandString = tokenizer.nextToken();
                        command = Command.valueOf(Command.class, commandString);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;

                default:
                    System.out.println("Illegal command");
                    return null;
            }
            userInputTokenNo++;
        }

        return command;
    }
}
