package se.kth.h16p02.npwj.gretarttsi.hw3.traders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.StringTokenizer;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.entities.Account;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.*;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

public class TraderImpl extends UnicastRemoteObject implements Trader
{
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String HOME = "Home";
    private static final String MARKETPLACE = "Marketplace";
    private static final String PRODUCT_ERROR = "Product name is not specified";
    private static final String AMOUNT_ERROR = "Illegal amount specified";
    private static final String WISH_LIST_AVAILABLE = "The item %s that you have in your wish list is now available in the marketplace";
    private static final String ITEM_SOLD = "Your item %s has been sold on the marketplace. Deposit has been made to your account";
    private static final String ITEM_ALREADY_EXIST = "The Item %s already exist in the marketplace at this cost";
    private static final String ITEM_NOT_FOUND = "This item is not for sale in the marketplace. You can add it to your wishlist";
    private static final String TRADER_ALREADY_EXIST = "Trader with this username already exist";
    private static final String TRADER_NOT_FOUND = "User not registered";
    private static final String REJECTED = "Rejected";
    private static final String REGISTRATION_SUCCESS = "User successfully registered";
    private static final String DEREGISTRATION_SUCCESS = "User successfully deregistered";
    private static final String BANKACCOUNT_NOT_FOUND = "Could not find bank account";
    private static final String BUY_SUCCESSFUL = "You bought %s for the price of %s";
    private static final String ADD_TO_WISHLIST_SUCCESSFUL = "Item has been added to wish list";
    private static final String SELLING_SUCCESSFUL = "You are now selling %s for the price of %s";
    private static final String SELLING_FAILURE = "Unable to sell item";
    private static final String NO_WISHES_REGISTERED = "You have no wishes registered";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final int DEFAULT_PORT = 1099;
    private static final String DEFAULT_HOST = "localhost";

    private BufferedReader consoleIn;
    private Bank bankobj;
    private MarketPlace marketplaceobj;
    private String bankname;
    private String username;
    private State state;

    enum HomeCommandName{
        bank,
        marketplace,
        home,
        help,
        ls
    }

    enum BankCommandName {
        newaccount,
        getaccount,
        deleteaccount,
        deposit,
        withdraw,
        balance,
        exit,
        help,
        list,
        ls
    };

    enum MarketplaceCommandName {
        register,
        unregister,
        inspect,
        sell,
        buy,
        wish,
        getwishes,
        help,
        exit,
        ls
    }

    enum State
    {
        bank,
        marketplace,
        home
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TraderImpl trader = (TraderImpl) o;

        return username.equals(trader.username);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    public TraderImpl(String username, String host, int port) throws RemoteException
    {
        super();

        this.username = username;
        this.bankname = DEFAULT_BANK_NAME;

        try
        {
            try
            {
                LocateRegistry.getRegistry(host,port).list();
                System.out.println("Found registry at: " + host + " at port " + port);
            }
            catch (RemoteException e)
            {
                if(host.equals(DEFAULT_HOST))
                {
                    LocateRegistry.createRegistry(DEFAULT_PORT);
                    System.out.println("Created registry at: " + DEFAULT_HOST + " at port " + DEFAULT_PORT);
                }
                else
                {
                    System.out.println(e);
                    System.exit(1);
                }
            }

            //Properties props = System.getProperties();
            //props.setProperty("java.rmi.server.hostname", host);
            bankobj = (Bank) Naming.lookup(bankname);
            marketplaceobj = (MarketPlace)Naming.lookup("MarketPlace");

        }
        catch (Exception e)
        {
            System.out.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }

        System.out.println(this.username + " - Connected to bank: " + bankname);
        System.out.println(this.username + " - Connected to marketplace");
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public void wishListAvailableNotification(String itemName) throws RemoteException {
        System.out.println("\n" + String.format(WISH_LIST_AVAILABLE,itemName));
        runConsolOutput(state);
    }

    @Override
    public void itemSoldNotification(String itemName) throws RemoteException {
        System.out.println("\n" + String.format(ITEM_SOLD,itemName));
        runConsolOutput(state);
    }

    public void runConsolOutput(State state)
    {
        switch(state)
        {
            case home:
                System.out.print(username + "@" + HOME + ">");
                break;
            case bank:
                System.out.print(username + "@" + bankname + ">");
                break;
            case marketplace:
                System.out.print(username + "@" + MARKETPLACE + ">");
                break;
        }
    }

    //region Run functions

    public void run()
    {
        this.consoleIn = new BufferedReader(new InputStreamReader(System.in));

        runHome();
    }

    private void runHome()
    {
        while (true)
        {
            state = State.home;
            runConsolOutput(state);
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
            state = State.marketplace;
            runConsolOutput(state);
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
            state = State.bank;
            runConsolOutput(state);
            try
            {
                String userInput = consoleIn.readLine();
                run = bankExecute(bankParse(userInput));
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
    //endregion functions

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
            case ls:
            case help:
                for (HomeCommandName homeCommandName : HomeCommandName.values()) {
                    System.out.println(homeCommandName);
                }
                return;

            case marketplace:
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
                if (this.marketplaceobj.deregister(this))
                {
                    System.out.println(DEREGISTRATION_SUCCESS);
                    return true;
                }
                else
                {
                    return false;
                }

            case register:
                try
                {
                    this.marketplaceobj.register(this);
                    System.out.println(REGISTRATION_SUCCESS);
                    return true;
                }
                catch (TraderAlreadyExistsException e)
                {
                    System.out.println(TRADER_ALREADY_EXIST);
                    return false;
                }
                catch (BankAccountNotFoundException e)
                {
                    System.out.println(BANKACCOUNT_NOT_FOUND);
                    return true;
                }

            case inspect:
                ArrayList<SaleItem> saleItems = this.marketplaceobj.inspectAvailableItems();
                System.out.println(getAvailbleItemsDisplayString(saleItems));
                return true;

            case getwishes:
                ArrayList<WishListItem> wishListItems = this.marketplaceobj.getTradersWishes(this);
                System.out.println(getTradersWishesDisplayString(wishListItems));
                return true;

            case ls:
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
            try
            {
                if (marketplaceobj.sell(this, new Item(command.productName, new BigDecimal(command.amount))))
                    System.out.println(String.format(SELLING_SUCCESSFUL,command.productName,String.valueOf(command.getAmount())));
                else
                    System.out.println(SELLING_FAILURE);
            }
            catch (ItemAlreadyExistsException e)
            {
                System.out.println(String.format(ITEM_ALREADY_EXIST, command.getProductName()));
            }
            catch (TraderNotFoundException e)
            {
                System.out.println(TRADER_NOT_FOUND);
            }
            return true;
        }

        //commands using two arguments
        if(command.amount < 0)
        {
            System.out.println(AMOUNT_ERROR);
            return true;
        }

        // commands using three arguments
        switch (command.commandName)
        {
            case buy:
                try
                {
                    this.marketplaceobj.buy(this, new Item(command.productName, new BigDecimal(command.getAmount())));
                    System.out.println(String.format(BUY_SUCCESSFUL,command.productName,String.valueOf(command.getAmount())));
                }
                catch (TraderNotFoundException e)
                {
                    System.out.println(TRADER_NOT_FOUND);
                }
                catch (ItemNotFoundException e)
                {
                    System.out.println(ITEM_NOT_FOUND);
                }
                catch (RejectedException e)
                {
                    System.out.println(REJECTED);
                }
                catch (BankAccountNotFoundException e)
                {
                    System.out.println(e.getMessage());
                }
                catch (BuyException e)
                {
                    System.out.println(e.getMessage());
                }
                catch (InsufficientFundsException ex)
                {
                    System.out.println(INSUFFICIENT_FUNDS);
                }
                return true;

            case wish:
                try
                {
                    this.marketplaceobj.addItemToWishlist(this, new Item(command.productName, new BigDecimal(command.getAmount())));
                    System.out.println(ADD_TO_WISHLIST_SUCCESSFUL);
                }
                catch (TraderNotFoundException ex)
                {
                    System.out.println(TRADER_NOT_FOUND);
                }
                catch (NumberFormatException ex)
                {
                    System.err.println(ex);
                }
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
        return new BankCommand(commandName, this.username, amount);
    }

    boolean bankExecute(BankCommand command) throws RemoteException, RejectedException, InsufficientFundsException
    {
        if (command == null)
        {
            return true;
        }

        switch (command.getCommandName())
        {
            /*
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
             */
            case exit:
                runHome();
                return false;

            case ls:
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
            case newaccount:
                bankobj.newAccount(username);
                return true;
            case deleteaccount:
                bankobj.deleteAccount(username);
                return true;
        }

        // All further commands require a Account reference
        Account acc = bankobj.findAccount(username);
        if (acc == null)
        {
            System.out.println("No account for " + username);
            return true;
        }

        switch (command.getCommandName())
        {
            case getaccount:
                System.out.println(acc);
                break;

            case deposit:
                acc.deposit(command.getAmount());
                break;

            case withdraw:
                acc.withdraw(command.getAmount());
                break;

            case balance:
                System.out.println("balance: $" + acc.getBalance());
                break;

            default:
                System.out.println("Illegal command");
        }

        return true;
    }

    private class BankCommand
    {
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

        private BankCommand(BankCommandName commandName, String userName, float amount)
        {
            this.commandName = commandName;
            this.userName = userName;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "BankCommand{" +
                    "userName='" + userName + '\'' +
                    ", amount=" + amount +
                    ", commandName=" + commandName +
                    '}';
        }
    }
    //endregion

    //region Display functions

    private String getAvailbleItemsDisplayString (ArrayList<SaleItem> saleItems)
    {
        if (saleItems.size() == 0)
            return "No items available";

        StringBuilder stringBuilder = new StringBuilder();

        for(SaleItem saleItem : saleItems)
        {
            stringBuilder.append(saleItem.getItem().getName() + " - " + "Price: " + saleItem.getItem().getPrice() + "\n");
        }

        return stringBuilder.toString();
    }

    private String getTradersWishesDisplayString(ArrayList<WishListItem> wishListItems)
    {

        if(wishListItems.size() > 0)
        {
            StringBuilder wishListItemsString = new StringBuilder();
            for(WishListItem wishListItem: wishListItems)
            {
                wishListItemsString.append(wishListItem.getItem().toDisplayString() + "\n");
            }
            return wishListItemsString.toString();
        }
        else
        {
            return NO_WISHES_REGISTERED;
        }
    }
    //endregion

}
