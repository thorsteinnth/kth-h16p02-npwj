package se.kth.h16p02.npwj.gretarttsi.hw3.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model.Account;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model.AccountDTO;
import se.kth.h16p02.npwj.gretarttsi.hw3.bank.server.model.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw3.marketplace.exceptions.*;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class MarketPlaceImpl extends UnicastRemoteObject implements MarketPlace
{
    private static final String DEFAULT_BANK_NAME = "Nordea";
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1099;

    private MarketPlaceRepository repository;
    private Bank bank;

    public MarketPlaceImpl(String host, int port) throws RemoteException
    {
        super();
        this.repository = new MarketPlaceRepository();

        try
        {
            try
            {
                LocateRegistry.getRegistry(host, port).list();
                System.out.println("Found registry at: " + host + " " + port);
            }
            catch (RemoteException e)
            {
                if (host.equals(DEFAULT_HOST))
                {
                    LocateRegistry.createRegistry(DEFAULT_PORT);
                    System.out.println("Created registry at: " + DEFAULT_HOST + " " + DEFAULT_PORT);
                }
                else
                {
                    System.err.println(e);
                    System.exit(1);
                }
            }

            this.bank = (Bank) Naming.lookup(DEFAULT_BANK_NAME);
        }
        catch (NotBoundException|MalformedURLException e)
        {
            System.err.println("The runtime failed: " + e.getMessage());
            System.exit(0);
        }

        System.out.println("Marketplace connection to bank established");
    }

    @Override
    public boolean addItemToWishlist(Trader trader, Item item) throws RemoteException, TraderNotFoundException
    {
        if (!this.repository.isTraderRegistered(trader))
        {
            throw new TraderNotFoundException ("Trader not found in marketplace");
        }

        return this.repository.addWishListItem(trader,item);
    }

    @Override
    public ArrayList<WishListItem> getTradersWishes(Trader trader) throws RemoteException {

        System.out.println("Getting traders wishes");
        ArrayList<WishListItem>  wishListItems = this.repository.getTradersWishes(trader);
        return wishListItems;
    }

    @Override
    public boolean sell(Trader trader, Item item) throws RemoteException, ItemAlreadyExistsException, TraderNotFoundException
    {
        if (!this.repository.isTraderRegistered(trader))
        {
            throw new TraderNotFoundException ("Trader not found in marketplace");
        }

        if (this.repository.addSaleItem(trader, item))
        {
            // Item successfully put up for sale
            // Notify everyone that has this item on their wishlist, for the same or greater price
            ArrayList<Trader> tradersToNotify = this.repository.getTradersThatHaveItemInWishListForGreaterOrSamePrice(item);
            for (Trader traderToNotify : tradersToNotify)
                traderToNotify.wishListAvailableNotification(item.getName());

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean buy(Trader trader, Item item) throws
            RemoteException,
            TraderNotFoundException,
            ItemNotFoundException,
            RejectedException,
            BankAccountNotFoundException,
            BuyException,
            InsufficientFundsException
    {
        if (!this.repository.isTraderRegistered(trader))
        {
            throw new TraderNotFoundException ("Trader not found in marketplace");
        }

        if (!this.repository.itemExists(item))
        {
            throw new ItemNotFoundException("Item not found: " + item);
        }

        SaleItem saleItem = this.repository.findSaleItem(item);

        // Check if the seller and buyer are the same person
        if (trader.equals(saleItem.getTrader()))
        {
            throw new BuyException("Cannot buy an item that you are selling");
        }

        // Get the relevant bank accounts
        AccountDTO sellerAccount = bank.findAccount(saleItem.getTrader().getUsername());
        Account buyerAccount = bank.findAccount(trader.getUsername());
        if (sellerAccount == null)
            throw new BankAccountNotFoundException("Could not find bank account for seller");
        if (buyerAccount == null)
            throw new BankAccountNotFoundException("Could not find bank account for buyer");

        // "Transaction" for moving money around
        boolean depositDone = false;
        boolean withdrawDone = false;
        float itemPrice = saleItem.getItem().getPrice().floatValue();
        try
        {
            sellerAccount.deposit(itemPrice);
            depositDone = true;
            buyerAccount.withdraw(itemPrice);
            withdrawDone = true;
        }
        catch (Exception ex)
        {
            // Rollback changes
            if (depositDone)
                sellerAccount.withdraw(itemPrice);
            if (withdrawDone)
                buyerAccount.deposit(itemPrice);

            throw ex;
        }

        // Send sale notification to seller
        Trader seller = saleItem.getTrader();
        seller.itemSoldNotification(saleItem.getItem().getName());

        // Remove sale item from the market repository
        this.repository.removeSaleItem(saleItem);

        // Remove the item from the buyer's wish list if it is there
        WishListItem foundWishListItem = this.repository.findWishListItem(trader, item);
        if (foundWishListItem != null)
            this.repository.removeWishListItem(foundWishListItem);

        return true;
    }

    @Override
    public ArrayList<SaleItem> inspectAvailableItems() throws RemoteException
    {
        return this.repository.getAllSaleItems();
    }

    @Override
    public boolean register(Trader trader) throws RemoteException, TraderAlreadyExistsException, BankAccountNotFoundException
    {
        if (this.bank.findAccount(trader.getUsername()) == null)
            throw new BankAccountNotFoundException("Could not find bank account for user: " + trader.getUsername());

        return this.repository.registerTrader(trader);
    }

    @Override
    public boolean deregister(Trader trader) throws RemoteException
    {
        this.repository.removeTradersWishListItems(trader);
        return this.repository.deregisterTrader(trader);
    }

    @Override
    public boolean checkIfRegistered(Trader trader) throws RemoteException
    {
        return this.repository.isTraderRegistered(trader);
    }

    //endregion
}
