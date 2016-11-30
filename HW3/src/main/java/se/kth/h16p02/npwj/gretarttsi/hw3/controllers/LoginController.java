package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.BankAccountNotFoundException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.TraderAlreadyExistsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw3.traders.TraderImpl;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

public class LoginController extends Controller
{
    private final int passwordLength = 8;
    private String host;
    private int port;

    public LoginController(String host, int port)
    {
        super(null, host, port);
        this.host = host;
        this.port = port;
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

    private void register(String username, String password)
    {
        // TODO Save to DB and stuff
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
                return;
            }

            try
            {
                this.marketPlace.register(newTrader);
            }
            catch (TraderAlreadyExistsException|BankAccountNotFoundException e)
            {
                System.out.println(e.getMessage());
            }

            new HomeController(newTrader, this.host, this.port).run();
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
        }
    }

    private void login(String username, String password)
    {
        // TODO Save to DB and stuff
        System.out.println("Logging in user with username and password: " + username + " " + password);

        try
        {
            Trader newTrader = new TraderImpl(username);
            new HomeController(newTrader, this.host, this.port).run();
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
}
