package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.entities.Account;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class BankController extends Controller
{
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds";

    private String bankname;

    public BankController(Trader user)
    {
        super(user);
        this.bankname = DEFAULT_BANK_NAME;
    }

    @Override
    protected void printConsolePrompt()
    {
        System.out.print(username + "@" + bankname + ">");
    }

    @Override
    protected void run()
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
            catch (RejectedException re)
            {
                System.out.println(re);
            }
            catch (InsufficientFundsException ex)
            {
                System.out.println(INSUFFICIENT_FUNDS);
            }
            catch (IOException e)
            {
                e.printStackTrace();
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
        float amount = 0;
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
                        amount = Float.parseFloat(tokenizer.nextToken());
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

    protected boolean execute(Command command) throws RemoteException, RejectedException, InsufficientFundsException
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

            case exit:
                //new HomeController(this).run();
                System.out.println("Should go home");
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

        switch (bankCommand.getCommandType())
        {
            case newaccount:
                //bankobj.newAccount(username);
                System.out.println("Should create new account");
                return true;
            case deleteaccount:
                //bankobj.deleteAccount(username);
                System.out.println("Should delete account");
                return true;
        }

        // All further commands require an account reference

        // TODO Get account
        /*
        Account acc = bankobj.findAccount(username);
        if (acc == null)
        {
            System.out.println("No account for " + username);
            return true;
        }*/

        switch (bankCommand.getCommandType())
        {
            case getaccount:
                //System.out.println(acc);
                System.out.println("Should print account");
                break;

            case deposit:
                //bankobj.deposit(username,command.getAmount());
                System.out.println("Should deposit");
                break;

            case withdraw:
                //bankobj.withdraw(username,command.getAmount());
                System.out.println("Should withdraw");
                break;

            case balance:
                //System.out.println("balance: $" + acc.getBalance());
                System.out.println("Should print balance");
                break;

            default:
                System.out.println("Illegal command");
        }

        return true;
    }
}
