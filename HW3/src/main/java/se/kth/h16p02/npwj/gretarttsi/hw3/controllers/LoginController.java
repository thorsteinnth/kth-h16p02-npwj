package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.database.MarketplaceDBException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.BankAccountNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.PasswordNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw3.traders.TraderClient;
import se.kth.h16p02.npwj.gretarttsi.hw3.traders.TraderImpl;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class LoginController extends Controller
{
    private final int passwordLength = 8;

    public LoginController(Bank bank, MarketPlace marketPlace)
    {
        super(null, bank, marketPlace);
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

        while (run )
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

    private void register(String username, String password)
    {
        pingMarketPlace(false);
        pingBank(false);

        System.out.println("Registering user with username and password: " + username + " " + password);
        try
        {
            Trader newTrader = new TraderImpl(username,password);

            //CheckIfRegister function onlu checks if there is a trader already registered with this username
            boolean userNameTaken = this.marketPlace.checkIfRegistered(newTrader);

            if(userNameTaken == true)
            {
                System.out.println("This username is taken. Please choose another name");
                return;
            }

            // The user need to have a bank account.
            try
            {
                this.bank.newAccount(username);
            }
            catch (RejectedException e)
            {
                //If an account under this name already exist then we return;
                System.out.println(e.getMessage());
            }

            // After creating the account for the user we register to the marketplace
            try
            {
                if(this.marketPlace.register(newTrader))
                {
                    TraderClient.goToLogin = false;
                    System.out.println("Marketplace registration and bank account creation was completed successfully for the user: "+ username);
                    new HomeController(newTrader, bank, marketPlace).run();
                }
                else
                {
                    //this.bank.deleteAccount(this.bank.getAccount(username));
                    System.out.println("Could not register user");
                }
            }
            catch (TraderAlreadyExistsException|BankAccountNotFoundException e)
            {
                System.out.println(e.getMessage());
                return;
            }
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
        }
    }

    private void login(String username, String password)
    {
        pingMarketPlace(false);
        pingBank(false);

        System.out.println("Logging in user with username and password: " + username + " " + password);

        try
        {
            Trader newTrader = new TraderImpl(username,password);

            try
            {
                 marketPlace.login(newTrader);
            }
            catch (TraderNotFoundException tnfe)
            {
                System.out.println(tnfe.getMessage());
                return;
            }
            catch (PasswordNotFoundException pe)
            {
                System.out.println(pe.getMessage());
                return;
            }

            TraderClient.goToLogin = false;
            new HomeController(newTrader, bank, marketPlace).run();
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
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
            case pingbank:
                pingBank(true);
                return true;
            case pingmarketplace:
                pingMarketPlace(true);
                return true;
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

        if (loginCommand.getPassword().length() <= this.passwordLength)
        {
            System.out.println("Password is not legal. It has to be longer than 8 characters");
            return true;
        }

        switch (loginCommand.getCommandType())
        {
            case login:
                login(loginCommand.getUsername(), loginCommand.getPassword());
                return true;
            case register:
                register(loginCommand.getUsername(), loginCommand.getPassword());
                return true;
        }

        return true;
    }

    private void pingBank(boolean showMessage)
    {
        try
        {
            if(showMessage)
                System.out.println(bank.ping());
        }
        catch (RemoteException e)
        {
            System.out.println("lost connection to bank");
            System.err.println(e);
            intiateConnectionToBank();
        }

    }

    private void intiateConnectionToBank()
    {
        try
        {
            System.out.println("Please wait -> restarting connection to marketplace");
            bank = (Bank) Naming.lookup("Nordea");
            System.out.println("TraderClient - Connected to Nordea bank");
        }
        catch (Exception e)
        {
            System.out.println("TraderClient: The runtime failed: " + e.getMessage());
        }
    }

    private void pingMarketPlace(boolean showMessage)
    {
        try
        {
            if(showMessage)
                System.out.println(marketPlace.ping());
        }
        catch (RemoteException e)
        {
            System.out.println("lost connection to marketplace");
            System.err.println(e);
            intiateConnectionToMarketplace();
            //printConsolePrompt();
        }
    }

    private void intiateConnectionToMarketplace()
    {
        try
        {
            System.out.println("Please wait -> restarting connection to marketplace");
            marketPlace = (MarketPlace) Naming.lookup("MarketPlace");
            System.out.println("TraderClient - Connected to marketplace");
        }
        catch (Exception e)
        {
            System.out.println("TraderClient: The runtime failed: " + e.getMessage());
        }
    }
}
