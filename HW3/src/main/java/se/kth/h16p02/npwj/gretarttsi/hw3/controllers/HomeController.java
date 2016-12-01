package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw3.traders.TraderClient;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class HomeController extends Controller
{
    private static final String LOGOUT_SUCCESS = "User successfully logout";

    public HomeController(Trader user, Bank bank, MarketPlace marketPlace)
    {
        super(user, bank, marketPlace);
    }

    @Override
    public void printConsolePrompt()
    {
        System.out.print(this.username + "@Home" + ">");
    }

    @Override
    public void run()
    {
        while (true && TraderClient.goToLogin != true)
        {
            printConsolePrompt();
            try
            {
                String userInput = consoleIn.readLine();
                execute(parse(userInput));
            }
            catch(ConnectException ex)
            {
                System.err.println(ex);
                System.err.println("Something went wrong, we lost connection to the marketplace. You will be logged out");
                System.out.print(username + "@" + "login" + ">");
                TraderClient.goToLogin = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void execute(Command command) throws RemoteException
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

            case logout:
                if (marketPlace.logOut(user))
                {
                    System.out.println(LOGOUT_SUCCESS);
                    TraderClient.goToLogin = true;
                    return;
                }
                else
                {
                    return;
                }

            case marketplace:
                new MarketPlaceController(user, bank, marketPlace).run();
                return;

            case bank:
                new BankController(user, bank, marketPlace).run();
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
