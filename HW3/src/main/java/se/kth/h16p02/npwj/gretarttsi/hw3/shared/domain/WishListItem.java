package se.kth.h16p02.npwj.gretarttsi.hw3.shared.domain;

import java.io.Serializable;
import java.rmi.RemoteException;

public class WishListItem implements Serializable
{
    private String username;
    private final Item item;
    private boolean bought;

    /**
     * WishListItem
     * Username is the name of the trader that owns this wish list item
     * Item is the item that he is wishing for. The price of the item is the max price that
     * the trader is willing to pay for the item.
     * */
    public WishListItem(String username, Item item)
    {
        this.username = username;
        this.item = item;
        this.bought = false;
    }

    public WishListItem(String username, Item item, boolean bought)
    {
        this.username = username;
        this.item = item;
        this.bought = bought;
    }

    public String getUsername()
    {
        return username;
    }

    public Item getItem()
    {
        return item;
    }

    public boolean isBought()
    {
        return bought;
    }

    public boolean equalsWithoutPrice(Object o) throws RemoteException
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishListItem that = (WishListItem) o;

        if (!username.equals(that.username)) return false;
        return item.getName().equals(that.item.getName());
    }

    public boolean equalsWithoutBought(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishListItem that = (WishListItem) o;

        if (!username.equals(that.username)) return false;
        return item.equals(that.item);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WishListItem that = (WishListItem) o;

        if (bought != that.bought) return false;
        if (!username.equals(that.username)) return false;
        return item.equals(that.item);
    }

    @Override
    public int hashCode()
    {
        int result = username.hashCode();
        result = 31 * result + item.hashCode();
        result = 31 * result + (bought ? 1 : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "WishListItem{" +
                "username='" + username + '\'' +
                ", item=" + item +
                ", bought=" + bought +
                '}';
    }

    public String toDisplayString() throws RemoteException{
        return "WishListItem{" +
                "username=" + username +
                ", item=" + item.toDisplayString() +
                ", bought=" + bought +
                '}';
    }
}
