package se.kth.h16p02.npwj.gretarttsi.hw2.traders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.StringTokenizer;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Account;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

public class TraderImpl extends UnicastRemoteObject implements Trader{

    private static final String USAGE = "java bankrmi.TraderClient <bank_url>";
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String HOME = "Home";
    private static final String MARKETPLACE = "Marketplace";
    private static final String BANK = "Bank";

    BufferedReader consoleIn;
    Account account;
    Bank bankobj;
    private String bankname;
    String clientname;

    static enum HomeCommandName{
        bank, marketPlace, home, help
    }

    static enum BankCommandName {
        newAccount, getAccount, deleteAccount, deposit, withdraw, balance, quit, help, list;
    };

    static enum marketplaceCommandName {
        register, unregister, status, inspect, sell, buy, wish, help, quit
    }

    //TODO breyta command line virkni hjá bankanum þannig að þegar ýtt er á quit þá er farið aftur á home.

    public TraderImpl(String clientName) throws RemoteException
    {
        super();

        this.clientname = clientName;
        this.bankname = DEFAULT_BANK_NAME;
        try
        {
            try
            {
                LocateRegistry.getRegistry(1099).list();
            }
            catch (RemoteException e)
            {
                LocateRegistry.createRegistry(1099);
            }
            bankobj = (Bank) Naming.lookup(bankname);
        }
        catch (Exception e)
        {
            System.out.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Connected to bank: " + bankname);
    }

    @Override
    public void wishIsAvailable(String item) {

    }

    @Override
    public void itemSold(String item) {

    }

    public void run()
    {
        this.consoleIn = new BufferedReader(new InputStreamReader(System.in));

        runHome();
    }

    private void runHome()
    {
        while (true)
        {
            System.out.print(clientname + "@" + HOME + ">");
            try
            {
                String userInput = consoleIn.readLine();
                homeExecute(homeParse(userInput));
            }
            /*
            catch (RejectedException re)
            {
                System.out.println(re);
            }
            */
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void runMarketplace()
    {

    }

    private void runBank()
    {
        boolean run = true;

        while (run)
        {
            System.out.print(clientname + "@" + bankname + ">");
            try
            {
                String userInput = consoleIn.readLine();
                run = bankExecute(bankParse(userInput));
            }
            catch (RejectedException re)
            {
                System.out.println(re);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    //region Home command line function
    private HomeCommandName homeParse(String userInput)
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

        HomeCommandName commandName = null;
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens())
        {
            switch (userInputTokenNo)
            {
                case 1:
                    try
                    {
                        String commandNameString = tokenizer.nextToken();
                        commandName = BankCommandName.valueOf(HomeCommandName.class, commandNameString);
                    }
                    catch (IllegalArgumentException commandDoesNotExist)
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
        return commandName;
    }

    private void homeExecute ( HomeCommandName commandName)
    {
        if (commandName == null)
        {
            return;
        }

        switch (commandName)
        {
            case help:
                for (HomeCommandName homeCommandName : HomeCommandName.values()) {
                    System.out.println(homeCommandName);
                }
                return;

            case marketPlace:
                System.out.println("MarketPlace not ready. Coming real soon");
                return;

            case bank:
                runBank();
                return;

        }
    }
    //endregion

    //region Marketplace command line function
    private marketplaceCommandName marketplaceParse()
    {

    }

    private boolean marketPlaceExecute()
    {
        return true;
    }
    //endregion

    //region Bank Command line function
    private BankCommand bankParse(String userInput)
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

        BankCommandName commandName = null;
        String userName = null;
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
                        commandName = BankCommandName.valueOf(BankCommandName.class, commandNameString);
                    }
                    catch (IllegalArgumentException commandDoesNotExist)
                    {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;

                case 2:
                    userName = tokenizer.nextToken();
                    break;

                case 3:
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
        return new BankCommand(commandName, userName, amount);
    }

    boolean bankExecute(BankCommand command) throws RemoteException, RejectedException
    {
        if (command == null)
        {
            return true;
        }

        switch (command.getCommandName())
        {
            case list:
                try
                {
                    for (String accountHolder : bankobj.listAccounts())
                    {
                        System.out.println(accountHolder);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return true;
                }
                return true;

            case quit:
                runHome();
                return false;

            case help:
                for (BankCommandName commandName : BankCommandName.values()) {
                    System.out.println(commandName);
                }
                return true;

        }

        // all further commands require a name to be specified
        String userName = command.getUserName();
        if (userName == null) {
            userName = clientname;
        }

        if (userName == null) {
            System.out.println("name is not specified");
            return true;
        }

        switch (command.getCommandName()) {
            case newAccount:
                clientname = userName;
                bankobj.newAccount(userName);
                return true;
            case deleteAccount:
                clientname = userName;
                bankobj.deleteAccount(userName);
                return true;
        }

        // all further commands require a Account reference
        Account acc = bankobj.getAccount(userName);
        if (acc == null) {
            System.out.println("No account for " + userName);
            return true;
        } else {
            account = acc;
            clientname = userName;
        }

        switch (command.getCommandName()) {
            case getAccount:
                System.out.println(account);
                break;
            case deposit:
                account.deposit(command.getAmount());
                break;
            case withdraw:
                account.withdraw(command.getAmount());
                break;
            case balance:
                System.out.println("balance: $" + account.getBalance());
                break;
            default:
                System.out.println("Illegal command");
        }
        return true;
    }

    private class BankCommand {
        private String userName;
        private float amount;
        private BankCommandName commandName;

        private String getUserName() {
            return userName;
        }

        private float getAmount() {
            return amount;
        }

        private BankCommandName getCommandName() {
            return commandName;
        }

        private BankCommand(BankCommandName commandName, String userName, float amount) {
            this.commandName = commandName;
            this.userName = userName;
            this.amount = amount;
        }
    }

    //endregion

}
