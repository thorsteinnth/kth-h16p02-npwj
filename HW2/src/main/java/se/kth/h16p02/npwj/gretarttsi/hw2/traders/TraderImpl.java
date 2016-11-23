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

public class TraderImpl extends UnicastRemoteObject implements Trader
{
    private static final String USAGE = "java bankrmi.TraderClient <bank_url>";
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String HOME = "Home";
    private static final String MARKETPLACE = "Marketplace";
    private static final String PRODUCT_ERROR = "Product name is not specified";
    private static final String AMOUNT_ERROR = "Illegal amount specified";
    private static final String BANK = "Bank";

    private BufferedReader consoleIn;
    private Account account;
    private Bank bankobj;
    private String bankname;
    private String username;

    enum HomeCommandName{
        bank,
        marketPlace,
        home,
        help
    }

    enum BankCommandName {
        newAccount,
        getAccount,
        deleteAccount,
        deposit,
        withdraw,
        balance,
        exit,
        help,
        list;
    };

    enum MarketplaceCommandName {
        register,
        unregister,
        status,
        inspect,
        sell,
        buy,
        wish,
        help,
        exit
    }

    //TODO breyta command line virkni hjá bankanum þannig að þegar ýtt er á quit þá er farið aftur á home.

    public TraderImpl(String username) throws RemoteException
    {
        super();

        this.username = username;
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
    public String getUsername()
    {
        return this.username;
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
            System.out.print(username + "@" + HOME + ">");
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
        boolean run = true;

        while (run)
        {
            System.out.print(username + "@" + MARKETPLACE + ">");
            try
            {
                String userInput = consoleIn.readLine();
                run = marketPlaceExecute(marketplaceParse(userInput));
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

    private void runBank()
    {
        boolean run = true;

        while (run)
        {
            System.out.print(username + "@" + bankname + ">");
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
                runMarketplace();
                return;

            case bank:
                runBank();
                return;

        }
    }
    //endregion

    //region Marketplace command line function
    private MarketPlaceCommand marketplaceParse(String userInput)
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

        MarketplaceCommandName commandName = null;
        String productName = null;
        float amount = -1;
        int userInputTokenNo = 1;

        while (tokenizer.hasMoreTokens())
        {
            switch (userInputTokenNo)
            {
                case 1:
                    try
                    {
                        String commandNameString = tokenizer.nextToken();
                        commandName = MarketplaceCommandName.valueOf(MarketplaceCommandName.class, commandNameString);
                    }
                    catch (IllegalArgumentException commandDoesNotExist)
                    {
                        System.out.println("Illegal command");
                        return null;
                    }
                    break;

                case 2:
                    productName = tokenizer.nextToken();
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
        return new MarketPlaceCommand(commandName, productName, amount);
    }

    private boolean marketPlaceExecute(MarketPlaceCommand command) throws RemoteException
    {
        if(command == null)
        {
            return true;
        }

        // commands using only one argument
        switch (command.commandName)
        {
            case unregister:
                System.out.println("Not implemented yeat");
                return true;

            case register:
                System.out.println("Not implemented yeat");
                return true;

            case status:
                System.out.println("Not implemented yeat");
                return true;

            case inspect:
                System.out.println("Not implemented yeat");
                return true;

            case help:
                for (MarketplaceCommandName commandName : MarketplaceCommandName.values()) {
                    System.out.println(commandName);
                }
                return true;

            case exit:
                return false;
        }

        //commands using two arguments
        if(command.productName == null || command.productName == "")
        {
            System.out.println(PRODUCT_ERROR);
        }

        if(command.commandName == MarketplaceCommandName.sell)
        {
            System.out.println("Sell Not implemented yeat");
            return true;
        }

        //commands using two arguments
        if(command.amount < 0)
        {
            System.out.println(AMOUNT_ERROR);
        }

        // commands using three arguments
        switch (command.commandName)
        {
            case buy:
                System.out.println("Buy not implemented yeat");
                return true;

            case wish:
                System.out.println("Wish not implemented yeat");
                return true;

            default:
                System.out.println("Illegal command");
        }

        return true;
    }

    private class MarketPlaceCommand {
        private MarketplaceCommandName commandName;
        private String productName;
        private float amount;

        private MarketplaceCommandName getCommandName() {
            return commandName;
        }

        private String getProductName() {
            return productName;
        }

        private float getAmount() {
            return amount;
        }

        private MarketPlaceCommand(MarketplaceCommandName commandName, String productName, float amount) {
            this.commandName = commandName;
            this.productName = productName;
            this.amount = amount;
        }

        public String toString()
        {
            return "command name: " + this.commandName.toString() + " " +
                    "product name: " + this.productName + " " +
                    "amount: " +  Float.toString(this.amount);
        }
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

            case exit:
                runHome();
                return false;

            case help:
                for (BankCommandName commandName : BankCommandName.values()) {
                    System.out.println(commandName);
                }
                return true;

        }

        if (username == null || username == "")
        {
            System.out.println("username is not specified");
            return true;
        }

        switch (command.getCommandName())
        {
            case newAccount:
                bankobj.newAccount(username);
                return true;
            case deleteAccount:
                bankobj.deleteAccount(username);
                return true;
        }

        // All further commands require a Account reference
        Account acc = bankobj.getAccount(username);
        if (acc == null)
        {
            System.out.println("No account for " + username);
            return true;
        }
        else
        {
            account = acc;
        }

        switch (command.getCommandName())
        {
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
