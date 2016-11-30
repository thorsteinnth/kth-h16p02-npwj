package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.IOException;
import java.util.StringTokenizer;

public class HomeController extends Controller
{
    private static final String HOME = "Home";

    public HomeController(Trader user)
    {
        super(user);
    }

    @Override
    public void printConsolePrompt()
    {
        System.out.print(this.username + "@" + HOME + ">");
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
        HomeCommand homeCommand = (HomeCommand)command;

        if (command == null)
        {
            return;
        }

        switch (homeCommand.getCommandType())
        {
            case ls:
            case help:
                for (HomeCommand.CommandType availableCommand : HomeCommand.CommandType.values())
                {
                    System.out.println(availableCommand);
                }
                return;

            case marketplace:
                new MarketPlaceController(user).run();
                return;

            case bank:
                new BankController(user).run();
                return;

        }
    }

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

        HomeCommand command = null;
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens())
        {
            switch (userInputTokenNo)
            {
                case 1:
                    try
                    {
                        String commandString = tokenizer.nextToken();
                        command = new HomeCommand(
                                HomeCommand.CommandType.valueOf(
                                        HomeCommand.CommandType.class,
                                        commandString)
                        );
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
