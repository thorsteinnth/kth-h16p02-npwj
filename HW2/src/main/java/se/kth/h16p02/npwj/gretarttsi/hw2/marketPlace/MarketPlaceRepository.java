package se.kth.h16p02.npwj.gretarttsi.hw2.marketPlace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class MarketPlaceRepository
{
    private ArrayList<Item> items;
    private ArrayList<Trader> traders;

    public MarketPlaceRepository()
    {
        this.items = new ArrayList<>();
        this.traders = new ArrayList<>();
    }

    //region Traders

    public boolean registerTrader(Trader trader) throws TraderAlreadyExistsException
    {
        try
        {
            if (!isUsernameUnique(trader))
                throw new TraderAlreadyExistsException(
                        "Trader already exists with username: " + trader.getUsername()
                );

            this.traders.add(trader);
            return true;
        }
        catch (RemoteException ex)
        {
            System.err.println(ex);
            return false;
        }
    }

    public boolean deregisterTrader(Trader trader)
    {
        this.traders.remove(trader);
        return true;
    }

    public boolean isTraderRegistered(Trader trader)
    {
        if (this.traders.contains(trader))
            return true;
        else
            return false;
    }

    private boolean isUsernameUnique(Trader trader) throws RemoteException
    {
        boolean isUnique = true;

        for (Trader t : this.traders)
        {
            if (t.getUsername().equals(trader.getUsername()))
                isUnique = false;
        }

        return isUnique;
    }

    //endregion

    //region Items
    //endregion
}
