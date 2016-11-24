package se.kth.h16p02.npwj.gretarttsi.hw2.marketplace;

import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.Item;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.SaleItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.Domain.WishListItem;
import se.kth.h16p02.npwj.gretarttsi.hw2.shared.RemoteInterfaces.Trader;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarketPlaceRepository
{
    private ArrayList<Trader> traders;
    private ArrayList<SaleItem> saleItems;
    private ArrayList<WishListItem> wishListItems;

    public MarketPlaceRepository()
    {
        this.traders = new ArrayList<>();
        this.saleItems = new ArrayList<>();
        this.wishListItems = new ArrayList<>();
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

    public boolean addSaleItem(Trader trader, Item item) throws ItemAlreadyExistsException
    {
        if(!isItemUnique(item))
        {
           throw new ItemAlreadyExistsException("Item already exists in marketplace " + item.toString());
        }

        this.saleItems.add(new SaleItem(item,trader));

        return false;
    }

    public boolean removeSaleItem(SaleItem saleItem)
    {
        this.saleItems.remove(saleItem);

        return true;
    }

    public ArrayList<SaleItem> getAllSaleItems()
    {
        return this.saleItems;
    }

    public boolean itemExists(Item item)
    {
        return !isItemUnique(item);
    }

    public SaleItem findSaleItem(Item item) throws ItemNotFoundException
    {
        List<SaleItem> foundSaleItems =
                this.saleItems
                        .stream()
                        .filter(si -> si.getItem().equals(item))
                        .collect(Collectors.toList());

        if (foundSaleItems.size() == 0)
            throw new ItemNotFoundException(item.toString());
        else if (foundSaleItems.size() > 1)
            throw new IllegalStateException("More than one item found: " + item);
        else
            return foundSaleItems.get(0);
    }

    private boolean isItemUnique(Item item)
    {
        for(SaleItem saleItem : this.saleItems)
        {
            if(item.equals(saleItem.getItem()))
            {
                return false;
            }
        }

        return true;
    }

    //endregion

    //region WishListItem
    public boolean addWishListItem(Trader trader, Item item) throws WishListItemAlreadyExistsException
    {
        return true;
    }

    public boolean removeWishListItem(WishListItem wishListItem)
    {
        this.wishListItems.remove(wishListItem);
        return true;
    }

    public boolean wishListItemExists(Trader trader, Item item)
    {
        for(WishListItem wishListItem : this.wishListItems)
        {
            if(trader.equals(wishListItem.getTrader()) && item.equals(wishListItem.getItem()))
            {
                return false:
            }
        }
        return true;
    }

    //endregion
}
