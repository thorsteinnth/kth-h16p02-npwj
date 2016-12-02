package se.kth.h16p02.npwj.gretarttsi.hw3.controllers;

import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.*;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.History;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.InsufficientFundsException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;
import se.kth.h16p02.npwj.gretarttsi.hw3.traders.TraderClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MarketPlaceController extends Controller
{
    private static final String MARKETPLACE = "Marketplace";
    private static final String REGISTRATION_SUCCESS = "User successfully registered";
    private static final String LOGOUT_SUCCESS = "User successfully logged out";
    private static final String TRADER_ALREADY_EXIST = "Trader with this username already exist";
    private static final String TRADER_NOT_FOUND = "User not registered";
    private static final String BANKACCOUNT_NOT_FOUND = "Could not find bank account";
    private static final String SELLING_SUCCESSFUL = "You are now selling %s for the price of %s";
    private static final String SELLING_FAILURE = "Unable to sell item";
    private static final String ITEM_ALREADY_EXIST = "The Item %s already exist in the marketplace at this cost";
    private static final String ITEM_NOT_FOUND = "This item is not for sale in the marketplace. You can add it to your wishlist";
    private static final String INSUFFICIENT_FUNDS = "Insufficient funds";
    private static final String ADD_TO_WISHLIST_SUCCESSFUL = "Item has been added to wish list";
    private static final String BUY_SUCCESSFUL = "You bought %s for the price of %s";
    private static final String REJECTED = "Rejected";
    private static final String PRODUCT_ERROR = "Product name is not specified";
    private static final String AMOUNT_ERROR = "Illegal amount specified";
    private static final String NO_WISHES_REGISTERED = "You have no wishes registered";

    public MarketPlaceController(Trader user, Bank bank, MarketPlace marketPlace)
    {
        super(user, bank, marketPlace);
    }

    @Override
    protected void printConsolePrompt()
    {
        System.out.print(username + "@" + MARKETPLACE + ">");
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
                System.err.println("Something went wrong, we lost connection to the marketplace. You will be logged out");
                System.err.println(ex);
                System.out.print("Login>");
                TraderClient.goToLogin = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean execute(Command command) throws RemoteException
    {
        if (command == null)
        {
            return true;
        }

        MarketPlaceCommand marketPlaceCommand = (MarketPlaceCommand)command;

        // Commands using only one argument
        switch (marketPlaceCommand.getCommandType())
        {
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

                /*
            case register:
                try
                {
                    marketPlace.register(user);
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
                */

            case inspect:
                ArrayList<SaleItem> saleItems = marketPlace.inspectAvailableItems();
                System.out.println(getAvailbleItemsDisplayString(saleItems));
                return true;

            case getwishes:
                ArrayList<WishListItem> wishListItems = marketPlace.getTradersWishes(user);
                System.out.println(getTradersWishesDisplayString(wishListItems));
                return true;

            case history:
                History history = marketPlace.getTradersHistory(user);
                System.out.println(history.toDisplayString());
                return true;

            case ls:
            case help:
                for (MarketPlaceCommand.CommandType availableCommand : MarketPlaceCommand.CommandType.values())
                {
                    System.out.println(availableCommand);
                }
                return true;

            case exit:
                return false;
        }

        // Commands using two arguments

        if (marketPlaceCommand.getProductName() == null || marketPlaceCommand.getProductName() == "")
        {
            System.out.println(PRODUCT_ERROR);
        }

        if (marketPlaceCommand.getCommandType() == MarketPlaceCommand.CommandType.sell)
        {
            try
            {
                if (marketPlace.sell(user, new Item(marketPlaceCommand.getProductName(), new BigDecimal(marketPlaceCommand.getAmount()))))
                    System.out.println(String.format(SELLING_SUCCESSFUL, marketPlaceCommand.getProductName(), String.valueOf(marketPlaceCommand.getAmount())));
                else
                    System.out.println(SELLING_FAILURE);
            }
            catch (ItemAlreadyExistsException e)
            {
                System.out.println(String.format(ITEM_ALREADY_EXIST, marketPlaceCommand.getProductName()));
            }
            catch (TraderNotFoundException e)
            {
                System.out.println(TRADER_NOT_FOUND);
            }
            return true;
        }

        if (marketPlaceCommand.getAmount() < 0)
        {
            System.out.println(AMOUNT_ERROR);
            return true;
        }

        // Commands using three arguments

        switch (marketPlaceCommand.getCommandType())
        {
            case buy:
                try
                {
                    marketPlace.buy(user, new Item(marketPlaceCommand.getProductName(), new BigDecimal(marketPlaceCommand.getAmount())));
                    System.out.println(String.format(BUY_SUCCESSFUL, marketPlaceCommand.getProductName(), String.valueOf(marketPlaceCommand.getAmount())));
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
                    marketPlace.addItemToWishlist(user, new Item(marketPlaceCommand.getProductName(), new BigDecimal(marketPlaceCommand.getAmount())));
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

        MarketPlaceCommand.CommandType commandType = null;
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
                        commandType = MarketPlaceCommand.CommandType.valueOf(
                                MarketPlaceCommand.CommandType.class,
                                commandNameString
                        );
                    }
                    catch (IllegalArgumentException ex)
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

        return new MarketPlaceCommand(commandType, productName, amount);
    }

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
                wishListItemsString.append(wishListItem.getItem().toDisplayString() + " " + "bought : " + wishListItem.isBought() + "\n" );
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