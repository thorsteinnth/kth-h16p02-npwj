package se.kth.h16p02.npwj.gretarttsi.hw3.traders;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import se.kth.h16p02.npwj.gretarttsi.hw3.shared.remoteInterfaces.Trader;

public class TraderImpl extends UnicastRemoteObject implements Trader
{
    private static final String WISH_LIST_AVAILABLE = "The item %s that you have in your wish list is now available in the marketplace";
    private static final String ITEM_SOLD = "Your item %s has been sold on the marketplace. Deposit has been made to your account";

    private String username;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TraderImpl trader = (TraderImpl) o;

        return username.equals(trader.username);
    }

    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + username.hashCode();
        return result;
    }

    public TraderImpl(String username) throws RemoteException
    {
        super();
        this.username = username;
    }

    @Override
    public String getUsername()
    {
        return this.username;
    }

    @Override
    public void wishListAvailableNotification(String itemName) throws RemoteException {
        System.out.println("\n" + String.format(WISH_LIST_AVAILABLE,itemName));
        // TODO Print the correct console output depending on what controller he is in
    }

    @Override
    public void itemSoldNotification(String itemName) throws RemoteException {
        System.out.println("\n" + String.format(ITEM_SOLD,itemName));
        // TODO Print the correct console output depending on what controller he is in
    }
}
