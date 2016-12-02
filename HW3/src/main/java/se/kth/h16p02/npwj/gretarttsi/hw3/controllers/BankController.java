package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model.AccountDTO;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.BankAccountNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw3.traders.TraderClient;

import java.io.IOException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class BankController extends Controller
{
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String LOGOUT_SUCCESS = "User successfully logged out";

    public BankController(Trader user, Bank bank, MarketPlace marketPlace)
    {
        super(user, bank, marketPlace);
    }

    @Override
    protected void printConsolePrompt()
    {
        System.out.print(username + "@" + DEFAULT_BANK_NAME + ">");
    }

    @Override
    protected void run()
    {
        boolean run = true;

        while (run && TraderClient.goToLogin != true)
        {
            printConsolePrompt();

            try
            {
                String userInput = consoleIn.readLine();
                run = execute(parse(userInput));
            }
            catch(ConnectException ex)
            {
                System.err.println(ex);
                System.err.println("Something went wrong, we lost connection to the marketplace. You will be logged out");
                System.out.print("Login>");
                TraderClient.goToLogin = true;
            }
            catch (RejectedException re)
            {
                System.out.println(re);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            catch (InsufficientFundsException ex)
            {
                System.out.println(INSUFFICIENT_FUNDS);
            }
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

        BankCommand.CommandType commandType = null;
        int amount = 0;
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens())
        {
            switch (userInputTokenNo)
            {
                case 1:
                    try
                    {
                        String commandNameString = tokenizer.nextToken();
                        commandType = BankCommand.CommandType.valueOf(
                                BankCommand.CommandType.class, commandNameString);
                    }
                    catch (IllegalArgumentException ex)
                    {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;

                case 2:
                    try
                    {
                        amount = Integer.parseInt(tokenizer.nextToken());
                    }
                    catch (NumberFormatException e)
                    {
                        System.out.println("Illegal amount");
                        return null;
                    }
                    break;

                default:
                    System.out.println("Illegal command");
                    return null;
            }
            userInputTokenNo++;
        }

        return new BankCommand(commandType, this.username, amount);
    }

    private boolean execute(Command command) throws RemoteException, RejectedException, InsufficientFundsException
    {
        if (command == null)
        {
            return true;
        }

        BankCommand bankCommand = (BankCommand)command;

        switch (bankCommand.getCommandType())
        {
            case list:
                try
                {
                    /*
                    for (String accountHolder : bankobj.listAccounts())
                    {
                        System.out.println(accountHolder);
                    }*/
                    System.out.println("List accounts currently not supported");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return true;
                }
                return true;

            case logout:
                if (marketPlace.logOut(user))
                {
                    System.out.println(LOGOUT_SUCCESS);
                    TraderClient.goToLogin = true;
                    return true;
                }
                else
                {
                    return true;
                }
            case exit:
                new HomeController(user, bank, marketPlace).run();
                return false;

            case ls:
            case help:
                for (BankCommand.CommandType availableCommandType : BankCommand.CommandType.values())
                {
                    System.out.println(availableCommandType);
                }
                return true;

        }

        if (bankCommand.getUsername() == null || bankCommand.getUsername() == "")
        {
            System.out.println("username is not specified");
            return true;
        }

        /*
        switch (bankCommand.getCommandType())
        {
            case newaccount:
                bank.newAccount(username);
                return true;
        }
        */

        // All further commands require an account reference

        AccountDTO acc;

        try
        {
            acc = bank.getAccount(username);

            switch (bankCommand.getCommandType())
            {
                case getaccount:
                    System.out.println(acc);
                    break;

                    /*
                case deleteaccount:
                    bank.deleteAccount(acc);
                    return true;
                */
                case deposit:
                    bank.deposit(acc, bankCommand.getAmount());
                    break;

                case withdraw:
                    bank.withdraw(acc, bankCommand.getAmount());
                    break;

                case balance:
                    System.out.println("balance: $" + acc.getBalance());
                    break;

                default:
                    System.out.println("Illegal command");
            }
        }
        catch (BankAccountNotFoundException e)
        {
            System.out.println("No account for " + username);
            return true;
        }

        return true;
    }
}
