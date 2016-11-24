package se.kth.h16p02.npwj.gretarttsi.hw2.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Exceptions.RejectedException;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Account;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Bank;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.MarketPlace;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

import java.math.BigDecimal;
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

    private MarketPlaceRepository repository;
    private Bank bank;

    public MarketPlaceImpl() throws RemoteException
    {
        super();
        this.repository = new MarketPlaceRepository();

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
    public void addItemToWishlist(Trader trader, Item item, BigDecimal maxPrice) throws RemoteException
    {
        System.out.println("Trader " + trader
                + " adding item to wishlist with max price " + item.getPrice()
                + ": "+ item.getName()
        );

        this.repository.addWishListItem(trader,item);
    }

    @Override
    public ArrayList<WishListItem> getTradersWishes(Trader trader) throws RemoteException {
        return this.repository.getTradersWishes(trader);
    }

    @Override
    public boolean sell(Trader trader, Item item) throws RemoteException, ItemAlreadyExistsException, TraderNotFoundException
    {
        if (!this.repository.isTraderRegistered(trader))
        {
            throw new TraderNotFoundException ("Trader not found in marketplace");
        }

        return this.repository.addSaleItem(trader, item);
    }

    @Override
    public boolean buy(Trader trader, Item item) throws RemoteException, TraderNotFoundException, ItemNotFoundException, RejectedException, BankAccountNotFoundException
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

        Account sellerAccount = bank.getAccount(saleItem.getTrader().getUsername());
        Account buyerAccount = bank.getAccount(trader.getUsername());
        if (sellerAccount == null)
            throw new BankAccountNotFoundException("Could not find bank account for seller");
        if (buyerAccount == null)
            throw new BankAccountNotFoundException("Could not find bank account for buyer");

        sellerAccount.deposit(saleItem.getItem().getPrice().floatValue());
        buyerAccount.withdraw(saleItem.getItem().getPrice().floatValue());

        Trader seller = saleItem.getTrader();
        seller.itemSoldNotification(saleItem.getItem().getName());

        this.repository.removeSaleItem(saleItem);

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
        if (this.bank.getAccount(trader.getUsername()) == null)
            throw new BankAccountNotFoundException("Could not find bank account for user: " + trader.getUsername());

        return this.repository.registerTrader(trader);
    }

    @Override
    public boolean deregister(Trader trader) throws RemoteException
    {
        return this.repository.deregisterTrader(trader);
    }

    @Override
    public boolean checkIfRegistered(Trader trader) throws RemoteException
    {
        return this.repository.isTraderRegistered(trader);
    }



    //endregion
}
